package com.figstreet.core.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonSensitiveExclusionStrategy implements ExclusionStrategy
{

	@Override
	public boolean shouldSkipField(FieldAttributes pFieldAttributes)
	{
		if (pFieldAttributes.getAnnotation(Sensitive.class) != null)
			return true;
		return false;
	}

	@Override
	public boolean shouldSkipClass(Class<?> pClass)
	{
		return false;
	}

}
