package com.figstreet.core;

import java.lang.reflect.Constructor;

public class CtorUtil
{
	private static final String LOGGER_NAME = CtorUtil.class.getPackage().getName() + ".CtorUtil";

	public static <T> Constructor<T> getCtorDefault(Class<T> cl)
	{
		try
		{
			return cl.getConstructor();
		}
		catch (Exception e)
		{
			Logging.fatal(LOGGER_NAME, "getCtorDefault", "An error occurred while creating a default constructor", e);
		}

		return null;
	}

	public static <T> Constructor<T> getCtorString(Class<T> cl)
	{
		try
		{
			return cl.getConstructor(String.class);
		}
		catch (Exception e)
		{
			Logging.fatal(LOGGER_NAME, "getCtorString", "An error occurred while creating a String constructor", e);
		}

		return null;
	}

	public static <T> Constructor<T> getCtorInteger(Class<T> cl)
	{
		try
		{
			return cl.getConstructor(Integer.class);
		}
		catch (Exception e)
		{
			Logging.fatal(LOGGER_NAME, "getCtorInteger", "An error occurred while creating a Integer constructor", e);
		}

		return null;
	}

	public static <T> Constructor<T> getCtorLong(Class<T> cl)
	{
		try
		{
			return cl.getConstructor(Long.class);
		}
		catch (Exception e)
		{
			Logging.fatal(LOGGER_NAME, "getCtorLong", "An error occurred while creating a Long constructor", e);
		}

		return null;
	}

	public static <T> Constructor<T> getCodeConstantCtor(Class<T> cl)
	{
		try
		{
			return cl.getConstructor(String.class, String.class);
		}
		catch (Exception e)
		{
			Logging.fatal(LOGGER_NAME, "getCodeConstantCtor", "An error occurred while creating a String,String constructor", e);
		}

		return null;
	}

}
