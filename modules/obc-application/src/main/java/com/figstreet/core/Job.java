package com.figstreet.core;

import java.sql.Timestamp;


public abstract class Job extends Thread
{
	private Timestamp fCreated;
	private Timestamp fStarted;

	private boolean fProcessComplete = true;
	private volatile boolean fStopRequested = false;

	public Job()
	{
		super();
		this.fCreated = DateUtil.now();
	}

	public abstract String getJobName();

	public abstract void process();

	public abstract String getLoggerName();

	@Override
	public void run()
	{
		Logging.infof(this.getLoggerName(), "run", "Start job %s", this.getJobName());
		this.fProcessComplete = false;
		this.fStarted = DateUtil.now();
		this.process();
		this.fProcessComplete = true;
		Logging.infof(this.getLoggerName(), "run", "End job %s", this.getJobName());
	}

	public void stopProcessing()
	{
		this.fStopRequested = true;
	}

	protected boolean isStopRequested()
	{
		return this.fStopRequested;
	}

	public boolean isProcessComplete()
	{
		return this.fProcessComplete;
	}

	public Timestamp getCreated()
	{
		return this.fCreated;
	}

	public Timestamp getStarted()
	{
		return this.fStarted;
	}

	@Override
	public int hashCode()
	{
		return this.getJobName().hashCode();
	}

	@Override
	public boolean equals(Object pObj)
	{
		if (pObj == null)
			return false;
		if (!(pObj instanceof Job))
			return false;

		return pObj.hashCode() == this.hashCode();
	}

}
