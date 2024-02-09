package com.figstreet.core;

public class FormatUtil
{
	public static String truncate(String pString, int pMaxLength)
	{
		if (pMaxLength < 0)
			pMaxLength = 0;
		if (pString == null)
			return pString;

		if (pString.length() <= pMaxLength)
			return pString;
		return pString.substring(0, pMaxLength);
	}

	public static String escapeSQL(String pValue)
	{
		if (pValue == null)
			return null;
		return pValue.replaceAll("[']", "''");
	}

	public static String formatName(String pPrefix, String pFirst, String pMiddle, String pLast, String pSuffix,
			boolean pLastNameFirst)
	{
		StringBuilder name = new StringBuilder();
		if (!pLastNameFirst)
		{
			if (!CompareUtil.isEmpty(pPrefix))
			{
				name.append(pPrefix);
				name.append(" ");
			}
			if (!CompareUtil.isEmpty(pFirst))
			{
				name.append(pFirst);
				name.append(" ");
			}
			if (!CompareUtil.isEmpty(pMiddle))
			{
				String middleInitial = pMiddle.substring(0, 1);
				name.append(middleInitial.toUpperCase());
				name.append(". ");
			}
			if (!CompareUtil.isEmpty(pLast))
			{
				name.append(pLast);
				name.append(" ");
			}
		}
		else
		{
			if (!CompareUtil.isEmpty(pLast))
			{
				name.append(pLast);
				name.append(", ");
			}
			if (!CompareUtil.isEmpty(pFirst))
			{
				name.append(pFirst);
				name.append(" ");
			}
			if (!CompareUtil.isEmpty(pMiddle))
			{
				String middleInitial = pMiddle.substring(0, 1);
				name.append(middleInitial.toUpperCase());
				name.append(". ");
			}
		}
		if (!CompareUtil.isEmpty(pSuffix))
		{
			if (name.charAt(name.length() - 1) != ',')
				name.setCharAt(name.length() - 1, ',');
			name.append(" ");
			name.append(pSuffix);
		}

		String nameValue = name.toString();
		if (!CompareUtil.isEmpty(nameValue))
			nameValue = nameValue.trim();
		return nameValue;
	}

	public static String formatLocation(String pCity, String pState, String pCountry, String pZip1, String pZip2)
	{
		StringBuilder location = new StringBuilder();
		if (!CompareUtil.isEmpty(pCity))
			location.append(pCity);
		if (!CompareUtil.isEmpty(pState))
		{
			if (location.length() > 0)
				location.append(", ");
			location.append(pState);
		}
		if (!CompareUtil.isEmpty(pCountry))
		{
			if (location.length() > 0)
				location.append(" ");
			location.append(pCountry);
		}
		if (!CompareUtil.isEmpty(pZip1))
		{
			if (location.length() > 0)
				location.append("  ");
			location.append(pZip1);
		}
		if (!CompareUtil.isEmpty(pZip2))
		{
			if (!CompareUtil.isEmpty(pZip1))
				location.append("-");
			else if (location.length() > 0)
				location.append(" -");
			location.append(pZip2);
		}
		return location.toString();
	}
}
