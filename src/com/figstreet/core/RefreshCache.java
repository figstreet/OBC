package com.figstreet.core;

import java.sql.SQLException;

import javax.mail.search.SearchException;

import com.figstreet.data.users.UsersID;

public abstract class RefreshCache
{
	private String fName;
	private long fLastModifiedMS = -1;

	protected RefreshCache(String pName)
	{
		this.fName = pName;
		// TODO RefreshCacheManager.getThe().addCache(this);
	}

	protected abstract void refresh();

	public abstract String getLoggerName();

	protected String getCacheName()
	{
		return this.fName;
	}

	protected long getLastModifiedMS()
	{
		return this.fLastModifiedMS;
	}

	protected void setLastModifiedMS(long pLastModifiedMS)
	{
		if (this.fLastModifiedMS < pLastModifiedMS)
		{
			this.fLastModifiedMS = pLastModifiedMS;
		}
	}

	public void markCacheModified(long pTimeStampMS, UsersID pBy) throws SQLException, SearchException
	{
		Logging.debugf(this.getLoggerName(), "markCacheModified", "Marking %s cache modified", this.getCacheName());
		// TODO RefreshCacheManager.getThe().setTimeStamp(this.getCacheName(),
		// pTimeStampMS, pBy);
		this.setLastModifiedMS(pTimeStampMS);
	}

	public void markCacheModified(UsersID pBy) throws SQLException, SearchException
	{
		this.markCacheModified(System.currentTimeMillis(), pBy);
	}

	public boolean checkIfModified(boolean pCheckDb) throws SQLException, SearchException
	{
		Long modified = null; //TODO RefreshCacheManager.getThe().findTimestamp(this.fName, pCheckDb);
		if (modified != null && modified > this.fLastModifiedMS)
		{
			this.fLastModifiedMS = modified;
			this.refresh();
			return true;
		}
		return false;
	}
}
