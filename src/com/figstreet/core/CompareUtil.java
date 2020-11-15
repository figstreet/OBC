package com.figstreet.core;

import java.sql.Date;

public class CompareUtil
{
	public static boolean isEmpty(String pValue)
	{
		return pValue == null || pValue.trim().length() == 0;
	}

	public static boolean equalsString(String pVal1, String pVal2, boolean pIgnoreCase)
	{
		if (pVal1 == null)
		{
			if (pVal2 == null)
				return true;
			return false;
		}
		if (pVal2 == null)
			return false;

		if (pIgnoreCase)
			return pVal1.toLowerCase().equals(pVal2.toLowerCase());
		return pVal1.equals(pVal2);
	}

	public static boolean equals(Object pComp1, Object pComp2)
	{
		if (pComp1 == null)
		{
			if (pComp2 == null)
				return true;
			return false;
		}
		return pComp1.equals(pComp2);
	}

	public static int compareString(String pLeft, String pRight, boolean pIgnoreCase)
	{
		int retVal = 0;
		if (pLeft == null)
		{
			if (pRight == null)
				retVal = 0;
			else
				retVal = -1;
		}
		else if (pRight == null)
			retVal = 1;

		if (retVal == 0 && pLeft != null)
		{
			if (pIgnoreCase)
				retVal = pLeft.compareToIgnoreCase(pRight);
			else
				retVal = pLeft.compareTo(pRight);
		}
		return retVal;
	}

	public static Integer getAsInteger(String pValue)
	{
		Integer number = null;
		if (!isEmpty(pValue))
		{
			try
			{
				number = new Integer(pValue);
			}
			catch (NumberFormatException nfe)
			{
				// nothing
			}
		}
		return number;
	}

	public static int compareDateNullsGreater(Date pLHS, Date pRHS)
	{
		if (pLHS == null)
			return (pRHS == null) ? 0 : 1;

		if (pRHS == null)
			return -1;

		return pLHS.compareTo(pRHS);
	}

	public static int compareDate(Date pLHS, Date pRHS)
	{
		if (pLHS == null)
			return (pRHS == null) ? 0 : -1;

		if (pRHS == null)
			return 1;

		return pLHS.compareTo(pRHS);
	}

	public static int compareDate(Date pLHS, Date pRHS, boolean pDescending)
	{
		if (pDescending)
			return compareDate(pRHS, pLHS);
		return compareDate(pLHS, pRHS);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compareDataID(DataID pLHS, DataID pRHS)
	{
		if (pLHS == null)
			return (pRHS == null) ? 0 : -1;

		if (pRHS == null)
			return 1;

		return pLHS.compareTo(pRHS);
	}

	@SuppressWarnings("rawtypes")
	public static int compareDataID(DataID pLHS, DataID pRHS, boolean pDescending)
	{
		if (pDescending)
			return compareDataID(pRHS, pLHS);
		return compareDataID(pLHS, pRHS);
	}

	public static <T extends Comparable<?>> int compare(Comparable<T> pLHS, T pRHS)
	{
		if (pLHS == null)
			return (pRHS == null) ? 0 : -1;

		if (pRHS == null)
			return 1;

		return pLHS.compareTo(pRHS);
	}

	public static <T extends Comparable<?>> int compare(Comparable<T> pLHS, T pRHS, boolean pDescending)
	{
		return compare(pLHS, pRHS) * (pDescending ? -1 : 1);
	}

	public static int compareInteger(Integer pLHS, Integer pRHS)
	{
		if (pLHS == null)
			return (pRHS == null) ? 0 : -1;

		if (pRHS == null)
			return 1;

		return pLHS.compareTo(pRHS);
	}

	public static int compareShort(Short pLHS, Short pRHS)
	{
		if (pLHS == null)
			return (pRHS == null) ? 0 : -1;

		if (pRHS == null)
			return 1;

		return pLHS.compareTo(pRHS);
	}

	public static int compareLong(Long pLHS, Long pRHS)
	{
		if (pLHS == null)
			return (pRHS == null) ? 0 : -1;

		if (pRHS == null)
			return 1;

		return pLHS.compareTo(pRHS);
	}
}
