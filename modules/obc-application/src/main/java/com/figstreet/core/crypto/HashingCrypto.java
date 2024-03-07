package com.figstreet.core.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;

import javax.crypto.Mac;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.figstreet.core.Logging;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import org.bouncycastle.jcajce.provider.digest.SHA3;

public class HashingCrypto extends CryptoUtil
{
	public static final String MESSAGE_DIGEST_SHA_256 = "SHA-256";
	public static final EncodingType USER_HASH_ENCODING = EncodingType.HEX;
	public static final EncodingType PARTY_HASH_ENCODING = EncodingType.BASE64;

	private static volatile SecureRandom random = null;
	private static long lastRandom = 0;
	private static Object lock = new Object();

	private static void nextBytes(byte[] pBytes)
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
		random.nextBytes(pBytes);
	}

	public static String hashUserPassword(String pToHash, String pSalt)
		throws IllegalArgumentException, NoSuchAlgorithmException, DecoderException
	{
		return hashUserPassword(pToHash, pSalt, USER_HASH_ENCODING)[1];
	}

	//Returns two strings - the salt and the hashed password
	public static String[] hashUserPassword(String pToHash)
		throws IllegalArgumentException, NoSuchAlgorithmException, DecoderException
	{
		return hashUserPassword(pToHash, null, USER_HASH_ENCODING);
	}

	//Returns two strings - the salt and the hashed password
	public static String[] hashUserPassword(String pToHash, String pSalt, EncodingType pEncoding)
		throws IllegalArgumentException, NoSuchAlgorithmException, DecoderException
	{
		if (CompareUtil.isEmpty(pToHash))
			throw new IllegalArgumentException("Value to hash (i.e. password) is required");

		String[] retVal = new String[2];

		byte[] saltBytes;
		if (CompareUtil.isEmpty(pSalt))
		{
			saltBytes = new byte[32];
			nextBytes(saltBytes);
		}
		else
		{
			if (EncodingType.BASE64.equals(pEncoding))
				saltBytes = decodeBase64StringToBytes(pSalt);
			else
				saltBytes = decodeHexStringToBytes(pSalt);
		}

		if (EncodingType.BASE64.equals(pEncoding))
			retVal[0] = encodeToBase64String(saltBytes);
		else
			retVal[0] = encodeToHexString(saltBytes);

		byte[] hashBytes;
		if (EncodingType.BASE64.equals(pEncoding))
			hashBytes = decodeBase64StringToBytes(pToHash);
		else
			hashBytes = decodeHexStringToBytes(pToHash);

		MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_SHA_256);
		byte[] bytes = ArrayUtils.addAll(saltBytes, hashBytes);
		byte[] hashed = md.digest(bytes);

		if (EncodingType.BASE64.equals(pEncoding))
			retVal[1] = encodeToBase64String(hashed);
		else
			retVal[1] = encodeToHexString(hashed);

		clearArray(saltBytes);
		clearArray(hashBytes);
		clearArray(bytes);
		clearArray(hashed);

		return retVal;
	}

	//Returns two strings - the salt and the hashed password
	public static String[] doublyHashUserPassword(String pPlainText)
		throws IllegalArgumentException, NoSuchAlgorithmException
	{
		return doublyHashUserPassword(pPlainText, USER_HASH_ENCODING);
	}

	//Returns two strings - the salt and the hashed password
	public static String[] doublyHashUserPassword(String pPlainText, EncodingType pEncoding)
		throws IllegalArgumentException, NoSuchAlgorithmException
	{
		if (CompareUtil.isEmpty(pPlainText))
			throw new IllegalArgumentException("Value to hash (i.e. password) is required");

		String[] retVal = new String[2];

		MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_SHA_256);
		byte[] preHash = md.digest(pPlainText.getBytes(StandardCharsets.UTF_8));

		byte[] saltBytes = new byte[32];
		nextBytes(saltBytes);

		if (EncodingType.BASE64.equals(pEncoding))
			retVal[0] = encodeToBase64String(saltBytes);
		else
			retVal[0] = encodeToHexString(saltBytes);

		byte[] bytes = ArrayUtils.addAll(saltBytes, preHash);
		byte[] hashed = md.digest(bytes);

		if (EncodingType.BASE64.equals(pEncoding))
			retVal[1] = encodeToBase64String(hashed);
		else
			retVal[1] = encodeToHexString(hashed);

		clearArray(saltBytes);
		clearArray(preHash);
		clearArray(bytes);
		clearArray(hashed);

		return retVal;
	}

	public static ArrayList<byte[]> generateSaltKeyAndHash(char[] pPassword)
	{
		return generateSaltKeyAndHash(HmacAlgorithms.HMAC_SHA_256, pPassword);
	}

	//char[] salt and key are Hex encoded strings; pPassword is plain text
	public static byte[] hashPassword(char[] pSalt, char[] pKey, char[] pPassword) throws DecoderException
	{
		return hashPassword(HmacAlgorithms.HMAC_SHA_256, USER_HASH_ENCODING, pSalt, pKey, pPassword);
	}

	//Returns an ArrayList where the first element is the Salt, second is the Key and third is the Hash
	public static ArrayList<byte[]> generateSaltKeyAndHash(HmacAlgorithms pAlgorithm, char[] pPassword)
	{
		ArrayList<byte[]> returnValues = new ArrayList<byte[]>(3);
		byte[] saltBytes = new byte[32];
		nextBytes(saltBytes);
		returnValues.add(saltBytes);

		byte[] keyBytes = new byte[32];
		nextBytes(keyBytes);
		returnValues.add(keyBytes);

		byte[] toHash = ArrayUtils.addAll(saltBytes, toBytes(pPassword));

		Mac hmac = HmacUtils.getInitializedMac(pAlgorithm, keyBytes);
		returnValues.add(hmac.doFinal(toHash));
		return returnValues;
	}

	//char[] salt and key are encoded strings; pPassword is plain text
	public static byte[] hashPassword(HmacAlgorithms pAlgorithm, EncodingType pEncodingType, char[] pSalt, char[] pKey,
		char[] pPassword) throws DecoderException
	{
		byte[] keyBytes;
		if (EncodingType.BASE64.equals(pEncodingType))
			keyBytes = decodeBase64CharsToBytes(pKey);
		else
			keyBytes = decodeHexCharsToBytes(pKey);

		Mac hmac = HmacUtils.getInitializedMac(pAlgorithm, keyBytes);
		clearArray(keyBytes);

		byte[] saltBytes;
		if (EncodingType.BASE64.equals(pEncodingType))
			saltBytes = decodeBase64CharsToBytes(pSalt);
		else
			saltBytes = decodeHexCharsToBytes(pSalt);

		byte[] toHash = ArrayUtils.addAll(saltBytes, toBytes(pPassword));
		clearArray(saltBytes);

		byte[] hashed = hmac.doFinal(toHash);
		clearArray(toHash);

		return hashed;
	}

	public static String fileHashSHA256(File pFile, EncodingType pEncodingType)
		throws NoSuchAlgorithmException, IOException
	{
		return fileHash(MESSAGE_DIGEST_SHA_256, pFile, pEncodingType);
	}

	public static String fileHash(String pMessageDigestAlgorithm, File pFile, EncodingType pEncodingType)
		throws NoSuchAlgorithmException, IOException
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(pFile);
			MessageDigest digest = MessageDigest.getInstance(pMessageDigestAlgorithm);
			int bufferLength = 32768;
			byte[] buffer = new byte[bufferLength];
			int read = fis.read(buffer);
			while (read >= 0)
			{
				if (read > 0)
				{
					if (read < bufferLength)
					{
						byte[] smaller = new byte[read];
						System.arraycopy(buffer, 0, smaller, 0, read);
						digest.update(smaller);
					}
					else
						digest.update(buffer);
				}
				read = fis.read(buffer);
			}

			byte[] hash = digest.digest();
			if (EncodingType.BASE64.equals(pEncodingType))
				return encodeToBase64String(hash);
			else if (EncodingType.HEX.equals(pEncodingType))
				return encodeToHexString(hash);

			return new String(hash, StandardCharsets.UTF_8);
		}
		finally
		{
			if (fis != null)
				fis.close();
		}
	}

	public static String sha3Hash(byte[] pInput, EncodingType pOutputEncoding)
	{
		return sha3Hash(pInput, 0, pInput.length, pOutputEncoding);
	}

	public static String sha3Hash(byte[] pInput, int pOffset, int pLength, EncodingType pOutputEncoding)
	{
		SHA3.DigestSHA3 sha3 = new SHA3.Digest256();
		sha3.update(pInput, pOffset, pLength);
		byte[] hashed = sha3.digest();
		try
		{
			if (EncodingType.BASE64.equals(pOutputEncoding))
				return encodeToBase64String(hashed);

			return encodeToHexString(hashed);
		}
		finally
		{
			clearArray(hashed);
		}
	}

	public static String sha3Hash(String pInput, Charset pCharset, EncodingType pOutputEncoding)
	{
		return sha3Hash(pInput.getBytes(pCharset), pOutputEncoding);
	}

	public static String sha3HashParty(SecureString pFirstName, SecureString pMiddleName, SecureString pLastName,
		SecureString pSsn, Date pDateOfBirth)
	{
		try
		{
			return sha3Hash(SecureString.getValue(pFirstName), SecureString.getValue(pMiddleName),
				SecureString.getValue(pLastName), SecureString.getValue(pSsn), pDateOfBirth);
		}
		catch (Exception e)
		{
			Logging.warn(LOGGER_NAME, "sha3HashParty", "Error calculating hash, returning null", e);
		}
		return null;
	}

	public static String sha3Hash(char[] pFirstName, char[] pMiddleName, char[] pLastName, char[] pSsn,
		Date pDateOfBirth) throws IllegalArgumentException
	{
		boolean ssnValid = SecureString.isSSNValid(pSsn);
		if (!ssnValid && pDateOfBirth == null)
			throw new IllegalArgumentException("Valid SSN or Date of Birth is required");

		char delim = '{';
		String dateFormat = DateUtil.DATE_FORMAT_ISO8601;
		int size = (pFirstName == null || pFirstName.length == 0 ? 1 : pFirstName.length) + 1
			+ (pMiddleName == null || pMiddleName.length == 0 ? 1 : pMiddleName.length) + 1
			+ (pLastName == null || pLastName.length == 0 ? 1 : pLastName.length)
			+ (ssnValid ? pSsn.length + 1 : 0)
			+ (pDateOfBirth != null ? dateFormat.length() + 1 : 0);

		char[] toHash = new char[size];
		try
		{
			int i = 0;
			if (pFirstName == null || pFirstName.length == 0)
				toHash[i++] = ' ';
			else
			{
				for (int j = 0; j < pFirstName.length; j++)
				{
					toHash[i++] = Character.toUpperCase(pFirstName[j]);
				}
			}
			toHash[i++] = delim;

			if (pMiddleName == null || pMiddleName.length == 0)
				toHash[i++] = ' ';
			else
			{
				for (int j = 0; j < pMiddleName.length; j++)
				{
					toHash[i++] = Character.toUpperCase(pMiddleName[j]);
				}
			}
			toHash[i++] = delim;

			if (pLastName == null || pLastName.length == 0)
				toHash[i++] = ' ';
			else
			{
				for (int j = 0; j < pLastName.length; j++)
				{
					toHash[i++] = Character.toUpperCase(pLastName[j]);
				}
			}

			//Only include SSN and delim in hash if SSN valid
			if (ssnValid)
			{
				toHash[i++] = delim;
				for (int j = 0; j < pSsn.length; j++)
				{
					toHash[i++] = pSsn[j];
				}
			}

			//Only include DoB and delim in hash if DoB provided
			if (pDateOfBirth != null)
			{
				toHash[i++] = delim;
				String dob = DateUtil.formatDate(pDateOfBirth, dateFormat);
				for (int j = 0; j < dob.length(); j++)
				{
					toHash[i++] = dob.charAt(j);
				}
			}

			//Changing the EncodingType would require updating records in the database (i.e SearchParty, Party, SearchResult, MultipleRecordParty)
			return sha3Hash(toBytes(toHash), PARTY_HASH_ENCODING);
		}
		finally
		{
			clearArray(toHash);
		}
	}

	public static String sha3HashName(SecureString pName)
	{
		return sha3HashName(pName, true);
	}

	public static String sha3HashName(SecureString pName, boolean pUpperCase)
	{
		if (pName == null || pName.isEmpty())
			return null;

		char[] value = SecureString.copyValue(pName);
		try
		{
			if (pUpperCase)
			{
				for (int i = 0; i < value.length; i++)
				{
					value[i] =  Character.toUpperCase(value[i]);
				}
			}

			//Changing the EncodingType would require updating records in the database (i.e Party)
			return sha3Hash(toBytes(value), PARTY_HASH_ENCODING);
		}
		finally
		{
			clearArray(value);
		}
	}

	public static String sha3HashName(String pName)
	{
		return sha3HashName(pName, true);
	}

	public static String sha3HashName(String pName, boolean pUpperCase)
	{
		if (pName == null || pName.isEmpty())
			return null;

		if (pUpperCase)
			pName = pName.toUpperCase();

		//Changing the EncodingType would require updating records in the database (i.e. Party)
		return sha3Hash(toBytes(pName.toCharArray()), PARTY_HASH_ENCODING);
	}

	private static final String LOGGER_NAME = HashingCrypto.class.getPackage().getName() + ".HashingCrypto";
}
