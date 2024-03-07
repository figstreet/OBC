package com.figstreet.core.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.figstreet.core.Logging;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class RSACrypto extends CryptoUtil
{
	private static final String KEY_FACTORY_ALGORITHM = "RSA";
	private static final String CIPHER_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

	private static volatile SecureRandom random = null;
	private static long lastRandom = 0;
	private static Object lock = new Object();

	private static final String LOGGER_NAME = RSACrypto.class.getPackage().getName() + ".RSACrypto";

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
					Logging.error(LOGGER_NAME, "nextBytes", "Generating default SecureRandom instead of SHA1PRNG.", e);

					random = new SecureRandom(); //Not as secure, but it's something
				}
			}
		}
		return random;
	}

	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException
	{
		return generateKeyPair(2048);
	}

	public static KeyPair generateKeyPair(int pBitSize) throws NoSuchAlgorithmException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_FACTORY_ALGORITHM);
		keyGen.initialize(pBitSize, getRandom());
		return keyGen.generateKeyPair();
	}

	public static RSAPublicKey getPublicKey(String pPublicKey, EncodingType pEncoding)
		throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException
	{
		RSAPublicKey publicKey = null;
		byte[] key = null;
		try
		{
			key = decodeStringToBytes(pPublicKey, pEncoding);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		}
		finally
		{
			clearArray(key);
		}
		return publicKey;
	}

	public static RSAPrivateKey getPrivateKey(String pPrivateKey, EncodingType pEncoding)
		throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException
	{
		RSAPrivateKey privateKey = null;
		byte[] key = null;
		try
		{
			key = decodeStringToBytes(pPrivateKey, pEncoding);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		}
		finally
		{
			clearArray(key);
		}
		return privateKey;
	}

	/* Encryption */

	public static String encryptString(String pPlainText, EncodingType pEncryptionEncoding, String pPublicKey,
		EncodingType pKeyEncodingType)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, InvalidKeySpecException, DecoderException, EncoderException, IOException
	{
		if (pPlainText == null || pPlainText.length() == 0)
			return pPlainText;

		RSAPublicKey publicKey = getPublicKey(pPublicKey, pKeyEncodingType);
		return encryptString(pPlainText, pEncryptionEncoding, publicKey);
	}

	public static String encryptString(String pPlainText, EncodingType pEncodingType, RSAPublicKey pPublicKey)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, EncoderException, IOException
	{
		if (pPlainText == null || pPlainText.length() == 0)
			return pPlainText;

		byte[] encrypted = null;
		try
		{
			encrypted = encrypt(pPlainText.getBytes(StandardCharsets.UTF_8), pPublicKey);
			return encodeBytesToString(encrypted, pEncodingType);
		}
		finally
		{
			clearArray(encrypted);
			encrypted = null;
		}
	}

	public static char[] encryptChars(char[] pPlainText, EncodingType pEncodingType, RSAPublicKey pPublicKey)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, EncoderException, IOException
	{
		if (pPlainText == null || pPlainText.length == 0)
			return pPlainText;

		byte[] encrypted = null;
		try
		{
			encrypted = encrypt(toBytes(pPlainText, StandardCharsets.UTF_8), pPublicKey);
			return encodeBytesToChars(encrypted, pEncodingType);
		}
		finally
		{
			clearArray(encrypted);
			encrypted = null;
		}
	}

	public static byte[] encrypt(byte[] pToEncrypt, RSAPublicKey pPublicKey) throws BadPaddingException,
		IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException
	{
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, pPublicKey);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(pToEncrypt.length);
		try
		{
			int maxEncryptSize = (pPublicKey.getModulus().bitLength() / 8) - 66; //OAEP_MGF1_SHA256_OVERHEAD
			int offset = 0;
			while (offset < pToEncrypt.length)
			{
				int encryptSize = Math.min(maxEncryptSize, pToEncrypt.length - offset);
				byte[] partial = cipher.doFinal(pToEncrypt, offset, encryptSize);
				if (partial != null)
					outputStream.write(partial);
				offset += encryptSize;
			}
			return outputStream.toByteArray();
		}
		finally
		{
			outputStream.close();
		}
	}

	/* Decryption */

	public static String decryptString(String pCipherText, EncodingType pCipherEncoding, RSAPrivateKey pKey)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, DecoderException, IOException
	{
		if (pCipherText == null || pCipherText.isEmpty())
			return pCipherText;

		byte[] toDecrypt = decodeStringToBytes(pCipherText, pCipherEncoding);
		byte[] decrypted = null;
		try
		{
			decrypted = decrypt(toDecrypt, 0, toDecrypt.length, pKey);
			return new String(decrypted, StandardCharsets.UTF_8);
		}
		finally
		{
			clearArray(decrypted);
			decrypted = null;
			clearArray(toDecrypt);
			toDecrypt = null;
		}
	}

	public static char[] decryptChars(char[] pCipherText, EncodingType pCipherEncoding, RSAPrivateKey pKey)
		throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
		BadPaddingException, DecoderException, IOException
	{
		if (pCipherText == null || pCipherText.length == 0)
			return pCipherText;

		byte[] toDecrypt = decodeCharsToBytes(pCipherText, pCipherEncoding);
		byte[] decrypted = null;
		try
		{
			decrypted = decrypt(toDecrypt, 0, toDecrypt.length, pKey);
			return toChars(decrypted);
		}
		finally
		{
			clearArray(decrypted);
			decrypted = null;
			clearArray(toDecrypt);
			toDecrypt = null;
		}
	}

	public static byte[] decrypt(byte[] pToDecrypt, int pInputOffset, int pInputLength, RSAPrivateKey pPrivateKey)
		throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
		IllegalBlockSizeException, IOException
	{
		if (pToDecrypt == null || pToDecrypt.length == 0)
			return pToDecrypt;

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, pPrivateKey);

		SecureByteArrayOutputStream outputStream = new SecureByteArrayOutputStream();
		try
		{
			int maxDecryptSize = (pPrivateKey.getModulus().bitLength() / 8);
			int processed = 0;
			while (processed < pInputLength)
			{
				int decryptSize = Math.min(maxDecryptSize, pInputLength - pInputOffset);
				byte[] partial = cipher.doFinal(pToDecrypt, pInputOffset, decryptSize);
				if (partial != null)
					outputStream.write(partial);

				pInputOffset += decryptSize;
				processed += decryptSize;
			}
			return outputStream.toByteArray();
		}
		finally
		{
			outputStream.close();
		}
	}

	public static void main(String[] args) throws Exception
	{
		KeyPair secureKeys = generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) secureKeys.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) secureKeys.getPrivate();

		System.out.println("Public Key["
			+ encodeBytesToString(publicKey.getEncoded(), EncodingType.BASE64) + "]");
		System.out.println("Private Key["
			+ encodeBytesToString(privateKey.getEncoded(), EncodingType.BASE64) + "]");

		String plainText = "  This is a secret �.    ";
		testString(plainText, publicKey, privateKey);

		plainText = "";
		testString(plainText, publicKey, privateKey);

		plainText = null;
		testString(plainText, publicKey, privateKey);

		plainText = "This test is exactly 256 characters long 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234";
		testString(plainText, publicKey, privateKey);

		plainText = "This test is exactly 512 characters long 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234This test is exactly 512 characters long 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234";
		testString(plainText, publicKey, privateKey);

		plainText = "This test is exactly 513 characters long 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234This test is exactly 512 characters long 012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345";
		testString(plainText, publicKey, privateKey);
	}

	public static void testString(String plainText, RSAPublicKey publicKey, RSAPrivateKey privateKey) throws Exception
	{
		System.out.println("Text[" + plainText + "]");

		String encryptedHex = encryptString(plainText, EncodingType.HEX, publicKey);
		System.out.println("Hex[" + encryptedHex + "]");
		String decryptedHex = decryptString(encryptedHex, EncodingType.HEX, privateKey);
		System.out.println("Hex[" + decryptedHex + "]");

		if (plainText != null)
		{
			if (!plainText.equals(decryptedHex))
				System.err.println("HEX results differ!");
		}

		String encryptedB64 = encryptString(plainText, EncodingType.BASE64, publicKey);
		System.out.println("B64[" + encryptedB64 + "]");
		String decryptedB64 = decryptString(encryptedB64, EncodingType.BASE64, privateKey);
		System.out.println("B64[" + decryptedB64 + "]");

		if (plainText != null)
		{
			if (!plainText.equals(decryptedB64))
				System.err.println("B64 results differ!");
		}

		char[] plainChars = null;
		if (plainText != null)
			plainChars = plainText.toCharArray();

		String outputValue = null;
		if (plainChars != null)
			outputValue = new String(plainChars);

		System.out.println("Char[" + outputValue + "]");

		char[] encryptedHexArray = encryptChars(plainChars, EncodingType.HEX, publicKey);
		outputValue = null;
		if (encryptedHexArray != null)
			outputValue = new String(encryptedHexArray);
		System.out.println("Hex[" + outputValue + "]");

		char[] decryptedHexArray = decryptChars(encryptedHexArray, EncodingType.HEX, privateKey);
		outputValue = null;
		if (decryptedHexArray != null)
			outputValue = new String(decryptedHexArray);
		System.out.println("Hex[" + outputValue + "]");

		if (!equals(plainChars, decryptedHexArray))
			System.err.println("Hex char[] results differ!");

		char[] encryptedB64Array = encryptChars(plainChars, EncodingType.BASE64, publicKey);
		outputValue = null;
		if (encryptedB64Array != null)
			outputValue = new String(encryptedB64Array);
		System.out.println("B64[" + outputValue + "]");

		char[] decryptedB64Array = decryptChars(encryptedB64Array, EncodingType.BASE64, privateKey);
		outputValue = null;
		if (decryptedB64Array != null)
			outputValue = new String(decryptedB64Array);
		System.out.println("B64[" + outputValue + "]");

		if (!equals(plainChars, decryptedB64Array))
			System.err.println("B64 char[] results differ!");
	}
}
