package com.figstreet.core;

import com.figstreet.data.users.UsersID;

public class RefreshCacheJob extends Job
{
	public static final String LOGGER_NAME = RefreshCacheJob.class.getPackage().toString() + ".RefreshCacheJob";

	private RefreshCache fCache;
	private UsersID fBy;

	public RefreshCacheJob(RefreshCache pCache, UsersID pBy)
	{
		super();
		this.fCache = pCache;
		this.fBy = pBy;
	}

	@Override
	public String getJobName()
	{
		return "RefreshCache_" + this.fCache.getCacheName();
	}

	@Override
	public void process()
	{
		Logging.debugf(LOGGER_NAME, "process", "Refreshing cache %s by UsersID %s", this.fCache.getCacheName(),
				UsersID.asString(this.fBy));
		this.fCache.refresh();
	}

	@Override
	public String getLoggerName()
	{
		return LOGGER_NAME;
	}

}
