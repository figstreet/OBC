package com.figstreet.core.h2server;

import java.util.ArrayList;

import com.figstreet.core.RunIntervalType;
import com.figstreet.core.RunnableProcess;
import com.figstreet.core.Logging;
import org.h2.tools.Server;


public class H2WebServer extends RunnableProcess
{
	private Server fWebServer;

	protected H2WebServer(long pCheckServerMillis, Integer pWebPort, boolean pWebServerAllowOthers, boolean pUseSSL)
	{
		super(pCheckServerMillis, RunIntervalType.TIMER_INTERVAL);
		Logging.debugBegin(LOGGER_NAME, "ctor");
		try
		{
			ArrayList<String> values = new ArrayList<String>(5);
			if (pWebPort != null)
			{
				values.add("-webPort");
				values.add(pWebPort.toString());
			}
			if (pWebServerAllowOthers)
				values.add("-webAllowOthers");
			if (pUseSSL)
				values.add("-webSSL");

			Logging.debugf(LOGGER_NAME, "ctor", "Attempting to start Web Server; config: %s", values.toString());
			this.fWebServer = Server.createWebServer(values.toArray(new String[values.size()]));
			this.fWebServer.start();
			start();

			Logging.debug(LOGGER_NAME, "ctor", "Web Server started.");
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "ctor", "Error starting Web Server: ", e);
		}
	}

	@Override
	public String getLoggerName()
	{
		return LOGGER_NAME;
	}

	@Override
	public void shutdown()
	{
		Logging.debugBegin(LOGGER_NAME, "shutdown");
		super.shutdown();
		if (this.fWebServer != null)
			this.fWebServer.stop();
	}

	@Override
	protected void process()
	{
		Logging.debug(LOGGER_NAME, "process", "Checking server status ...");
		try
		{
			if (this.fWebServer == null || !this.fWebServer.isRunning(false))
			{
				Logging.error(LOGGER_NAME, "process", "Web Server not running!");
			}
			else
				Logging.debug(LOGGER_NAME, "process", "Web Server reports online.");
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "process", e);
		}
		Logging.debug(LOGGER_NAME, "process", "Finished");
	}


	public static final String LOGGER_NAME = H2WebServer.class.getPackage().getName() + ".H2WebServer";
}
