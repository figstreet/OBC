package com.figstreet.core;

import java.util.concurrent.TimeUnit;

public abstract class RunStateThread extends Thread
{
	public static final int RUN_SLEEP_MILLIS = 1000;
	public static final int SHUTDOWN_SLEEP_MILLIS = 250;

	private RunState fRunState = RunState.STOPPED;

	public abstract String getLoggerName();

	public void shutdown()
	{
		Logging.info(this.getLoggerName(), "shutdown", "Starting shutdown");

		this.stopRunning();
		while (this.isAlive() && !this.isStopped())
		{
			Logging.info(this.getLoggerName(), "shutdown", "Waiting for stop");
			try
			{
				TimeUnit.MILLISECONDS.sleep(SHUTDOWN_SLEEP_MILLIS); // wait for threads to finish
			}
			catch (InterruptedException e)
			{
				Logging.info(this.getLoggerName(), "shutdown", "Interrupted during shutdown", e);
			}
		}
		Logging.info(this.getLoggerName(), "shutdown", "All done.");
	}

	protected void setRunning()
	{
		this.fRunState = RunState.RUNNING;
	}

	protected boolean isRunning()
	{
		if (this.isAlive())
			return RunState.RUNNING.equals(this.fRunState);
		Logging.info(this.getLoggerName(), "isRunning", "Thread is not alive");
		this.setStopped();
		return false;
	}

	protected void sleepWhileRunning(long pSleepMillis)
	{
		long sleepCycles = (long)Math.ceil((double)pSleepMillis / (double)RUN_SLEEP_MILLIS);
		for (int cycle = 0; this.isRunning() && (cycle < sleepCycles); cycle++)
		{
			try
			{
				TimeUnit.MILLISECONDS.sleep(RUN_SLEEP_MILLIS);
			}
			catch (InterruptedException e)
			{
				Logging.info(this.getLoggerName(), "sleepWhileRunning", "Interrupted during sleep", e);
			}
		}
	}

	protected void sleepWhileRunning()
	{
		this.sleepWhileRunning(RUN_SLEEP_MILLIS);
	}

	public void stopRunning()
	{
		Logging.infof(this.getLoggerName(), "stopRunning", "Stop request received, current state %s", this.fRunState);
		if (RunState.RUNNING.equals(this.fRunState))
			this.fRunState = RunState.STOPPING;
	}

	protected void setStopped()
	{
		this.fRunState = RunState.STOPPED;
	}

	private boolean isStopped()
	{
		return RunState.STOPPED.equals(this.fRunState);
	}
}
