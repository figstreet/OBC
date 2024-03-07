package com.figstreet.core.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.figstreet.core.ClientID;
import com.figstreet.data.configvalue.ConfigValue;
import com.figstreet.data.configvalue.ConfigValueList;
import com.figstreet.data.users.UsersID;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.digest.HmacAlgorithms;

import com.figstreet.core.Logging;
import com.figstreet.core.CompareUtil;

public class PasswordCrypto extends CryptoUtil
{
	private static final int BYTE_SIZE = 16;
	private static final int BIT_LENGTH = 128;
	private static final String KEY_FACTORY_ALGORITHM = "PBEWithHmacSHA256AndAES_128";
	private static final String KEY_SPEC = "AES";
	private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";

	private static volatile SecureRandom random = null;
	private static long lastRandom = 0;
	private static Object lock = new Object();

	private static SecureRandom getRandom()
	{
		long currentMS = System.currentTimeMillis();
		long diff = currentMS - lastRandom;
		if (random == null || diff > 21600000 || diff <= 0) //regenerate after 6 hours
		{
			synchronized (lock)
			{
				lastRandom = currentMS;
				try
				{
					random = SecureRandom.getInstance("SHA1PRNG", "SUN");
					random.nextBytes(new byte[8]); //ensure seeding
				}
				catch (Exception e)
				{
					if (Logging.isInitialized())
						Logging.error(LOGGER_NAME, "nextBytes",
							"Generating default SecureRandom instead of SHA1PRNG.", e);
					else
						e.printStackTrace();
					random = new SecureRandom(); //Not as secure, but it's something
				}
			}
		}
		return random;
	}

	private static void nextBytes(byte[] pBytes)
	{
		getRandom().nextBytes(pBytes);
	}

	public static String randomString(int pLength)
	{
		if (pLength <= 0 || pLength > 130)
			throw new IllegalArgumentException("Length must be > 0 and <= 130");

		int loops = pLength / 26 + (pLength % 26 == 0 ? 0 : 1);
		String retVal = "";
		while (loops > 0)
		{
			retVal += new BigInteger(130, getRandom()).toString(32);
			loops--;
		}
		if (retVal.length() > pLength)
			retVal = retVal.substring(0, pLength);

		return retVal;
	}

	public static String encryptString(String pPlaintext, byte[] pKey, EncodingType pEncodingType)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
		InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		if (pPlaintext == null || pPlaintext.length() == 0)
			return pPlaintext;

