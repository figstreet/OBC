package com.figstreet.core;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateTransaction
{
	private static final String LOGGER_NAME = HibernateTransaction.class.getPackage().getName()
			+ ".HibernateTransaction";

	private static final Map<Long, HibernateTransaction> fOpenDatabaseTransMap = Collections
			.synchronizedMap(new HashMap<>());

	public static final int RANDOM_STRING_LENGTH = 26;

	private Session fSession;
	private Transaction fTransaction;
	private Boolean fDoCommit;
	private String fIdentity;
	private int fCallers;

	private static volatile SecureRandom random = null;
	private static long lastRandom = 0;
	private static final Object lock = new Object();

	private static SecureRandom getRandom()
	{
		long currentMS = System.currentTimeMillis();
		long diff = currentMS - lastRandom;
		if (random == null || diff > 21600000 || diff <= 0) // regenerate after 6 hours
		{
			synchronized (lock)
			{
				lastRandom = currentMS;
				try
				{
					SecureRandom temp = SecureRandom.getInstance("SHA1PRNG", "SUN");
					temp.nextBytes(new byte[8]); // ensure seeding
					random = temp;
				}
				catch (Exception e)
				{
					Logging.error(LOGGER_NAME, "getRandom", "Generating default SecureRandom instead of SHA1PRNG.", e);
					random = new SecureRandom(); // Not as secure, but it's something
				}
			}
		}
		return random;
	}

	public static String randomString(int pLength)
	{
		if (pLength <= 0 || pLength > 130)
			throw new IllegalArgumentException("Length must be > 0 and <= 130");

		int loops = pLength / RANDOM_STRING_LENGTH + (pLength % RANDOM_STRING_LENGTH == 0 ? 0 : 1);
		String retVal = "";
		while (loops > 0)
		{
			retVal += new BigInteger(130, getRandom()).toString(32);
			loops--;
		}
		if (retVal.length() > pLength)
			retVal = retVal.substring(0, pLength);

		return retVal;
	}

	public static HibernateTransaction open() throws IllegalArgumentException, SQLException
	{
		Long threadID = Thread.currentThread().getId();
		HibernateTransaction trans = fOpenDatabaseTransMap.get(threadID);
		if (trans == null)
		{
			trans = new HibernateTransaction(HibernateSessionFactory.openSession());
			trans.fCallers = 1;
			fOpenDatabaseTransMap.put(threadID, trans);
			Logging.debugf(LOGGER_NAME, "open", "Open Trans: %s", trans.fIdentity);
		}
		else
		{
			if (Boolean.TRUE.equals(trans.fDoCommit)) //Only reset if True, not for False (indicates rollback)
				trans.fDoCommit = null;
			trans.fCallers++;
			Logging.debugf(LOGGER_NAME, "open", "Re-using Trans: %s", trans.fIdentity);
		}
		return trans;
	}

	private HibernateTransaction(Session pSession) throws IllegalArgumentException, SQLException
	{
		if (pSession == null || !pSession.isOpen())
			throw new IllegalArgumentException("Session is not open");

		this.fSession = pSession;
		try
		{
			this.fTransaction = this.fSession.beginTransaction();
		}
		catch (HibernateException he)
		{
			throw new SQLException(he);
		}
		this.fIdentity = Thread.currentThread().getId() + "_" + System.currentTimeMillis() + "_"
				+ randomString(RANDOM_STRING_LENGTH);
	}

	public Session getSession()
	{
		return this.fSession;
	}

	public EntityManager getEntityManager()
	{
		return this.fSession.getEntityManagerFactory().createEntityManager();
	}

	public void commit()
	{
		if (this.fDoCommit == null)
			this.fDoCommit = Boolean.TRUE;
	}

	public void rollback()
	{
		this.fDoCommit = Boolean.FALSE;
	}

	public void closeNoException()
	{
		try
		{
			this.close();
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "closeNoException", "Error closing transaction", e);
		}
	}

	public void close() throws SQLException
	{
		this.fCallers--;
		if (this.fCallers <= 0)
		{
			try
			{
				fOpenDatabaseTransMap.remove(Thread.currentThread().getId());
				if (this.fDoCommit != null && this.fDoCommit)
					this.fTransaction.commit();
				else
					this.fTransaction.rollback();
			}
			catch (HibernateException he)
			{
				throw new SQLException(he);
			}
			finally
			{
				this.fSession.close();
			}
			Logging.debug(LOGGER_NAME, "close", "Close Trans: " + this.fIdentity);
		}
	}

}
