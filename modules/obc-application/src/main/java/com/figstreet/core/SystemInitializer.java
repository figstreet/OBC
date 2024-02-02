package com.figstreet.core;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.TimeZone;

import org.apache.log4j.LogManager;

import com.figstreet.data.codes.CodeCache;

public class SystemInitializer implements AutoCloseable
{
	private static final String LOGGER_NAME = SystemInitializer.class.getPackage().getName() + ".SystemInitializer";

	public static void initialize(String pLogConfigPath, String pDatabaseConfigPath)
	{
		try
		{
			TimeZone.setDefault(TimeZone.getTimeZone("EST"));
			Logging.initialize(pLogConfigPath);

			initializeDB(pDatabaseConfigPath);
			CodeCache.getThe().initialize();
			RefreshCacheManager.getThe().start();
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "init", "Error initializing application", e);
		}
	}

	public static void initializeDB(String pDatabaseConfigPath) throws MalformedURLException, SQLException
	{
		Logging.info(LOGGER_NAME, "initializeDatabase", "Connecting to database ...");

		File dbConfigFile = new File(pDatabaseConfigPath);
		HibernateConfiguration.initialize(dbConfigFile.toURI().toURL());

		Logging.info(LOGGER_NAME, "initializeDatabase", "Testing database ...");
		HibernateSessionFactory.openConnection();

		Logging.info(LOGGER_NAME, "initializeDatabase", "Database initialized");
	}

	// Shutdown methods
	public static void shutdown()
	{
		Logging.debugBegin(LOGGER_NAME, "shutdown");

		RefreshCacheManager.getThe().stopRunning();
		shutdownDb();
		LogManager.shutdown();
	}

	public static void shutdownDb()
	{
		Logging.debugBegin(LOGGER_NAME, "shutdownDb");
		HibernateSessionFactory.closeFactory();
	}

	@Override
	public void close() throws Exception
	{
		shutdown();
	}

}
