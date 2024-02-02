package com.figstreet.core;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.search.SearchException;

import com.figstreet.data.configvalue.ConfigValue;
import com.figstreet.data.configvalue.ConfigValueList;
import com.figstreet.data.users.UsersID;

public class RefreshCacheManager extends Thread
{
	private static final String CONFIG_NAME = "CACHETIMESTAMPS";
	public static final String LOGGER_NAME = RefreshCacheManager.class.getPackage().toString() + ".RefreshCacheManager";

	private static final long SLEEP_MS = 2000L;
	private static final long RUN_INTERVAL_MS = 1000L * 60L * 15L;

	//Initialization-on-demand holder
	private static class Holder
	{
		static volatile RefreshCacheManager fManager = new RefreshCacheManager();
	}

	public static RefreshCacheManager getThe()
	{
		return Holder.fManager;
	}

	private volatile Map<String, RefreshCache> fCaches = new ConcurrentHashMap<>(200);
	private boolean fStopRunning = false;
	private boolean fIsRunning = false;
	private HashMap<String, Long> fLastReadTimeStamps = new HashMap<String, Long>();
	private JobTimer fRefreshJobs = new JobTimer("RefreshCacheManager", false);

	private RefreshCacheManager()
	{
		super.setName(this.getClass().getSimpleName());
		this.loadTimeStampsFromDB();
		this.fRefreshJobs.start();
	}

	public void forceRefreshes() throws SQLException, SearchException, IOException
	{
		this.doWork(null, true, UsersID.ADMIN);
	}

