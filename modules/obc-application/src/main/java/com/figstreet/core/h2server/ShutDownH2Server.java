package com.figstreet.core.h2server;

import com.figstreet.core.Logging;

import java.util.concurrent.TimeUnit;

public class ShutDownH2Server extends Thread
{
	@Override
	public void run()
	{
		Logging.error(LOGGER_NAME, "run", "Stopping H2Server!!!");
		H2Server.stopRunning();
		try
		{
			while (!H2Server.isStopped())
			{
				Logging.info(LOGGER_NAME, "run", "Waiting on H2Server to stop");
				TimeUnit.SECONDS.sleep(2);
			}
		}
		catch (InterruptedException e)
		{
			Logging.error(LOGGER_NAME, "run", e);
		}
	}

	private static final String LOGGER_NAME = ShutDownH2Server.class.getPackage().getName() + ".ShutDownH2Server";
}
