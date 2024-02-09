package com.figstreet.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class RunnableProcess extends RunStateThread
{
	public static final long ONCE_DAILY_INTERVAL_MILLIS = DateUtil.MILLIS_PER_DAY;

	private long fNextRunMillis;
	private long fRunIntervalMillis;
	private RunIntervalType fRunIntervalType;
	private TimeUnit fSleepTimeUnit;
	private int fSleepUnits;
	private boolean fLogProcessingMessages;

	protected RunnableProcess(long pRunIntervalMillis, RunIntervalType pRunIntervalType)
	{
		this(System.currentTimeMillis(), pRunIntervalMillis, pRunIntervalType, TimeUnit.MILLISECONDS, RUN_SLEEP_MILLIS,
			true);
	}

	protected RunnableProcess(long pRunIntervalMillis, TimeUnit pSleepTimeUnit, int pSleepUnits,
		boolean pLogProcessingMessages)
	{
		this(System.currentTimeMillis(), pRunIntervalMillis, RunIntervalType.TIMER_INTERVAL, pSleepTimeUnit,
			pSleepUnits, pLogProcessingMessages);
	}

	protected RunnableProcess(long pNextRunMillis, long pRunIntervalMillis, RunIntervalType pRunIntervalType,
		TimeUnit pSleepTimeUnit, int pSleepUnits, boolean pLogProcessingMessages)
	{
		this.fNextRunMillis = pNextRunMillis;
		this.fRunIntervalMillis = pRunIntervalMillis;
		this.fRunIntervalType = pRunIntervalType;
		this.fSleepTimeUnit = pSleepTimeUnit;
		if (this.fSleepTimeUnit == null)
			this.fSleepTimeUnit = TimeUnit.SECONDS;
		this.fSleepUnits = pSleepUnits;
		if (this.fSleepUnits <= 0)
			this.fSleepUnits = 1;

		this.fLogProcessingMessages = pLogProcessingMessages;
		super.setName(this.getClass().getSimpleName());
	}

	@Override
	public void run()
	{
		Logging.info(this.getLoggerName(), "run", String.format("Start, processing %severy %d millis...",
			(RunIntervalType.POST_PROCESSING_INTERVAL.equals(this.fRunIntervalType) ? "approximately " : ""),
			this.fRunIntervalMillis));
		this.setRunning();
		try
		{
			while (this.isRunning())
			{
				if (System.currentTimeMillis() >= this.fNextRunMillis)
				{
					if (this.isMaintenanceWindow())
					{
						Logging.info(this.getLoggerName(), "run", "Not running; within maintenance window");
					}
					else
					{
						if (this.isLogProcessingMessages())
							Logging.info(this.getLoggerName(), "run", "Processing...");

						this.process();
					}

					this.setNextRunMillis();
				}

				if (this.isRunning())
				{
					try
					{
						this.fSleepTimeUnit.sleep(this.fSleepUnits);
					}
					catch (InterruptedException e)
					{
						Logging.error(this.getLoggerName(), "run", "Interrupted while sleeping", e);
					}
				}
			}
		}
		catch (Exception e)
		{
			Logging.error(this.getLoggerName(), "run", "Unexpected error while running", e);
		}
		finally
		{
			this.setStopped();
		}
		Logging.info(this.getLoggerName(), "run", "Done!");
	}

	protected abstract void process();

	protected boolean isMaintenanceWindow()
	{
		return false;
	}

	public long getNextRunMillis()
	{
		return this.fNextRunMillis;
	}

	//Not allowing caller set this to less than MILLIS_PER_SECOND
	public void setRunIntervalMillis(long pRunIntervalMillis)
	{
		if (pRunIntervalMillis < DateUtil.MILLIS_PER_SECOND)
			this.fRunIntervalMillis = DateUtil.MILLIS_PER_SECOND;
		else
			this.fRunIntervalMillis = pRunIntervalMillis;
	}

	public long getRunIntervalMillis()
	{
		return this.fRunIntervalMillis;
	}

	protected void setNextRunMillis()
	{
		// this logic is need to deal with day light savings effect on time of day
		long timeOfDay = 0;
		boolean keepTimeOfDay = this.fRunIntervalMillis == ONCE_DAILY_INTERVAL_MILLIS;
		if (keepTimeOfDay)
			timeOfDay = DateUtil.timeOnlyMillis(new Date(this.fNextRunMillis));

		this.fNextRunMillis = calcNextRunMS(this.fNextRunMillis, this.fRunIntervalMillis, this.fRunIntervalType);

		if (keepTimeOfDay)
			this.fNextRunMillis = DateUtil.dateOnly(new Date(this.fNextRunMillis)).getTime() + timeOfDay;
	}

	protected static long calcNextRunMS(long pLastRunMS, long pIntervalMS, RunIntervalType pRunIntervalType)
	{
		if (RunIntervalType.POST_PROCESSING_INTERVAL.equals(pRunIntervalType))
			return System.currentTimeMillis() + pIntervalMS;

		return pLastRunMS + ((((System.currentTimeMillis() - pLastRunMS) / pIntervalMS) + 1) * pIntervalMS);
	}

	protected static long calcFirstRunMS(long pFutureRunMS, long pIntervalMS)
	{
		long currentTimeMillis = System.currentTimeMillis();
		long partialInterval = (pFutureRunMS < currentTimeMillis) ? 1 : 0;
		return pFutureRunMS + ((((currentTimeMillis - pFutureRunMS) / pIntervalMS) + partialInterval) * pIntervalMS);
	}

	public boolean isLogProcessingMessages()
	{
		return this.fLogProcessingMessages;
	}

	public void setLogProcessingMessages(boolean pLogProcessingMessages)
	{
		this.fLogProcessingMessages = pLogProcessingMessages;
	}

}
