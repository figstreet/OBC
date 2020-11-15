package com.figstreet.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

public class JobTimer extends RunnableProcess
{
	// The jobs to start at a specific time
	private final ConcurrentSkipListMap<Timestamp, LinkedHashSet<Job>> fStartTimeMap = new ConcurrentSkipListMap<>();
	// The list of jobs in queue
	private final HashMap<Job, Timestamp> fJobMap = new HashMap<>();
	// the list of running jobs
	private final List<Job> fRunningJobs = new ArrayList<Job>();
	private final String fOwner;
	private final String fLoggerName;
	private final Object LOCK = new Object();

	public JobTimer(String pOwner, boolean pLogProcessingMessages)
	{
		super(500, TimeUnit.MILLISECONDS, 900, pLogProcessingMessages);
		if (pOwner != null)
		{
			this.setName(this.getName() + "_" + pOwner);
			this.fOwner = pOwner + "_";
		}
		else
			this.fOwner = "";

		this.fLoggerName = this.getClass().getName();
	}

	@Override
	public String getLoggerName()
	{
		return this.fLoggerName;
	}

	@Override
	public void stopRunning()
	{
		super.stopRunning();

		long waitMS = 500;

		int stillRunning = 1;
		while (stillRunning > 0)
		{
			stillRunning = this.removeCompletedJobs(true);

			if (stillRunning > 0)
			{
				Logging.debug(this.fLoggerName, this.fOwner + "stopRunning",
						"Waiting on " + stillRunning + " job" + (stillRunning > 1 ? "s " : " ") + "to complete.");
				try
				{
					TimeUnit.MILLISECONDS.sleep(waitMS);
				}
				catch (InterruptedException e)
				{
					/* ignore */
				}
			}
		}

		if (!this.fJobMap.isEmpty())
		{
			int jobsInQueue = 0;
			for (LinkedHashSet<Job> jobSet : this.fStartTimeMap.values())
			{
				if (jobSet != null)
					jobsInQueue += jobSet.size();
			}

			Logging.warnf(this.fLoggerName, this.fOwner + "stopRunning", "Stopped with %d jobs in queue", jobsInQueue);
		}
	}

	// Adds a job to start right now
	public Timestamp addJob(Job pJob, boolean pLogScheduledAsError)
	{
		Timestamp now = DateUtil.now();
		return this.addJob(pJob, now, false, pLogScheduledAsError);
	}

	// Adds a job to start at that specific time, and won't duplicate this Job
	public Timestamp addJob(Job pJob, Timestamp pStartTime, boolean pLogScheduledAsError)
	{
		return this.addJob(pJob, pStartTime, false, pLogScheduledAsError);
	}

	// Adds a job to start at that specific time
	// pass true for pAllowDuplicate if multiple instances of this job should be
	// scheduled
	public Timestamp addJob(Job pJob, Timestamp pStartTime, boolean pAllowDuplicate, boolean pLogScheduledAsError)
	{
		Timestamp currentStartTime = null;
		if (pJob == null)
			return currentStartTime;

		if (pStartTime == null)
			pStartTime = DateUtil.now();

		synchronized (this.LOCK)
		{
			if (!pAllowDuplicate)
				currentStartTime = this.fJobMap.get(pJob);

			if (currentStartTime != null)
			{
				if (pStartTime.before(currentStartTime)) // Attempting to schedule job earlier
				{
					// Remove existing job
					LinkedHashSet<Job> jobSet = this.fStartTimeMap.get(currentStartTime);
					if (jobSet != null)
						jobSet.remove(pJob);
					else
						Logging.errorf(this.fLoggerName, this.fOwner + "addJob",
								"Expected to find job % at timestamp %s, but it wasn't there; this job may run twice",
								pJob.getJobName(), currentStartTime.toString());
					currentStartTime = null;
				}
			}

			if (currentStartTime == null)
			{
				this.fJobMap.put(pJob, pStartTime);
				LinkedHashSet<Job> scheduledSet = this.fStartTimeMap.get(pStartTime);
				if (scheduledSet == null)
				{
					scheduledSet = new LinkedHashSet<>();
					this.fStartTimeMap.put(pStartTime, scheduledSet);
				}
				scheduledSet.add(pJob);
				currentStartTime = pStartTime;

				if (pLogScheduledAsError)
					Logging.errorf(this.fLoggerName, this.fOwner + "addJob", "Added job %s scheduled for %s",
							pJob.getJobName(), DateUtil.formatDate(currentStartTime, DateUtil.ISO8601_TIMESTAMP_FORMAT));
				if (!this.isRunning())
					Logging.errorf(this.fLoggerName, this.fOwner + "addJob",
							"Added job %s, but the JobTimer isn't running.", pJob.getJobName());
			}
		}
		return currentStartTime;
	}

	@Override
	protected void process()
	{
		try
		{
			if (this.isRunning())
			{
				this.removeCompletedJobs(false);

				synchronized (this.LOCK)
				{
					Timestamp now = DateUtil.now();
					NavigableSet<Timestamp> keySet = this.fStartTimeMap.keySet();
					for (Timestamp startTime : keySet)
					{
						if (!startTime.after(now))
						{
							Iterator<Job> jobIterator = this.fStartTimeMap.remove(startTime).iterator();
							while (jobIterator.hasNext())
							{
								Job toStart = jobIterator.next();
								if (this.isLogProcessingMessages())
									Logging.debug(this.fLoggerName, this.fOwner + "process",
											"Starting job " + toStart.getJobName());
								try
								{
									toStart.start();
									this.fJobMap.remove(toStart);
									this.fRunningJobs.add(toStart);
									if (jobIterator.hasNext()) // if there's another to start
										TimeUnit.MILLISECONDS.sleep(200);
								}
								catch (Exception ex)
								{
									Logging.error(this.fLoggerName, this.fOwner + "process",
											"Error starting job " + toStart.getJobName(), ex);
								}
							}
						}
						else
							break;
					}
				}
			}
		}
		catch (Exception e)
		{
			Logging.fatal(this.fLoggerName, this.fOwner + "process", "Error while running; finishing", e);
		}

		if (this.isLogProcessingMessages())
			Logging.debug(this.fLoggerName, this.fOwner + "process", "Finished");
	}

	// Returns number of jobs still running
	private int removeCompletedJobs(boolean pRequestStop)
	{
		synchronized (this.LOCK)
		{
			if (!this.fRunningJobs.isEmpty())
			{
				ArrayList<Job> toRemoveList = new ArrayList<>(this.fRunningJobs.size());
				for (Job runningJob : this.fRunningJobs)
				{
					if (!runningJob.isAlive())
					{
						toRemoveList.add(runningJob);

						if (!runningJob.isProcessComplete())
						{
							Logging.errorf(this.fLoggerName, this.fOwner + "removeCompletedJobs",
									"%s (thread %d) didn't complete prior to stopping!", runningJob.getJobName(),
									runningJob.getId());
						}
					}
					else if (pRequestStop)
					{
						runningJob.stopProcessing();
					}
				}

				for (Job toRemove : toRemoveList)
					this.fRunningJobs.remove(toRemove);
			}
			return this.fRunningJobs.size();
		}
	}

}
