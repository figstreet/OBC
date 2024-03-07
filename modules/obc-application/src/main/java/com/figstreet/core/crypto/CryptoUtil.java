package com.figstreet.core.crypto;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class CryptoUtil
{
	public enum EncodingType
	{
		HEX,
		BASE64;

		public static EncodingType getInstance(String pValue)
		{
			if (pValue.equalsIgnoreCase("hex"))
				return HEX;
			else if (pValue.equalsIgnoreCase("base64"))
				return BASE64;
			return null;
		}

		public static String toString (EncodingType pValue)
		{
			if (pValue == null)
				return null;
			return pValue.toString();
		}
	}

	public static byte[] decodeStringToBytes(String pValue, EncodingType pEncoding) throws DecoderException
	{
		if (pValue == null)
			return null;

		byte[] decoded = null;
		if (EncodingType.BASE64.equals(pEncoding))
			decoded = decodeBase64StringToBytes(pValue);
		else if (EncodingType.HEX.equals(pEncoding))
			decoded = decodeHexStringToBytes(pValue);
		else
			throw new DecoderException("Encoding Type " + EncodingType.toString(pEncoding) + " not supported");

		return decoded;
	}

	public static byte[] decodeCharsToBytes(char[] pValue, EncodingType pEncoding) throws DecoderException
	{
		if (pValue == null)
			return null;

		byte[] decoded = null;
		if (EncodingType.BASE64.equals(pEncoding))
			decoded = decodeBase64CharsToBytes(pValue);
		else if (EncodingType.HEX.equals(pEncoding))
			decoded = decodeHexCharsToBytes(pValue);
		else
			throw new DecoderException("Encoding Type " + EncodingType.toString(pEncoding) + " not supported");

		return decoded;
	}

	public static String encodeBytesToString(byte[] pValue, EncodingType pEncoding) throws EncoderException
	{
		if (pValue == null)
			return null;

		String encoded = null;
		if (EncodingType.BASE64.equals(pEncoding))
			encoded = encodeToBase64String(pValue);
		else if (EncodingType.HEX.equals(pEncoding))
			encoded = encodeToHexString(pValue);
		else
			throw new EncoderException("Encoding Type " + EncodingType.toString(pEncoding) + " not supported");

		return encoded;
	}

	public static char[] encodeBytesToChars(byte[] pValue, EncodingType pEncoding) throws EncoderException
	{
		if (pValue == null)
			return null;

		char[] encoded = null;
		if (EncodingType.BASE64.equals(pEncoding))
			encoded = encodeToBase64Chars(pValue);
		else if (EncodingType.HEX.equals(pEncoding))
			encoded = encodeToHexChars(pValue);
		else
			throw new EncoderException("Encoding Type " + EncodingType.toString(pEncoding) + " not supported");

		return encoded;
	}

	public static byte[] decodeHexStringToBytes(String pValue) throws DecoderException
	{
		return Hex.decodeHex(pValue.toCharArray());
	}

	public static char[] decodeHexStringToChars(String pValue) throws DecoderException
	{
		return toChars(Hex.decodeHex(pValue.toCharArray()));
	}

	public static byte[] decodeHexCharsToBytes(char[] pValue) throws DecoderException
	{
		return Hex.decodeHex(pValue);
	}

	public static String encodeToHexString(byte[] pBytes)
	{
		return Hex.encodeHexString(pBytes);
	}

	public static char[] encodeToHexChars(byte[] pBytes)
	{
		return Hex.encodeHex(pBytes);
	}

	public static char[] decodeBase64StringToChars(String pValue)
	{
		return toChars(Base64.decodeBase64(pValue));
	}

	public static byte[] decodeBase64StringToBytes(String pValue)
	{
		return Base64.decodeBase64(pValue);
	}

	public static byte[] decodeBase64CharsToBytes(char[] pValue)
	{
		return Base64.decodeBase64(toBytes(pValue));
	}

	public static String encodeToBase64String(String pValue)
	{
		if (pValue == null)
			return null;
		return Base64.encodeBase64String(pValue.getBytes(StandardCharsets.UTF_8));
	}

	public static String encodeToBase64String(byte[] pBytes)
	{
		return Base64.encodeBase64String(pBytes);
	}

	public static char[] encodeToBase64Chars(byte[] pBytes)
	{
		return toChars(Base64.encodeBase64(pBytes));
	}

	public static byte[] toBytes(char[] pToConvert)
	{
		return toBytes(pToConvert, StandardCharsets.UTF_8);
	}

	public static char[] toChars(byte[] pToConvert)
	{
		return toChars(pToConvert, StandardCharsets.UTF_8);
	}

	public static byte[] toBytes(char[] pToConvert, Charset pCharset)
	{
		if (pCharset == null)
			pCharset = StandardCharsets.UTF_8;
		CharBuffer charBuffer = CharBuffer.wrap(pToConvert);
		ByteBuffer byteBuffer = Charset.forName(pCharset.toString()).encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		clearArray(byteBuffer.array());
		charBuffer = null;
		byteBuffer = null;
		return bytes;
	}

	public static char[] toChars(byte[] pToConvert, Charset pCharset)
	{
		if (pCharset == null)
			pCharset = StandardCharsets.UTF_8;
		ByteBuffer byteBuffer = ByteBuffer.wrap(pToConvert);
		CharBuffer charBuffer = Charset.forName(pCharset.toString()).decode(byteBuffer);
		char[] chars = Arrays.copyOfRange(charBuffer.array(), charBuffer.position(), charBuffer.limit());
		clearArray(charBuffer.array());
		charBuffer = null;
		byteBuffer = null;
		return chars;
	}

	public static void clearArray(char[] pToClear)
	{
		if (pToClear != null)
			Arrays.fill(pToClear, '\u0000'); // clear sensitive data
	}

	public static void clearArray(byte[] pToClear)
	{
		if (pToClear != null)
			Arrays.fill(pToClear, (byte) 0); // clear sensitive data
	}

	public static boolean equals(char[] a, char[] b)
	{
		if (a == null)
			return b == null;
		if (b == null)
			return false;

		if (a.length != b.length)
			return false;

		for (int i = 0; i < a.length; i++)
		{
			if (a[i] != b[i])
				return false;
		}
		return true;
	}
}