	public void addCache(RefreshCache pCache)
	{
		try
		{
			this.doWork(pCache, false, UsersID.ADMIN);
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "addCache", "Unable to add cache " + pCache.getCacheName(), e);
		}
	}

	protected Long findTimestamp(String pName, boolean pCheckDb) throws SQLException, SearchException
	{
		if (pCheckDb || !this.fLastReadTimeStamps.containsKey(pName))
		{
			ConfigValue value = ConfigValue.findByName(CONFIG_NAME, pName);
			if (value != null)
			{
				try
				{
					return Timestamp.valueOf(value.getPropertyValue()).getTime();
				}
				catch (Exception e)
				{
					Logging.error(LOGGER_NAME, "findTimestamp",
							"Error parsing " + value.getPropertyValue() + " as timestamp", e);
					return null;
				}
			}
		}
		return this.fLastReadTimeStamps.get(pName);
	}

	private HashMap<String, Long> loadTimeStampsFromDB()
	{
		HashMap<String, Long> timeStamps = new HashMap<>();
		ConfigValueList values;
		try
		{
			values = ConfigValueList.loadByTypeAndName(CONFIG_NAME, null);
		}
		catch (Exception e)
		{
			values = new ConfigValueList();
			Logging.error(LOGGER_NAME, "loadTimeStampsFromDB",
				"Error loading timestamps, config modified values will be empty", e);
		}
		for (ConfigValue value : values)
		{
			Long timeStamp = 0L;
			try
			{
				timeStamp = Timestamp.valueOf(value.getPropertyValue()).getTime();
			}
			catch (Exception e)
			{
				Logging.error(LOGGER_NAME, "loadTimeStampsFromDB",
						"Error parsing " + value.getPropertyValue() + " as timestamp", e);
			}
			timeStamps.put(value.getPropertyName(), timeStamp);
		}
		this.fLastReadTimeStamps = timeStamps;
		return timeStamps;
	}

	private synchronized void doWork(RefreshCache pCache, boolean pForceRefresh, UsersID pUpdatedBy)
			throws SQLException
	{
		if (pCache != null)
		{
			Logging.debug(LOGGER_NAME, "doWork", "Adding cache: " + pCache.getCacheName());
			this.fCaches.put(pCache.getCacheName(), pCache);
			long modified = -1;
			if (this.fLastReadTimeStamps.get(pCache.getCacheName()) != null)
				modified = this.fLastReadTimeStamps.get(pCache.getCacheName());
			pCache.setLastModifiedMS(modified);
		}
		else
		{
			Logging.debug(LOGGER_NAME, "doWork", "Checking cache timestamps");
			Iterator<RefreshCache> caches = this.fCaches.values().iterator();
			if (pForceRefresh)
			{
				while (caches.hasNext())
				{
					RefreshCache cache = caches.next();
					Logging.debugf(LOGGER_NAME, "doWork", "Refreshing %s (forced)", cache.getCacheName());
					cache.refresh();
				}
			}
			else
			{
				HashMap<String, Long> timeStamps = this.loadTimeStampsFromDB();
				while (caches.hasNext())
				{
					RefreshCache cache = caches.next();
					Long timeStamp = timeStamps.get(cache.getCacheName());
					if (timeStamp == null)
					{
						this.setTimeStamp(cache.getCacheName(), cache.getLastModifiedMS(), pUpdatedBy);
					}
					else if (timeStamp > cache.getLastModifiedMS())
					{
						Logging.debugf(LOGGER_NAME, "doWork", "Refreshing %s (old timestamp)", cache.getCacheName());
						cache.refresh();
						cache.setLastModifiedMS(timeStamp);
					}
				}
			}

		}
	}

	public void setTimeStamp(String pCacheName, long pTimeStamp, UsersID pBy) throws SQLException
	{
		this.setTimeStamp(pCacheName, pTimeStamp, true, pBy);
	}

	public void setTimeStamp(String pCacheName, long pTimeStamp, boolean pAddIfMissing, UsersID pBy)
			throws SQLException
	{
		ConfigValue value = ConfigValue.findByName(CONFIG_NAME, pCacheName);
		if (value == null && pAddIfMissing)
			value = new ConfigValue(CONFIG_NAME, pCacheName, null, pBy);
		if (value != null)
		{
			value.setPropertyValue(new Timestamp(pTimeStamp).toString());
			value.saveOrUpdate(pBy);
		}
	}

	public void stopRunning()
	{
		this.fStopRunning = true;
		this.fRefreshJobs.shutdown();
	}

	public boolean isRunning()
	{
		return this.fIsRunning;
	}

	@Override
	public void run()
	{
		Logging.debug(LOGGER_NAME, "run", "Starting...");
		long nextRun = 0;
		this.fIsRunning = true;
		while (!this.fStopRunning)
		{
			long now = System.currentTimeMillis();
			if (now >= nextRun)
			{
				try
				{
					this.doWork(null, false, UsersID.ADMIN);
				}
				catch (Exception e)
				{
					Logging.error(LOGGER_NAME, "run", "Unexpected error in doWork, will try again", e);
				}
				nextRun = System.currentTimeMillis() + RUN_INTERVAL_MS;
			}
			try
			{
				Thread.sleep(SLEEP_MS);
			}
			catch (InterruptedException e)
			{
				Logging.error(LOGGER_NAME, "run", "Interrupted thread", e);
			}
		}
		Logging.debug(LOGGER_NAME, "run", "Stopping...");
		this.fStopRunning = false;
		this.fIsRunning = false;
	}

	public Timestamp scheduleRefresh(RefreshCache pCache, int pSeconds, boolean pLogScheduledAsError, UsersID pBy)
	{
		Timestamp scheduledFor = DateUtil.addSeconds(DateUtil.now(), pSeconds);
		return this.scheduleRefresh(pCache, scheduledFor, pLogScheduledAsError, pBy);
	}

	public Timestamp scheduleRefresh(RefreshCache pCache, Timestamp pScheduledFor, boolean pLogScheduledAsError, UsersID pBy)
	{
		RefreshCacheJob job = new RefreshCacheJob(pCache, pBy);
		return this.fRefreshJobs.addJob(job, pScheduledFor, pLogScheduledAsError);
	}

}
