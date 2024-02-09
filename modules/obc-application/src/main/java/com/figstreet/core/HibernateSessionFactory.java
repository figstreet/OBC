package com.figstreet.core;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

public final class HibernateSessionFactory
{
	private static SessionFactory SESSION_FACTORY;
	static
	{
		try
		{
			Configuration configuration = new Configuration().configure(HibernateConfiguration.getConfigurationURL());
			SESSION_FACTORY = configuration.buildSessionFactory();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	private HibernateSessionFactory()
	{
		// empty ctor;
	}

	/* Note - it's up to the caller to close this Session */
	public static Session openSession() throws SQLException
	{
		if (SESSION_FACTORY == null)
			throw new SQLException(); // TODO
		return SESSION_FACTORY.openSession();
	}

	/* Note - it's up to the caller to close this Connection */
	public static Connection openConnection() throws SQLException
	{
		if (SESSION_FACTORY == null)
			throw new SQLException(); // TODO
		return SESSION_FACTORY.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class)
				.getConnection();
	}

	public static void closeFactory()
	{
		if (SESSION_FACTORY != null)
			SESSION_FACTORY.close();
	}
}
