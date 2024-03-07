package com.figstreet.core.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;

import com.figstreet.core.ClientID;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DBConfig;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.digest.HmacAlgorithms;

public abstract class CryptoConfig extends DBConfig
{
	public static final String KEYSTOREPATH_PROPERTY = "KEYSTOREPATH";
	public static final String KEYSTORETYPE_PROPERTY = "KEYSTORETYPE";
	public static final String SALT_PROPERTY = "SALT";
	public static final String KEY_PROPERTY = "KEY";
	public static final String CODE_PROPERTY = "CODE";
	public static final String ENCODINGTYPE_PROPERTY = "ENCODINGTYPE";


	public CryptoConfig(String pConfigName, ClientID pClientID)
	{
		super(pConfigName, pClientID);
	}

	/**
	 * Path to the JAVA keystore on the local file system
	 */
	public String getKeyStorePath()
	{
		return this.getProperty(KEYSTOREPATH_PROPERTY);
	}

	public String findKeyStorePath()
	{
		return this.findProperty(KEYSTOREPATH_PROPERTY);
	}

	/**
	 * JAVA keystore type - usually jceks
	 */
	public String getKeyStoreType()
	{
		return this.getProperty(KEYSTORETYPE_PROPERTY);
	}

	public String findKeyStoreType()
	{
		return this.findProperty(KEYSTORETYPE_PROPERTY);
	}

	/**
	 * Keystore access is secured by a password.
	 * This password is generated as a hash using CryptoApp's genKeys function.
	 * This is the salt used when generating that hashed password.
	 * The salt is stored using the ENCODINGTYPE encoding.
	 */
	public String getSalt()
	{
		return this.getProperty(SALT_PROPERTY);
	}

	public String findSalt()
	{
		return this.findProperty(SALT_PROPERTY);
	}

	/**
	 * Keystore access is secured by a password.
	 * This password is generated as a hash using CryptoApp's genKeys function.
	 * This is the key used when generating that hashed password.
	 * The key is stored using the ENCODINGTYPE encoding.
	 */
	public String getKey()
	{
		return this.getProperty(KEY_PROPERTY);
	}

	public String findKey()
	{
		return this.findProperty(KEY_PROPERTY);
	}

	/**
	 * Keystore access is secured by a password.
	 * This password is generated using CryptoApp's genKeys function.
	 * This is the encoded hash's value.
	 * It's stored using the ENCODINGTYPE encoding.
	 */
	public String getCode()
	{
		return this.getProperty(CODE_PROPERTY);
	}

	public String findCode()
	{
		return this.findProperty(CODE_PROPERTY);
	}

	/**
	 * Keystore access is secured by a password
	 * This password is generated using CryptoApp's genKeys function.
	 * This is the encoding type used for SALT, KEY and CODE
	 */
	public CryptoUtil.EncodingType getEncodingType()
	{
		return CryptoUtil.EncodingType.getInstance(this.getProperty(ENCODINGTYPE_PROPERTY));
	}

	public CryptoUtil.EncodingType findEncodingType()
	{
		return CryptoUtil.EncodingType.getInstance(this.findProperty(ENCODINGTYPE_PROPERTY));
	}

	public boolean isConfigExists()
	{
		if (CompareUtil.isEmpty(this.findKeyStorePath()) || CompareUtil.isEmpty(this.findKeyStoreType())
			|| this.findEncodingType() == null || CompareUtil.isEmpty(this.findKey())
			|| CompareUtil.isEmpty(this.findSalt()) || CompareUtil.isEmpty(this.findCode()))
			return false;

		return true;
	}

	protected char[] getKeyStorePassword() throws DecoderException
	{
		char[] salt = null;
		char[] key = null;
		char[] password = null;
		byte[] hash = null;
		try
		{
			salt = this.getSalt().toCharArray();
			key = this.getKey().toCharArray();

			CryptoUtil.EncodingType encodingType = this.getEncodingType();
			if (CryptoUtil.EncodingType.BASE64.equals(encodingType))
				password = CryptoUtil.decodeBase64StringToChars(this.getCode());
			else
				password = CryptoUtil.decodeHexStringToChars(this.getCode());

			hash = HashingCrypto.hashPassword(HmacAlgorithms.HMAC_SHA_256, encodingType, salt, key, password);

			if (CryptoUtil.EncodingType.BASE64.equals(encodingType))
				return CryptoUtil.encodeToBase64Chars(hash);

			return CryptoUtil.encodeToHexChars(hash);
		}
		finally
		{
			CryptoUtil.clearArray(salt);
			salt = null;
			CryptoUtil.clearArray(key);
			key = null;
			CryptoUtil.clearArray(password);
			password = null;
			CryptoUtil.clearArray(hash);
			hash = null;
		}
	}

	public static String getKeyStoreType(Path pPath) throws IOException
	{
		String type = null;

		String pkcs12 = "PKCS12";
		InputStream pkcs12IS = null;
		try
		{
			KeyStore keyStore = KeyStore.getInstance(pkcs12);
			pkcs12IS = Files.newInputStream(pPath, StandardOpenOption.READ);
			keyStore.load(pkcs12IS, null);
			type = pkcs12;
		}
		catch (Exception e)
		{
			type = null;
		}
		finally
		{
			if (pkcs12IS != null)
				pkcs12IS.close();
		}
		if (type != null)
			return type;

		String jks = "JKS";
		InputStream jksIS = null;
		try
		{
			KeyStore keyStore = KeyStore.getInstance(jks);
			jksIS = Files.newInputStream(pPath, StandardOpenOption.READ);
			keyStore.load(jksIS, null);
			type = jks;
		}
		catch (Exception e)
		{
			type = null;
		}
		finally
		{
			if (jksIS != null)
				jksIS.close();
		}
		if (type != null)
			return type;

		String jceks = "JCEKS";
		InputStream jceksIS = null;
		try
		{
			KeyStore keyStore = KeyStore.getInstance(jceks);
			jceksIS = Files.newInputStream(pPath, StandardOpenOption.READ);
			keyStore.load(jceksIS, null);
			type = jceks;
		}
		catch (Exception e)
		{
			type = null;
		}
		finally
		{
			if (jceksIS != null)
				jceksIS.close();
		}
		if (type != null)
			return type;

		return type;
	}
}
