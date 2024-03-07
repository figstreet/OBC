package com.figstreet.core.crypto;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.HashMap;

import com.figstreet.core.ClientID;
import com.figstreet.core.Logging;
import com.figstreet.data.client.Client;
import com.figstreet.data.client.ClientList;
import com.figstreet.data.users.UsersID;
import org.apache.commons.codec.DecoderException;

import javax.mail.search.SearchException;


/**
 * Password based encryption configuration used to secure data stored for a given client.
 * Key is used by methods in PasswordCrypto to encrypt / decrypt sensitive data.
 *
 * To build a keystore with your encryption key:
 *  1. Generate a hashed password for the keystore
 *  	CryptoApp -f genKeys -p mypassword -e BASE64 -a HMAC_SHA_256
 *  2. Create a keystore, protected by the HASH generated in step 1:
 *  	CryptoApp -f genKS -ksf /path/to/file.keystore -kst jceks -ksp HASHED_KS_PWD -ec keystoreentry -pwd myencryptionkey
 *
 *  Note: the -pwd value should be 16, 24 or 32 characters
 *
 */
public class PasswordCryptoConfig extends CryptoConfig
{
	public static final String KEYSTOREENTRY_PROPERTY = "KEYSTOREENTRY";

	private static final String LOGGER_NAME = PasswordCryptoConfig.class.getPackage().getName() + ".PasswordCryptoConfig";
	public static final String CONFIG_NAME = "PASSWORDCRYPTO";
	private static final HashMap<ClientID, PasswordCryptoConfig> CONFIG_MAP =
		new HashMap<ClientID, PasswordCryptoConfig>();

	@Override
	public String getLoggerName()
	{
		return LOGGER_NAME;
	}

	public static void initializeKeys()
	{
		Logging.debugBegin(LOGGER_NAME, "initializeKeys");
		try
		{
			ClientList clientList = ClientList.loadAll(true);
			for (Client client : clientList)
			{
				PasswordCryptoConfig cryptoConfig = getConfig(client.getRecordID());
				cryptoConfig.getCryptoKey();
			}
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "initializeKeys", "Error initializing keys", e);
		}
	}


	public static PasswordCryptoConfig getConfig(ClientID pClientID)
	{
		PasswordCryptoConfig config = CONFIG_MAP.get(pClientID);
		if (config == null)
		{
			synchronized (CONFIG_MAP)
			{
				config = new PasswordCryptoConfig(pClientID);
				CONFIG_MAP.put(pClientID, config);
			}
		}

		return config;
	}

	public static void refreshCache(UsersID pBy, boolean pMarkModified)
		throws SearchException, SQLException
	{
		synchronized (CONFIG_MAP)
		{
			for (PasswordCryptoConfig cryptoConfig : CONFIG_MAP.values())
			{
				if (pMarkModified)
					cryptoConfig.markCacheModified(pBy);

				cryptoConfig.refresh();
			}
		}
	}

	private transient volatile byte[] fCryptoKey;
	private transient volatile boolean fCryptoKeyLoaded = false;

	private PasswordCryptoConfig(ClientID pClientID)
	{
		super(CONFIG_NAME, pClientID);
	}

	@Override
	public void refresh()
	{
		this.clearCryptoKey();
		super.refresh();
	}

	/**
	 * Keystore entry code for the encryption key
	 */
	public String getKeyStoreEntry()
	{
		return this.getProperty(KEYSTOREENTRY_PROPERTY);
	}

	public void clearCryptoKey()
	{
		synchronized (this)
		{
			this.fCryptoKeyLoaded = false;
			this.fCryptoKey = null;
		}
	}

	public byte[] getCryptoKey()
	{
		if (!this.fCryptoKeyLoaded)
		{
			synchronized (this)
			{
				if (!this.fCryptoKeyLoaded)
				{
					char[] keyStorePassword = null;
					char[] cryptoChars = null;
					try
					{
						keyStorePassword = this.getKeyStorePassword();
						File keystore = new File(this.getKeyStorePath());
						cryptoChars = PasswordCrypto.getPasswordFromKeyStore(keystore, this.getKeyStoreType(), keyStorePassword,
							this.getKeyStoreEntry());
						this.fCryptoKey = CryptoUtil.toBytes(cryptoChars);
					}
					catch (DecoderException | KeyStoreException | NoSuchAlgorithmException | CertificateException
						| UnrecoverableEntryException | InvalidKeySpecException | IOException e)
					{
						String msg =
							String.format("Error retrieving key from %s, returning empty value", this.getKeyStorePath());
						Logging.error(LOGGER_NAME, "getCryptoKey", msg, e);
						this.fCryptoKey = new byte[0];
					}
					finally
					{
						CryptoUtil.clearArray(keyStorePassword);
						keyStorePassword = null;
						CryptoUtil.clearArray(cryptoChars);
						cryptoChars = null;

						this.fCryptoKeyLoaded = true;
					}
				}
			}
		}

		return this.fCryptoKey;
	}

}
