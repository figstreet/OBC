package com.figstreet.core.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.figstreet.core.CompareUtil;
import com.figstreet.core.h2server.H2ServerConfig;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.digest.HmacAlgorithms;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class CryptoApp
{
	public static void main(String[] pArgs)
		throws DecoderException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException,
		UnrecoverableEntryException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
		InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		if ("genKeys".equalsIgnoreCase(pArgs[1]))
		{
			OptionParser parser = new OptionParser();
			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
			OptionSpec<SecureString> passwordOption = parser.accepts("p").withOptionalArg().ofType(SecureString.class);
			OptionSpec<String> encodingOption = parser.accepts("e").withOptionalArg().ofType(String.class);
			OptionSpec<String> algorithmOption =
				parser.accepts("a").withOptionalArg().ofType(String.class).defaultsTo("HMAC_SHA_256");

			OptionSet userArgs = parser.parse(pArgs);
			String function = userArgs.valueOf(functionOption); //not used

			CryptoUtil.EncodingType encodingType = null;
			String encodingArg = userArgs.valueOf(encodingOption);
			if (encodingArg != null)
				encodingType = CryptoUtil.EncodingType.getInstance(encodingArg);

			HmacAlgorithms algorithm = HmacAlgorithms.HMAC_SHA_256;
			String algorithmArg = userArgs.valueOf(algorithmOption);
			if (algorithmArg != null)
				algorithm = HmacAlgorithms.valueOf(algorithmArg);

			SecureString password = userArgs.valueOf(passwordOption);
			if (password == null)
				password = new SecureString(System.console().readPassword("Password: "));

			ArrayList<byte[]> secureStuff =
				HashingCrypto.generateSaltKeyAndHash(algorithm, SecureString.getValue(password));

			password.clearValue();
			userArgs = null;
			parser = null;

			if (CryptoUtil.EncodingType.HEX.equals(encodingType))
			{
				System.out.print("Salt: ");
				System.out.println(HashingCrypto.encodeToHexString(secureStuff.get(0)));
				System.out.print("Key:  ");
				System.out.println(HashingCrypto.encodeToHexString(secureStuff.get(1)));
				System.out.print("Hash: ");
				System.out.println(HashingCrypto.encodeToHexString(secureStuff.get(2)));
			}
			else if (CryptoUtil.EncodingType.BASE64.equals(encodingType))
			{
				System.out.print("Salt: ");
				System.out.println(HashingCrypto.encodeToBase64String(secureStuff.get(0)));
				System.out.print("Key:  ");
				System.out.println(HashingCrypto.encodeToBase64String(secureStuff.get(1)));
				System.out.print("Hash: ");
				System.out.println(HashingCrypto.encodeToBase64String(secureStuff.get(2)));
			}
			else
			{
				System.out.print("Salt: ");
				System.out.write(secureStuff.get(0));
				System.out.print("\r\nKey:  ");
				System.out.write(secureStuff.get(1));
				System.out.print("\r\nHash: ");
				System.out.write(secureStuff.get(2));
				System.out.println();
			}

			HashingCrypto.clearArray(secureStuff.get(0));
			HashingCrypto.clearArray(secureStuff.get(1));
			HashingCrypto.clearArray(secureStuff.get(2));
			return;
		}

		if ("hashValue".equalsIgnoreCase(pArgs[1]))
		{
			OptionParser parser = new OptionParser();
			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
			OptionSpec<SecureString> saltOption = parser.accepts("s").withRequiredArg().ofType(SecureString.class);
			OptionSpec<SecureString> keyOption = parser.accepts("k").withRequiredArg().ofType(SecureString.class);
			OptionSpec<SecureString> passwordOption = parser.accepts("p").withOptionalArg().ofType(SecureString.class);
			OptionSpec<SecureString> codeOption = parser.accepts("c").withOptionalArg().ofType(SecureString.class);
			OptionSpec<String> encodingOption = parser.accepts("e").withOptionalArg().ofType(String.class);
			OptionSpec<String> algorithmOption =
				parser.accepts("a").withOptionalArg().ofType(String.class).defaultsTo("HMAC_SHA_256");

			OptionSet userArgs = parser.parse(pArgs);
			if ((userArgs.has(saltOption) && userArgs.has(keyOption)))
			{
				CryptoUtil.EncodingType encodingType = null;
				String encodingArg = userArgs.valueOf(encodingOption);
				if (encodingArg != null)
					encodingType = CryptoUtil.EncodingType.getInstance(encodingArg);

				HmacAlgorithms algorithm = HmacAlgorithms.HMAC_SHA_256;
				String algorithmArg = userArgs.valueOf(algorithmOption);
				if (algorithmArg != null)
					algorithm = HmacAlgorithms.valueOf(algorithmArg);

				SecureString salt = userArgs.valueOf(saltOption);
				SecureString key = userArgs.valueOf(keyOption);

				SecureString password = userArgs.valueOf(passwordOption);
				if (password == null)
				{
					SecureString passCode = userArgs.valueOf(codeOption);
					if (passCode == null)
						password = new SecureString(System.console().readPassword("Password: "));
					else
					{
						if (CryptoUtil.EncodingType.BASE64.equals(encodingType))
							password = new SecureString(HashingCrypto.decodeBase64StringToChars(passCode.toString()));
						else
							password = new SecureString(HashingCrypto.decodeHexStringToChars(passCode.toString()));
					}
				}

				byte[] hashBytes = HashingCrypto.hashPassword(algorithm, encodingType, SecureString.getValue(salt),
					SecureString.getValue(key), SecureString.getValue(password));

				salt.clearValue();
				salt = null;
				key.clearValue();
				key = null;
				password.clearValue();
				password = null;

				if (CryptoUtil.EncodingType.HEX.equals(encodingType))
					System.out.println(HashingCrypto.encodeToHexString(hashBytes));
				else if (CryptoUtil.EncodingType.BASE64.equals(encodingType))
					System.out.println(HashingCrypto.encodeToBase64String(hashBytes));
				else
				{
					System.out.write(hashBytes);
					System.out.println();
				}

				return;
			}
		}

		if ("encode".equalsIgnoreCase(pArgs[1]))
		{
			OptionParser parser = new OptionParser();
			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
			OptionSpec<String> encodingOption = parser.accepts("e").withRequiredArg().ofType(String.class);
			OptionSpec<String> toEncodeOption = parser.accepts("data").withRequiredArg().ofType(String.class);

			OptionSet userArgs = parser.parse(pArgs);
			String function = userArgs.valueOf(functionOption); //not used

			CryptoUtil.EncodingType encodingType = CryptoUtil.EncodingType.BASE64;
			String encodingArg = userArgs.valueOf(encodingOption);
			if (encodingArg != null)
				encodingType = CryptoUtil.EncodingType.getInstance(encodingArg);

			String toEncode = userArgs.valueOf(toEncodeOption);
			if (!CompareUtil.isEmpty(toEncode))
			{
				if (CryptoUtil.EncodingType.HEX.equals(encodingType))
					System.out.println(HashingCrypto.encodeToHexString(toEncode.getBytes()));
				else
					System.out.println(HashingCrypto.encodeToBase64String(toEncode.getBytes()));
			}

			return;
		}

		if ("decode".equalsIgnoreCase(pArgs[1]))
		{
			OptionParser parser = new OptionParser();
			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
			OptionSpec<String> encodingOption = parser.accepts("e").withRequiredArg().ofType(String.class);
			OptionSpec<String> toDecodeOption = parser.accepts("data").withRequiredArg().ofType(String.class);

			OptionSet userArgs = parser.parse(pArgs);
			String function = userArgs.valueOf(functionOption); //not used

			CryptoUtil.EncodingType encodingType = CryptoUtil.EncodingType.BASE64;
			String encodingArg = userArgs.valueOf(encodingOption);
			if (encodingArg != null)
				encodingType = CryptoUtil.EncodingType.getInstance(encodingArg);

			String toDecode = userArgs.valueOf(toDecodeOption);
			if (!CompareUtil.isEmpty(toDecode))
			{
				if (CryptoUtil.EncodingType.HEX.equals(encodingType))
					System.out.println(
						new String(HashingCrypto.decodeHexStringToBytes(toDecode), StandardCharsets.UTF_8));
				else
					System.out.println(
						new String(HashingCrypto.decodeBase64StringToBytes(toDecode), StandardCharsets.UTF_8));
			}

			return;
		}

		if ("genKS".equalsIgnoreCase(pArgs[1]))
		{
			OptionParser parser = new OptionParser();
			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
			OptionSpec<File> keyStoreFileOption = parser.accepts("ksf").withRequiredArg().ofType(File.class);
			OptionSpec<String> keyStoreTypeOption = parser.accepts("kst").withRequiredArg().ofType(String.class);
			OptionSpec<SecureString> keyStorePWDOption =
				parser.accepts("ksp").withOptionalArg().ofType(SecureString.class);
			OptionSpec<String> entryCodeOption = parser.accepts("ec").withRequiredArg().ofType(String.class);
			OptionSpec<SecureString> securePWDOption =
				parser.accepts("pwd").withOptionalArg().ofType(SecureString.class);

			OptionSet userArgs = parser.parse(pArgs);
			String function = userArgs.valueOf(functionOption); //not used

			File keyStoreFile = userArgs.valueOf(keyStoreFileOption);
			String keyStoreType = userArgs.valueOf(keyStoreTypeOption);
			String entryCode = userArgs.valueOf(entryCodeOption);

			SecureString keyStorePassword = userArgs.valueOf(keyStorePWDOption);
			if (keyStorePassword == null)
				keyStorePassword = new SecureString(System.console().readPassword("KeyStore Password: "));

			SecureString securePassword = userArgs.valueOf(securePWDOption);
			if (securePassword == null)
				securePassword = new SecureString(System.console().readPassword("Secure Password: "));

			int pwdLength = securePassword.getLength();
			if (pwdLength != 16 && pwdLength != 24 && pwdLength != 32)
				throw new KeyStoreException("Secure password length must be 16, 24 or 32 chars");

			KeyStore keyStore = PasswordCrypto.makeNewKeyStore(keyStoreFile, keyStoreType,
				SecureString.getValue(keyStorePassword), entryCode, SecureString.getValue(securePassword));

			keyStorePassword.clearValue();
			securePassword.clearValue();
			userArgs = null;
			parser = null;

			return;
		}

		if ("getPWD".equalsIgnoreCase(pArgs[1]))
		{
			OptionParser parser = new OptionParser();
			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
			OptionSpec<File> keyStoreFileOption = parser.accepts("ksf").withRequiredArg().ofType(File.class);
			OptionSpec<String> keyStoreTypeOption = parser.accepts("kst").withRequiredArg().ofType(String.class);
			OptionSpec<SecureString> keyStorePWDOption =
				parser.accepts("ksp").withOptionalArg().ofType(SecureString.class);
			OptionSpec<String> codeOption = parser.accepts("code").withRequiredArg().ofType(String.class);

			OptionSet userArgs = parser.parse(pArgs);
			String function = userArgs.valueOf(functionOption); //not used

			File keyStoreFile = userArgs.valueOf(keyStoreFileOption);
			String keyStoreType = userArgs.valueOf(keyStoreTypeOption);
			String code = userArgs.valueOf(codeOption);

			SecureString keyStorePassword = userArgs.valueOf(keyStorePWDOption);
			if (keyStorePassword == null)
				keyStorePassword = new SecureString(System.console().readPassword("KeyStore Password: "));

			char[] securePassword = PasswordCrypto.getPasswordFromKeyStore(keyStoreFile, keyStoreType,
				SecureString.getValue(keyStorePassword), code);

			keyStorePassword.clearValue();
			userArgs = null;
			parser = null;

			System.out.println(securePassword);
			PasswordCrypto.clearArray(securePassword);

			return;
		}

//		if ("genLicense".equalsIgnoreCase(pArgs[1]))
//		{
//			OptionParser parser = new OptionParser();
//			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
//			OptionSpec<Date> expirationDateOption = parser.accepts("exp").withRequiredArg().ofType(Date.class);
//			OptionSpec<String> codeOption = parser.accepts("code").withRequiredArg().ofType(String.class);
//			OptionSpec<String> hostnameOption = parser.accepts("host").withOptionalArg().ofType(String.class);
//
//			OptionSet userArgs = parser.parse(pArgs);
//			String function = userArgs.valueOf(functionOption); //not used
//
//			Date expirationDate = userArgs.valueOf(expirationDateOption);
//			String code = userArgs.valueOf(codeOption);
//			String hostname = userArgs.valueOf(hostnameOption);
//
//			System.out.println(LicenseManager.generateLicense(expirationDate, code, hostname,
//				LicenseManager.LICENSE_DELIM, LicenseManager.LICENSE_CHARS));
//
//			return;
//		}
//
//		if ("decryptLicense".equalsIgnoreCase(pArgs[1]))
//		{
//			OptionParser parser = new OptionParser();
//			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
//			OptionSpec<String> codeOption = parser.accepts("license").withRequiredArg().ofType(String.class);
//
//			OptionSet userArgs = parser.parse(pArgs);
//			String function = userArgs.valueOf(functionOption); //not used
//
//			String code = userArgs.valueOf(codeOption);
//
//			String licenseValue = LicenseManager.decryptLicense(code, LicenseManager.LICENSE_CHARS);
//			Long licenseExpirationValue = null;
//			System.out.println(licenseValue);
//
//			String[] elements = licenseValue.split(LicenseManager.LICENSE_DELIM);
//			if (elements.length == 4 || elements.length == 5)
//			{
//				licenseExpirationValue = Long.parseLong(elements[1]);
//				Date expirationDate = new Date(licenseExpirationValue);
//				System.out.println("Expires: " + DateUtil.formatBoth(expirationDate));
//			}
//			else
//			{
//				System.out.println("License elements were not formatted as expected!");
//			}
//
//			return;
//		}

		if ("extractH2Cert".equalsIgnoreCase(pArgs[1]))
		{
			OptionParser parser = new OptionParser();
			OptionSpec<String> functionOption = parser.accepts("f").withRequiredArg().ofType(String.class);
			OptionSpec<String> pathOption = parser.accepts("path").withRequiredArg().ofType(String.class);

			OptionSet userArgs = parser.parse(pArgs);
			String function = userArgs.valueOf(functionOption); //not used

			String pathToH2Conf = userArgs.valueOf(pathOption);

			File configFile = new File(pathToH2Conf);
			H2ServerConfig config = new H2ServerConfig(configFile);
			CryptoUtil.EncodingType encodingType = CryptoUtil.EncodingType.getInstance(config.getKeyStoreEncodingType());
			byte[] decoded;
			if (CryptoUtil.EncodingType.HEX.equals(encodingType))
				decoded = CryptoUtil.decodeHexCharsToBytes(config.getKeyStoreCode());
			else
				decoded = CryptoUtil.decodeBase64CharsToBytes(config.getKeyStoreCode());
			SecureString password = new SecureString(new String(decoded, StandardCharsets.UTF_8));
			CryptoUtil.clearArray(decoded);

			String keyStoreFile = config.getKeyStoreFile();
			String keyStoreType = "jks";
			if (!CompareUtil.isEmpty(config.getKeyStoreType()))
				keyStoreType = config.getKeyStoreType().toLowerCase();

			char[] keyStoreSalt = config.getKeyStoreSalt();
			char[] keyStoreKey = config.getKeyStoreKey();

			byte[] hash = HashingCrypto.hashPassword(config.getKeyStoreAlgorithm(), encodingType, keyStoreSalt,
				keyStoreKey, password.copyValue());

			String keystorePassword = null;
			if (CryptoUtil.EncodingType.HEX.equals(encodingType))
				keystorePassword = CryptoUtil.encodeToHexString(hash);
			else if (CryptoUtil.EncodingType.BASE64.equals(encodingType))
				keystorePassword = CryptoUtil.encodeToBase64String(hash);
			else
				keystorePassword = new String(hash, StandardCharsets.UTF_8);

			//System.out.println(keystorePassword);

			KeyStore ks = KeyStore.getInstance(keyStoreType);
			ks.load(null, keystorePassword.toCharArray());

			FileInputStream fin = null;
			FileOutputStream fos = null;
			try
			{
				fin = new FileInputStream(keyStoreFile);
				ks.load(fin, keystorePassword.toCharArray());

				Certificate cert = ks.getCertificate("H2");

				File file = new File(configFile.getParent(), "H2_" + System.currentTimeMillis() + ".cer");
				byte[] buf = cert.getEncoded();
				String encoded = CryptoUtil.encodeToBase64String(buf);
				encoded = "-----BEGIN CERTIFICATE-----\r\n" + encoded + "\r\n-----END CERTIFICATE-----";

				fos = new FileOutputStream(file);
				OutputStreamWriter wr = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
				wr.write(encoded);
				wr.flush();
				wr.close();
			}
			finally
			{
				if (fin != null)
					fin.close();
				if (fos != null)
					fos.close();
			}

			CryptoUtil.clearArray(hash);
			hash = null;

			CryptoUtil.clearArray(keyStoreSalt);
			keyStoreSalt = null;
			CryptoUtil.clearArray(keyStoreKey);
			keyStoreKey = null;

			password.clearValue();
			password = null;

			return;
		}

		System.err.println("Usage: CryptoApp -f genKeys -p (optional) -e (optional) -a (optional)");
		System.err.println("\tf: Function (required)");
		System.err.println("\t genKeys - Generates a hash of the supplied password");
		System.err.println("\tp: Password");
		System.err.println("\t Can be specified on command line OR app will prompt");
		System.err.println("\te: Encoding Type");
		System.err.println("\t HEX");
		System.err.println("\t BASE64");
		System.err.println("\t no default, bytes printed to console");
		System.err.println("\ta: Algorithm");
		System.err.println("\t HMAC_MD5");
		System.err.println("\t HMAC_SHA_1");
		System.err.println("\t HMAC_SHA_256 (default)");
		System.err.println("\t HMAC_SHA_384");
		System.err.println("\t HMAC_SHA_512\r\n");

		System.err.println("OR: CryptoApp -f hashValue -s -k -p (optional) -e (optional) -a (optional)");
		System.err.println("\tf: Function (required)");
		System.err.println("\t hashValue");
		System.err.println("\ts: Salt (required)");
		System.err.println("\tk: Key (required)");
		System.err.println("\tp: Password");
		System.err.println("\t Can be specified on command line OR app will prompt");
		System.err.println("\te: Encoding Type");
		System.err.println("\t HEX");
		System.err.println("\t BASE64");
		System.err.println("\t no default, bytes printed to console");
		System.err.println("\ta: Algorithm");
		System.err.println("\t HMAC_MD5");
		System.err.println("\t HMAC_SHA_1");
		System.err.println("\t HMAC_SHA_256 (default)");
		System.err.println("\t HMAC_SHA_384");
		System.err.println("\t HMAC_SHA_512");

		System.err.println(
			"OR: CryptoApp -f genKS -ksf (required) -kst (required) -ksp (optional) -ec (required) -pwd (optional)");
		System.err.println("\tf: Function (required)");
		System.err.println("\t genKS - Creates a KeyStore to store the secure pwd value");
		System.err.println("\tksf: KeyStore file");
		System.err.println("\tkst: KeyStore Type");
		System.err.println("\t jceks");
		System.err.println("\t jks");
		System.err.println("\tksp: KeyStore Password");
		System.err.println("\t Can be specified on command line OR app will prompt");
		System.err.println("\tec: KeyStore Entry Code");
		System.err.println("\tpwd: Secure Password");
		System.err.println("\t Can be specified on command line OR app will prompt");

		System.err.println("OR: CryptoApp -f genLicense -exp (required) -code (required) -host (optional)");
		System.err.println("\tf: Function (required)");
		System.err.println("\t genLicense - Creates license key that can be pasted into a license file");
		System.err.println("\texp: Expiration date YYYY-MM-dd");
		System.err.println("\tcode: Client's license code");
		System.err.println("\thost: The hostname to license");

		System.err.println("OR: CryptoApp -f decryptLicense -license (required)");
		System.err.println("\tf: Function (required)");
		System.err.println("\t decryptLicense - Decrypts the license key");
		System.err.println("\tlicense: Client's license key");
	}

}
