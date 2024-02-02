package com.figstreet.core;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;

public final class Logging
{
	private static final String BEGIN = "BEGIN";
	private static boolean fInitialized;

	private Logging()
	{
	}

	public static synchronized void initialize(String pPath)
	{
		if (!fInitialized)
		{
			if (pPath != null)
			{
				DOMConfigurator.configure(pPath);

				fInitialized = true;
				info(Logging.class.getName(), "initialize", "Log settings initialized successfully");
			}
		}
	}

	public static void setupConsoleLogger(Class<?> pClass)
	{
		// creates pattern layout
		PatternLayout layout = new PatternLayout();
		String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
		layout.setConversionPattern(conversionPattern);

		// creates console appender
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(layout);
		consoleAppender.activateOptions();

		// configures the root logger
		org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		rootLogger.addAppender(consoleAppender);
		Thread.currentThread().setContextClassLoader(pClass.getClassLoader());
	}

	private static Logger getLogger(String pClass)
	{
		return Logger.getLogger(pClass);
	}

	public static String formatMessage(String pClass, String pMethod, String pMessage)
	{
		return String.format("%s.%s - %s", pClass.toString(), pMethod, pMessage);
	}

	public static void debug(String pClass, String pMethod, String pMessage)
	{
		getLogger(pClass).debug(formatMessage(pClass, pMethod, pMessage));
	}

	public static void debugf(String pClass, String pMethod, String pMessage, Object... pArgs)
	{
		getLogger(pClass).debug(formatMessage(pClass, pMethod, String.format(pMessage, pArgs)));
	}

	public static void debugBegin(String pClass, String pMethod)
	{
		getLogger(pClass).debug(formatMessage(pClass, pMethod, BEGIN));
	}

	public static void info(String pClass, String pMethod, String pMessage)
	{
		getLogger(pClass).info(formatMessage(pClass, pMethod, pMessage));
	}

	public static void info(String pClass, String pMethod, String pMessage, Exception pExc)
	{
		getLogger(pClass).info(formatMessage(pClass, pMethod, pMessage), pExc);
	}

	public static void infof(String pClass, String pMethod, String pMessage, Object... pArgs)
	{
		getLogger(pClass).info(formatMessage(pClass, pMethod, String.format(pMessage, pArgs)));
	}

	public static void warn(String pClass, String pMethod, String pMessage)
	{
		getLogger(pClass).warn(formatMessage(pClass, pMethod, pMessage));
	}

	public static void warnf(String pClass, String pMethod, String pMessage, Object... pArgs)
	{
		getLogger(pClass).warn(formatMessage(pClass, pMethod, String.format(pMessage, pArgs)));
	}

	public static void error(String pClass, String pMethod, String pMessage)
	{
		getLogger(pClass).error(formatMessage(pClass, pMethod, pMessage));
	}

	public static void errorf(String pClass, String pMethod, String pMessage, Object... pArgs)
	{
		getLogger(pClass).error(formatMessage(pClass, pMethod, String.format(pMessage, pArgs)));
	}

	public static void error(String pClass, String pMethod, String pMessage, Exception pExc)
	{
		getLogger(pClass).error(formatMessage(pClass, pMethod, pMessage), pExc);
	}

	public static void fatal(String pClass, String pMethod, String pMessage)
	{
		getLogger(pClass).fatal(formatMessage(pClass, pMethod, pMessage));
	}

	public static void fatal(String pClass, String pMethod, String pMessage, Exception pExc)
	{
		getLogger(pClass).fatal(formatMessage(pClass, pMethod, pMessage), pExc);
	}
}
