package com.figstreet.core;

import java.util.HashMap;

public enum RunIntervalType
{
	POST_PROCESSING_INTERVAL(0),   //apply the interval after processing as "sleep" time
	TIMER_INTERVAL(1);   //apply the interval to the last run time so the next process occurs on a schedule

	private int fValue;

	private static HashMap<Integer, RunIntervalType> fAllValues = new HashMap<Integer, RunIntervalType>();
	static
	{
		for(RunIntervalType value : values())
			fAllValues.put(value.getValue(), value);
	}

	private RunIntervalType(int pValue)
	{
		this.fValue = pValue;
	}

	public static RunIntervalType newInstance(Integer pValue)
	{
		if((pValue == null))
			return null;

		RunIntervalType item = fAllValues.get(pValue);
		if(item != null)
			return item;

		return null;
	}

	@Override
	public String toString()
	{
		return Integer.toString(this.fValue);
	}

	public int getValue()
	{
		return this.fValue;
	}
}