		return encryptChars(pPlaintext.toCharArray(), pKey, pEncodingType);
	}

	public static String encryptChars(char[] pPlaintext, byte[] pKey, EncodingType pEncodingType)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
		InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		if (pPlaintext == null)
			return null;
		if (pPlaintext.length == 0)
			return "";

		byte[] encrypted = null;
		String encryptedString = null;
		try
		{
			encrypted = encrypt(toBytes(pPlaintext), pKey);
			if (EncodingType.BASE64.equals(pEncodingType))
				encryptedString = encodeToBase64String(encrypted);
			else if (EncodingType.HEX.equals(pEncodingType))
				encryptedString = encodeToHexString(encrypted);
			else
				encryptedString = new String(encrypted, StandardCharsets.UTF_8);
		}
		finally
		{
			clearArray(encrypted);
			encrypted = null;
		}
		return encryptedString;
	}

	public static byte[] encrypt(byte[] pToEncrypt, byte[] pKey)
		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
		InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		// Generate IV for Encryption
		byte[] iv = new byte[BYTE_SIZE];
		byte[] es = null;
		byte[] outputBytes = null;
		Cipher cipher = null;
		try
		{
			nextBytes(iv);

			SecretKeySpec keySpec = new SecretKeySpec(pKey, KEY_SPEC);
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);

			// Generated Authentication Tag should be 128 bits
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(BIT_LENGTH, iv));
			es = cipher.doFinal(pToEncrypt);

			// Construct Output as "IV + CIPHERTEXT"
			outputBytes = new byte[BYTE_SIZE + es.length];
			System.arraycopy(iv, 0, outputBytes, 0, BYTE_SIZE);
			System.arraycopy(es, 0, outputBytes, BYTE_SIZE, es.length);
		}
		finally
		{
			clearArray(iv);
			iv = null;
			clearArray(es);
			es = null;
			cipher = null;
		}
		return outputBytes;
	}

	public static char[] decryptChars(String pToDecrypt, byte[] pKey, EncodingType pEncodingType)
		throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, DecoderException
	{
		byte[] toDecrypt;
		if (EncodingType.BASE64.equals(pEncodingType))
			toDecrypt = decodeBase64StringToBytes(pToDecrypt);
		else if (EncodingType.HEX.equals(pEncodingType))
			toDecrypt = decodeHexStringToBytes(pToDecrypt);
		else
			toDecrypt = toBytes(pToDecrypt.toCharArray());

		byte[] decrypted = null;
		try
		{
			decrypted = decrypt(toDecrypt, pKey);
			return toChars(decrypted);
		}
		finally
		{
			clearArray(decrypted);
			decrypted = null;
		}
	}

	public static String decryptString(String pToDecrypt, byte[] pKey, EncodingType pEncodingType)
		throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, DecoderException
	{
		char[] decrypted = null;
		try
		{
			decrypted = decryptChars(pToDecrypt, pKey, pEncodingType);
			return new String(decrypted);
		}
		finally
		{
			clearArray(decrypted);
			decrypted = null;
		}
	}

	public static byte[] decrypt(byte[] pToDecrypt, byte[] pKey)
		throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
	{
		// Check Minimum Length (IV + TAG (16))
		if (pToDecrypt != null && pToDecrypt.length > BYTE_SIZE + 16)
		{
			byte[] iv = null;
			byte[] es = null;
			Cipher cipher = null;
			try
			{
				iv = Arrays.copyOfRange(pToDecrypt, 0, BYTE_SIZE);
				es = Arrays.copyOfRange(pToDecrypt, BYTE_SIZE, pToDecrypt.length);

				// Perform Decryption
				SecretKeySpec secretKeySpec = new SecretKeySpec(pKey, KEY_SPEC);
				cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new GCMParameterSpec(BIT_LENGTH, iv));

				// Return our Decrypted String
				return cipher.doFinal(es);
			}
			finally
			{
				clearArray(iv);
				iv = null;
				clearArray(es);
				es = null;
				cipher = null;
			}
		}
		throw new InvalidAlgorithmParameterException("Encrypted text not long enough.");
	}

	public static KeyStore makeNewKeyStore(File pKeyStoreFile, String pKeyStoreType, char[] pKeyStorePassword,
		String pPasswordEntry, char[] pPassword)
		throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, InvalidKeySpecException
	{
		KeyStore ks = KeyStore.getInstance(pKeyStoreType);
		ks.load(null, pKeyStorePassword);

		if (pPasswordEntry != null && pPassword != null)
			addPasswordEntry(ks, pKeyStorePassword, pPasswordEntry, pPassword);

		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(pKeyStoreFile);
			ks.store(fos, pKeyStorePassword);
		}
		finally
		{
			if (fos != null)
				fos.close();
		}
		return ks;
	}

	public static void addPasswordEntry(KeyStore pKeyStore, char[] pKeyStorePassword, String pPasswordEntry,
		char[] pPassword) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException
	{
		KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(pKeyStorePassword);

		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
		SecretKey generatedSecret = factory.generateSecret(new PBEKeySpec(pPassword));
		pKeyStore.setEntry(pPasswordEntry, new KeyStore.SecretKeyEntry(generatedSecret), keyStorePP);
	}

	public static void addPasswordEntry(File pKeyStoreFile, String pKeyStoreType, char[] pKeyStorePassword,
		String pPasswordEntry, char[] pPassword)
		throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException, IOException
	{
		KeyStore ks = KeyStore.getInstance(pKeyStoreType);
		ks.load(null, pKeyStorePassword);

		addPasswordEntry(ks, pKeyStorePassword, pPasswordEntry, pPassword);

		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(pKeyStoreFile);
			ks.store(fos, pKeyStorePassword);
		}
		finally
		{
			if (fos != null)
				fos.close();
		}
	}

	public static char[] getPasswordFromKeyStore(File pKeyStoreFile, String pKeyStoreType, char[] pKeyStorePassword,
		String pPasswordEntry) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
		UnrecoverableEntryException, InvalidKeySpecException
	{
		KeyStore ks = KeyStore.getInstance(pKeyStoreType);
		ks.load(null, pKeyStorePassword);

		FileInputStream fin = null;
		try
		{
			try
			{
				fin = new FileInputStream(pKeyStoreFile);
				ks.load(fin, pKeyStorePassword);
			}
			catch (Exception e)
			{
				Logging.error(LOGGER_NAME, "getPasswordFromKeyStore", e);
				throw new IOException(e);
			}
		}
		finally
		{
			if (fin != null)
				fin.close();
		}

		return getPasswordFromKeyStore(ks, pKeyStorePassword, pPasswordEntry);
	}

	public static char[] getPasswordFromKeyStore(KeyStore pKeyStore, char[] pKeyStorePassword, String pPasswordEntry)
		throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, InvalidKeySpecException
	{
		KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(pKeyStorePassword);
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
		KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) pKeyStore.getEntry(pPasswordEntry, keyStorePP);
		PBEKeySpec keySpec = (PBEKeySpec) factory.getKeySpec(ske.getSecretKey(), PBEKeySpec.class);

		return keySpec.getPassword();
	}

	public static ConfigValueList generateClientCryptoConfig(ClientID pClientID, String pClientName, Path pOutputFolder,
															 String pKeyStorePassword, char[] pSecureKey, boolean pUseWindowsPath, UsersID pBy)
		throws KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, IOException
	{
		ConfigValueList configValues = new ConfigValueList();

		if (CompareUtil.isEmpty(pKeyStorePassword) || pKeyStorePassword.length() < 10
			|| pKeyStorePassword.length() > 50)
			pKeyStorePassword = randomString(25);

		HmacAlgorithms algorithm = HmacAlgorithms.HMAC_SHA_256;
		ArrayList<byte[]> pwdSecureStuff =
			HashingCrypto.generateSaltKeyAndHash(algorithm, pKeyStorePassword.toCharArray());
		String base64Salt = HashingCrypto.encodeToBase64String(pwdSecureStuff.get(0));
		String base64Key = HashingCrypto.encodeToBase64String(pwdSecureStuff.get(1));
		String base64Pwd = HashingCrypto.encodeToBase64String(pwdSecureStuff.get(2));

		String keyStoreType = "JCEKS";
		String keyStoreFilename = pClientName.toLowerCase() + ".keystore";
		File keyStoreFile = new File(pOutputFolder.toFile(), keyStoreFilename);
		if (keyStoreFile.exists())
		{
			keyStoreFilename = pClientName.toLowerCase() + "_" + randomString(6) + ".keystore";
			keyStoreFile = new File(pOutputFolder.toFile(), keyStoreFilename);
		}

		if (pSecureKey == null)
			pSecureKey = PasswordCrypto.randomString(24).toCharArray();

		@SuppressWarnings("unused")
		KeyStore keyStore = PasswordCrypto.makeNewKeyStore(keyStoreFile, keyStoreType, base64Pwd.toCharArray(),
			pClientName, pSecureKey);

		String base64Input = HashingCrypto.encodeToBase64String(pKeyStorePassword.getBytes(StandardCharsets.UTF_8));
		ConfigValue CODE = new ConfigValue(pClientID, PasswordCryptoConfig.CONFIG_NAME,
			PasswordCryptoConfig.CODE_PROPERTY, base64Input, pBy);
		//CODE.setNotes("Code used to access the keystore referenced in KEYSTOREPATH (when used in combination with KEY and SALT). Uses HMAC_SHA_256");
		configValues.add(CODE);

		ConfigValue ENCODINGTYPE = new ConfigValue(pClientID, PasswordCryptoConfig.CONFIG_NAME,
			PasswordCryptoConfig.ENCODINGTYPE_PROPERTY, "BASE64", pBy);
		//ENCODINGTYPE.setNotes("Encoding type used for the CODE, KEY & SALT values. Supports BASE64 and HEX");
		configValues.add(ENCODINGTYPE);

		ConfigValue KEY = new ConfigValue(pClientID, PasswordCryptoConfig.CONFIG_NAME,
			PasswordCryptoConfig.KEY_PROPERTY, base64Key, pBy);
		//KEY.setNotes("Key used to access the keystore referenced in KEYSTOREPATH (when used in combination with CODE and SALT). Uses HMAC_SHA_256");
		configValues.add(KEY);

		ConfigValue KEYSTOREENTRY = new ConfigValue(pClientID, PasswordCryptoConfig.CONFIG_NAME,
			PasswordCryptoConfig.KEYSTOREENTRY_PROPERTY, pClientName, pBy);
		//KEYSTOREENTRY.setNotes("Entry associated with the AES key stored within the keystore.");
		configValues.add(KEYSTOREENTRY);

		ConfigValue KEYSTOREPATH = new ConfigValue(pClientID, PasswordCryptoConfig.CONFIG_NAME,
			PasswordCryptoConfig.KEYSTOREPATH_PROPERTY, null, pBy);
		String keyStoreFilePath = keyStoreFile.getAbsolutePath();
		if (!pUseWindowsPath)
		{
			keyStoreFilePath = keyStoreFilePath.replaceFirst("[a-zA-Z]:", "");
			keyStoreFilePath = keyStoreFilePath.replace('\\', '/');
		}
		else
		{
			keyStoreFilePath = keyStoreFilePath.replace('\\', '/');
			keyStoreFilePath = "/" + keyStoreFilePath;
		}
		KEYSTOREPATH.setPropertyValue(keyStoreFilePath);
		//KEYSTOREPATH.setNotes("Full path (on appliance) of keystore storing the AES encryption key.");
		configValues.add(KEYSTOREPATH);

		ConfigValue KEYSTORETYPE = new ConfigValue(pClientID, PasswordCryptoConfig.CONFIG_NAME,
			PasswordCryptoConfig.KEYSTORETYPE_PROPERTY, keyStoreType, pBy);
		//KEYSTORETYPE.setNotes("Type of keystore used to store the AES encryption key.");
		configValues.add(KEYSTORETYPE);

		ConfigValue SALT = new ConfigValue(pClientID, PasswordCryptoConfig.CONFIG_NAME,
			PasswordCryptoConfig.SALT_PROPERTY, base64Salt, pBy);
		//SALT.setNotes("Salt used to access the keystore referenced in KEYSTOREPATH (when used in combination with CODE and KEY). Uses HMAC_SHA_256");
		configValues.add(SALT);

		return configValues;
	}

	private static final String LOGGER_NAME = PasswordCrypto.class.getPackage().getName() + ".PasswordCrypto";
}
