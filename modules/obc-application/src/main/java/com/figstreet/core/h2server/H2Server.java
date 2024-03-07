package com.figstreet.core.h2server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.figstreet.core.CompareUtil;
import com.figstreet.core.Logging;
import com.figstreet.core.RunState;
import com.figstreet.core.crypto.CryptoUtil;
import com.figstreet.core.crypto.HashingCrypto;
import com.figstreet.core.crypto.SecureString;
import org.apache.commons.codec.DecoderException;
import org.h2.util.JdbcUtils;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class H2Server
{
	private File fLockFile;
	private FileOutputStream fLockFileStream;
	private FileLock fLockFileLock;
	private RunState fRunState = RunState.STOPPED;

	private static H2Server fMainApp = new H2Server();

	private File fConfigFile;
	private H2ServerConfig fConfig;

	private boolean fWebServerStartAttempted;
	private H2WebServer fWebServer;

	private boolean fDatabaseServerStartAttempted;
	private H2DatabaseServer fDatabaseProcessor;

	public static void main(String[] pArgs)
	{
		boolean deleteLockFile = false;

		try
		{
			// this is needed when called from Apache daemon as Windows' Service for Log4j to use SMTPAppender.
			Thread.currentThread().setContextClassLoader(H2Server.class.getClassLoader());
			fMainApp.init(pArgs);
			Arrays.fill(pArgs, null);
			pArgs = null;
			try
			{
				fMainApp.lockProcess();
				deleteLockFile = true;
				fMainApp.doWork();
			}
			finally
			{
				fMainApp.unlockProcess(deleteLockFile);
			}

		}
		catch (Exception e)
		{
			Logging.fatal(LOGGER_NAME, "main", "An error occurred while running H2Server and it had to shutdown", e);
			e.printStackTrace(System.err);
		}

		if (pArgs != null)
		{
			Arrays.fill(pArgs, null);
			pArgs = null;
		}
		shutdown();
		fMainApp.fRunState = RunState.STOPPED;
	}

	public static void stopRunning()
	{
		if ((fMainApp != null) && RunState.RUNNING.equals(fMainApp.fRunState))
			fMainApp.fRunState = RunState.STOPPING;
	}

	public static void shutdown()
	{
		fMainApp.stopStartServices(false);
	}

	public static boolean isStopped()
	{
		if (fMainApp != null)
			return (RunState.STOPPED.equals(fMainApp.fRunState));
		return true;
	}

	private void stopStartServices(boolean pDoStart)
	{
		this.fWebServerStartAttempted = false;
		if (this.fWebServer != null)
			this.fWebServer.shutdown();

		this.fDatabaseServerStartAttempted = false;
		if (this.fDatabaseProcessor != null)
			this.fDatabaseProcessor.shutdown();

		if (pDoStart)
		{
			if (this.fConfig.isStartWebServer())
			{
				this.fWebServerStartAttempted = true;
				this.fWebServer =
					new H2WebServer(this.fConfig.getRunProcessorEveryMillis(), this.fConfig.getWebServerPort(),
						this.fConfig.getWebServerAllowOthers(), this.fConfig.getWebServerUseSSL());
			}
			if (this.fConfig.isStartTcpDatabaseServer())
			{
				this.fDatabaseServerStartAttempted = true;
				this.fDatabaseProcessor = new H2DatabaseServer(this.fConfig.getRunProcessorEveryMillis(),
					this.fConfig.getTcpServerPort(), this.fConfig.getTcpServerAllowOthers(),
					this.fConfig.getTcpServerUseSSL(), this.fConfig.getTcpServerAllowDatabaseCreation());
			}

			String dbURL = this.fConfig.getDatabaseInitURL();
			if (!CompareUtil.isEmpty(dbURL))
			{
				Logging.infof(LOGGER_NAME, "stopStartServices", "Attempting to connect to database %s", dbURL);
				try
				{
					char[] adminUser = this.fConfig.getAdminUser();
					char[] adminPass = this.fConfig.getAdminPassword();

					if (adminUser == null || adminUser.length < 1)
						Logging.warn(LOGGER_NAME, "stopStartServices", "Admin user name required to connect to database");
					else
					{
						Connection conn = null;
						if (this.fConfig.getPageCacheKB() != null)
							dbURL += String.format(";CACHE_SIZE=%d", this.fConfig.getPageCacheKB());
						try
						{
							org.h2.Driver.load();
							String pass = null;
							if (adminPass != null && adminPass.length > 0)
							{
								byte[] decoded;
								CryptoUtil.EncodingType encodingType = CryptoUtil.EncodingType.getInstance(this.fConfig.getKeyStoreEncodingType());
								if (CryptoUtil.EncodingType.HEX.equals(encodingType))
									decoded = CryptoUtil.decodeHexCharsToBytes(this.fConfig.getAdminPassword());
								else
									decoded = CryptoUtil.decodeBase64CharsToBytes(this.fConfig.getAdminPassword());
								pass = new String(decoded, StandardCharsets.UTF_8);
								CryptoUtil.clearArray(decoded);
							}
							conn = DriverManager.getConnection(dbURL, new String(adminUser),
								pass);
						}
						finally
						{
							JdbcUtils.closeSilently(conn);
						}
					}
					CryptoUtil.clearArray(adminUser);
					adminUser = null;
					CryptoUtil.clearArray(adminPass);
					adminPass = null;
				}
				catch (Exception e)
				{
					Logging.error(LOGGER_NAME, "stopStartServices", "Error connecting to databaseInitURL; server still started", e);
				}
			}
		}
	}

	private void loadKeyStore(H2ServerConfig pConfig, SecureString pPassword, boolean pInitialLoad) throws DecoderException
	{
		if (!pInitialLoad)
		{
			System.setProperty("javax.net.ssl.keyStore", "");
			System.setProperty("javax.net.ssl.keyStorePassword", "");
			System.setProperty("javax.net.ssl.keyStoreType", "");
		}

		String keyStoreFile = pConfig.getKeyStoreFile();
		if (!CompareUtil.isEmpty(keyStoreFile))
			System.setProperty("javax.net.ssl.keyStore", keyStoreFile);

		String keyStoreType = "jks";
		if (!CompareUtil.isEmpty(pConfig.getKeyStoreType()))
			keyStoreType = pConfig.getKeyStoreType().toLowerCase();
		System.setProperty("javax.net.ssl.keyStoreType", keyStoreType);

		char[] keyStoreSalt = null;
		char[] keyStoreKey = null;
		boolean passwordSet = false;
		if (pPassword != null && !pPassword.isEmpty())
		{
			keyStoreSalt = pConfig.getKeyStoreSalt();
			keyStoreKey = pConfig.getKeyStoreKey();
			if (keyStoreSalt != null && keyStoreSalt.length > 0 && keyStoreKey != null && keyStoreKey.length > 0)
			{
				CryptoUtil.EncodingType encodingType = CryptoUtil.EncodingType.getInstance(pConfig.getKeyStoreEncodingType());
				byte[] hash = HashingCrypto.hashPassword(pConfig.getKeyStoreAlgorithm(),
					encodingType, keyStoreSalt, keyStoreKey, pPassword.copyValue());

				if (CryptoUtil.EncodingType.HEX.equals(encodingType))
					System.setProperty("javax.net.ssl.keyStorePassword", CryptoUtil.encodeToHexString(hash));
				else if (CryptoUtil.EncodingType.BASE64.equals(encodingType))
					System.setProperty("javax.net.ssl.keyStorePassword", CryptoUtil.encodeToBase64String(hash));
				else
					System.setProperty("javax.net.ssl.keyStorePassword", new String(hash, StandardCharsets.UTF_8));

				CryptoUtil.clearArray(hash);
				hash = null;
				passwordSet = true;
			}
			else
				Logging.error(LOGGER_NAME, "init", "Cannot set keyStorePassword without salt & key.");
		}

		CryptoUtil.clearArray(keyStoreSalt);
		keyStoreSalt = null;
		CryptoUtil.clearArray(keyStoreKey);
		keyStoreKey = null;

		if (!CompareUtil.isEmpty(keyStoreFile) && !passwordSet)
		{
			Logging.error(LOGGER_NAME, "init", "KeyStore specified without a password.");
		}
	}

	protected void init(String[] pArgs) throws NoSuchAlgorithmException, IOException, DecoderException
	{
		OptionParser parser = new OptionParser();
		File configFile = new File("H2Server-conf.xml");
		OptionSpec<File> configFileOption =
			parser.accepts("c").withRequiredArg().ofType(File.class).defaultsTo(configFile);
		OptionSpec<SecureString> keystorePassCodeOption =
			parser.accepts("kp").withOptionalArg().ofType(SecureString.class);

		OptionSet userArgs = parser.parse(pArgs);

		this.fConfigFile = userArgs.valueOf(configFileOption);
		this.fConfig = new H2ServerConfig(this.fConfigFile);

		Logging.initialize(this.fConfig.getLog4JConf());
		SecureString password = userArgs.valueOf(keystorePassCodeOption);

		if (password == null && userArgs.has(keystorePassCodeOption))
			password = new SecureString(System.console().readPassword("KeyStore Pass Code: "));
		else
		{
			CryptoUtil.EncodingType encodingType = CryptoUtil.EncodingType.getInstance(this.fConfig.getKeyStoreEncodingType());
			byte[] decoded;
			if (CryptoUtil.EncodingType.HEX.equals(encodingType))
				decoded = CryptoUtil.decodeHexCharsToBytes(this.fConfig.getKeyStoreCode());
			else
				decoded = CryptoUtil.decodeBase64CharsToBytes(this.fConfig.getKeyStoreCode());
			password = new SecureString(new String(decoded, StandardCharsets.UTF_8));
			CryptoUtil.clearArray(decoded);
		}

		this.loadKeyStore(this.fConfig, password, true);
		password.clearValue();
		password = null;
		userArgs = null;
		parser = null;
		Logging.error(LOGGER_NAME, "init", "Starting H2Server!!!");
		this.stopStartServices(true);

		this.fConfig.clearSecureElements();
	}

	/**
	 * Drop lock file to stop 2nd instance of App from running at the same time.
	 *
	 * @throws IOException
	 */
	private void lockProcess() throws IOException
	{
		this.fLockFile = new File("h2server.lck");
		this.fLockFile.createNewFile(); // don't fail if already exists

		this.fLockFileStream = new FileOutputStream(this.fLockFile);

		this.fLockFileLock = this.fLockFileStream.getChannel().tryLock();
		if (this.fLockFileLock == null)
			throw new IOException("Can't get lock on file.");
	}

	/**
	 * Remove lock file.
	 */
	private void unlockProcess(boolean deleteLockFile)
	{
		if (this.fLockFileLock != null)
			try
			{
				this.fLockFileLock.release();
			}
			catch (Exception ignore)
			{/* do nothing */
			}
		this.fLockFileLock = null;
		if (this.fLockFileStream != null)
			try
			{
				this.fLockFileStream.close();
			}
			catch (Exception ignore)
			{/* do nothing */
			}
		this.fLockFileStream = null;
		if (deleteLockFile && (this.fLockFile != null))
			try
			{
				this.fLockFile.delete();
			}
			catch (Exception ignore)
			{/* do nothing */
			}
	}

	protected void doWork() throws Exception
	{
		Logging.info(LOGGER_NAME, "doWork", "Start...");

		this.fRunState = RunState.RUNNING;
		Runtime.getRuntime().addShutdownHook(new ShutDownH2Server());

		long nextRun = System.currentTimeMillis();

		while (RunState.RUNNING.equals(this.fRunState))
		{
			if (System.currentTimeMillis() >= nextRun)
			{
				try
				{
					Logging.debug(LOGGER_NAME, "doWork", "Checking config file ...");
					if (this.fConfig.isConfigOutOfDate(this.fConfigFile))
					{
						Logging.debug(LOGGER_NAME, "doWork", "Config file has changed, reloading configuration.");
						this.fConfig = new H2ServerConfig(this.fConfigFile);
						if (this.fConfig.getConfigChangeRestartsServices())
						{
							Logging.error(LOGGER_NAME, "doWork", "Config file has changed, restarting services.");
							this.stopStartServices(true);
						}
					}

					Logging.debug(LOGGER_NAME, "doWork", "Checking child processes ...");
					if (this.fWebServerStartAttempted)
					{
						if (this.fWebServer == null || !this.fWebServer.isAlive())
							Logging.fatal(LOGGER_NAME, "doWork", "WebServer is not alive, UI access to database is down!");
					}

					if (this.fDatabaseServerStartAttempted)
					{
						if (this.fDatabaseProcessor == null || !this.fDatabaseProcessor.isAlive())
							Logging.fatal(LOGGER_NAME, "doWork", "DatabaseProcessor is not alive, database may be down!");
					}
				}
				catch (Exception e)
				{
					Logging.fatal(LOGGER_NAME, "doWork", "Caught unexpected exception", e);
				}

				long interval = this.fConfig.getRunMainEveryMillis();
				nextRun = nextRun + ((((System.currentTimeMillis() - nextRun) / interval) + 1) * interval);
				Logging.debug(LOGGER_NAME, "doWork", "Finished.");
			}

			if (RunState.RUNNING.equals(this.fRunState))
				// noinspection BusyWait
				TimeUnit.SECONDS.sleep(1);
		}

		Logging.info(LOGGER_NAME, "doWork", "Done!");
	}

	private static final String LOGGER_NAME = H2Server.class.getPackage().getName() + ".H2Server";
}
