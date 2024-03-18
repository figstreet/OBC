package com.figstreet.core;

import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;

public class JobTimerTest {

    @Test
    //test adding a job, the job does not run
    public void addJobWithJobTest() {
        JobTimer jobTimer = new JobTimer("TestOwner", false);
        Job mockJob = Mockito.mock(Job.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp tm = jobTimer.addJob(mockJob, timestamp, false);
        assertEquals(tm, timestamp);
    }

    @Test
    //test adding a null job
    public void addJobWithoutJobTest() {
        JobTimer jobTimer = new JobTimer("TestOwner", false);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp tm = jobTimer.addJob(null, timestamp, false);
        assertNull(tm);
    }

    @Test
    //test adding two jobs, the latter with an execution time before the former
    public void addJobTimeBeforeTest() throws InterruptedException {
        JobTimer jobTimer = new JobTimer("TestOwner", false);
        Job mockJob = Mockito.mock(Job.class);
        Timestamp beforeTimestamp = new Timestamp(System.currentTimeMillis());
        Thread.sleep(1200);
        Timestamp afterTimestamp = new Timestamp(System.currentTimeMillis());
        jobTimer.addJob(mockJob, afterTimestamp, false);
        Timestamp tm = jobTimer.addJob(mockJob, beforeTimestamp, false);
        assertEquals(tm, beforeTimestamp);
    }

    @Test
    //test adding two jobs with the flag to log in true
    public void addJobTest() throws InterruptedException {
        JobTimer jobTimer = new JobTimer(null, true);
        Job mockJob = Mockito.mock(Job.class);
        Thread.sleep(1200);
        jobTimer.addJob(mockJob, true);
        jobTimer.addJob(mockJob, null, true);
        jobTimer.start();
        Thread.sleep(3000);
    }

    @Test
    //test executing tests
    public void processTest() throws InterruptedException {
        JobTimer jobTimer = new JobTimer("TestOwner", false);
        Job mockJob = Mockito.mock(Job.class);
        doNothing().when(mockJob).start();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        jobTimer.addJob(mockJob, timestamp, false);
        jobTimer.start();
        Thread.sleep(3000);
    }

    @Test
    public void stopRunningTest() throws InterruptedException {
        JobTimer jobTimer = new JobTimer("TestOwner", false);
        Job mockJob = Mockito.mock(Job.class);
        doNothing().when(mockJob).start();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        jobTimer.addJob(mockJob, timestamp, false);
        jobTimer.start();
        jobTimer.stopRunning();
    }

}
