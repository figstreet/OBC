package com.figstreet.core.crypto;

import com.figstreet.core.CompareUtil;
import com.figstreet.core.Logging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class SecureString implements Serializable
{
	private static final String LOGGER_NAME = SecureString.class.getPackage().getName() + ".SecureString";
	private char[] fValue;

	public SecureString(char[] pValue)
	{
		this.fValue = pValue;
	}

	public SecureString(String pValue)
	{
		if (pValue != null)
			this.fValue = pValue.toCharArray();
	}

	public SecureString(SecureString pValue)
	{
		this.fValue = SecureString.copyValue(pValue);
	}

	public boolean isEmpty()
	{
		return this.getLength() == 0;
	}

	public boolean isDigits()
	{
		if (this.getLength() == 0)
			return false;
		for (char c : this.fValue)
		{
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	public int getLength()
	{
		if (this.fValue == null)
			return 0;
		return this.fValue.length;
	}

	public char[] copyValue()
	{
		if (this.fValue == null)
			return null;
		return Arrays.copyOf(this.fValue, this.fValue.length);
	}

	public char[] copyValue(int pLength)
	{
		if (this.fValue == null || pLength == 0)
			return null;
		if (pLength >= this.fValue.length)
			return Arrays.copyOf(this.fValue, this.fValue.length);

		return Arrays.copyOf(this.fValue, pLength);
	}

	public void clearValue()
	{
		if (this.fValue != null && this.fValue.length > 0)
			CryptoUtil.clearArray(this.fValue);
		this.fValue = null;
	}

	public void toUpperCase()
	{
		if (this.fValue != null)
		{
			for(int i = 0; i < this.fValue.length; i++)
			{
				this.fValue[i] = Character.toUpperCase(this.fValue[i]);
			}
		}
	}

	@Override
	public String toString()
	{
		return toString(this.fValue);
	}

	public static String toString(char[] pValue)
	{
		if (pValue == null || pValue.length == 0)
			return null;
		return new String(pValue);
	}

	public static String toString(SecureString pValue)
	{
		if (pValue == null)
			return null;
		return toString(pValue.fValue);
	}

	public static char[] getValue(SecureString pSecureString)
	{
		if (pSecureString == null)
			return null;
		return pSecureString.fValue;
	}

	public static char[] copyValue(SecureString pSecureString)
	{
		if (pSecureString == null)
			return null;
		return pSecureString.copyValue();
	}

	public static char[] copyValue(SecureString pSecureString, int pLength)
	{
		if (pSecureString == null)
			return null;
		return pSecureString.copyValue(pLength);
	}

	/*
	 * Returns true only when all of pSecureString's characters are in pAllowedChars
	 */
	public static boolean containsChars(SecureString pSecureString, Set<Character> pAllowedChars, boolean pAllowEmptyString)
	{
		if (pSecureString == null)
			return pAllowEmptyString;
		if (pAllowedChars == null)
			return false;

		if (pSecureString.fValue == null || pSecureString.fValue.length == 0)
			return pAllowEmptyString;

		boolean contains = true;
		//Working in reverse - our data set has the invalid characters towards the end of the string
		for (int i = pSecureString.fValue.length - 1; contains && i >= 0; i--)
		{
			contains = pAllowedChars.contains(pSecureString.fValue[i]);
		}

		return contains;
	}

	public static boolean containsChars(String pString, Set<Character> pAllowedChars, boolean pAllowEmptyString)
	{
		if (pString == null)
			return pAllowEmptyString;
		if (pAllowedChars == null)
			return false;

		if (pString.length() == 0)
			return pAllowEmptyString;

		boolean contains = true;
		//Working in reverse - our data set has the invalid characters towards the end of the string
		for (int i = pString.length() - 1; contains && i >= 0; i--)
		{
			contains = pAllowedChars.contains(pString.charAt(i));
		}

		return contains;
	}

	public static boolean isSSNValid(SecureString pSsn)
	{
		if (pSsn == null)
			return false;
		if (pSsn.getLength() != 9)
			return false;
		if (!pSsn.isDigits())
			return false;
		return true;
	}

	public static boolean isSSNValid(char[] pSsn)
	{
		if (pSsn == null)
			return false;
		return isSSNValid(new SecureString(pSsn));
	}

	public static String encodeBase64(SecureString pSecureString)
	{
		if (pSecureString == null || pSecureString.isEmpty())
			return null;

		try
		{
			return CryptoUtil.encodeToBase64String(CryptoUtil.toBytes(pSecureString.fValue));
		}
		catch (Exception e)
		{
			String msg = String.format("Error encoding %s, returning null", pSecureString.toString());
			Logging.error(LOGGER_NAME, "encodeBase64", msg, e);
		}
		return null;
	}

	public static SecureString decodeBase64(String pValue)
	{
		if (CompareUtil.isEmpty(pValue))
			return null;

		try
		{
			char[] decoded = CryptoUtil.decodeBase64StringToChars(pValue);
			return new SecureString(decoded);
		}
		catch (Exception e)
		{
			String msg = String.format("Error decoding %s, returning null", pValue);
			Logging.error(LOGGER_NAME, "decodeBase64", msg, e);
		}
		return null;
	}

	@Override
	public boolean equals(Object pOther)
	{
		if (pOther == null)
			return false;
		if (pOther instanceof SecureString)
		{
			return equals(this, (SecureString) pOther, true);
		}
		else if (pOther instanceof String)
		{
			return CompareUtil.equals(this.fValue, ((String) pOther).toCharArray(), true);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		if (this.fValue == null)
			return 0;
		return Arrays.hashCode(this.fValue);
	}

	public ArrayList<SecureString> split(char pDelimiter)
	{
		ArrayList<SecureString> elements = new ArrayList<>();
		int startPos = 0;
		for (int i = 0; i < this.fValue.length; i++)
		{
			if (this.fValue[i] == pDelimiter)
			{
				SecureString element = new SecureString(Arrays.copyOfRange(this.fValue, startPos, i));
				elements.add(element);
				startPos = i+1;
			}
		}

		if (startPos < this.fValue.length)
		{
			SecureString element = new SecureString(Arrays.copyOfRange(this.fValue, startPos, this.fValue.length));
			elements.add(element);
		}

		return elements;
	}

	public static boolean equals(SecureString pLeft, SecureString pRight, boolean pCaseSensitive)
	{
		if (pLeft == null)
		{
			if (pRight == null)
				return true;
			return false;
		}
		else if (pRight == null)
			return false;

		return CompareUtil.equals(pLeft.fValue, pRight.fValue, pCaseSensitive);
	}
}
