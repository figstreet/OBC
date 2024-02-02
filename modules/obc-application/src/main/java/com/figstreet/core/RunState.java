package com.figstreet.core;

import java.util.HashMap;

public enum RunState
{
	STOPPED(0),
	RUNNING(1),
	STOPPING(2);

	private int fValue;

	private static HashMap<Integer, RunState> fAllValues = new HashMap<Integer, RunState>();
	static
	{
		for(RunState value : values())
			fAllValues.put(value.getValue(), value);
	}

	private RunState(int pValue)
	{
		this.fValue = pValue;
	}

	public static RunState newInstance(Integer pValue)
	{
		if((pValue == null))
			return null;

		RunState item = fAllValues.get(pValue);
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
