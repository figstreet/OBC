package com.figstreet.core.h2server;

import com.figstreet.core.Logging;
import com.figstreet.core.RunIntervalType;
import com.figstreet.core.RunnableProcess;
import org.h2.tools.Server;

import java.util.ArrayList;


public class H2DatabaseServer extends RunnableProcess
{
	private Server fTcpServer;

	protected H2DatabaseServer(long pCheckServerMillis, Integer pTcpPort, boolean pTcpServerAllowOthers, boolean pUseSSL, boolean pAllowDatabaseCreation)
	{
		super(pCheckServerMillis, RunIntervalType.TIMER_INTERVAL);
		Logging.debugBegin(LOGGER_NAME, "ctor");
		try
		{
			ArrayList<String> values = new ArrayList<String>(5);
			if (pTcpPort != null)
			{
				values.add("-tcpPort");
				values.add(pTcpPort.toString());
			}
			if (pTcpServerAllowOthers)
				values.add("-tcpAllowOthers");
			if (pUseSSL)
				values.add("-tcpSSL");
			if (pAllowDatabaseCreation)
				values.add("-ifExists");

			Logging.debugf(LOGGER_NAME, "ctor", "Attempting to start TCP Server; config: %s", values.toString());
			this.fTcpServer = Server.createTcpServer(values.toArray(new String[values.size()]));
			this.fTcpServer.start();
			Logging.debug(LOGGER_NAME, "ctor", "TCP Server started.");

			start();
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "ctor", "Error starting TCP Server: ", e);
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
		if (this.fTcpServer != null)
			this.fTcpServer.stop();
	}

	@Override
	protected void process()
	{
		Logging.debug(LOGGER_NAME, "process", "Checking server status ...");
		try
		{
			if (this.fTcpServer == null || !this.fTcpServer.isRunning(false))
			{
				Logging.error(LOGGER_NAME, "process", "TCP Server not running!");
			}
			else
				Logging.debug(LOGGER_NAME, "process", "TCP Server reports online.");
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "process", e);
		}
		Logging.debug(LOGGER_NAME, "process", "Finished");
	}

	private static final String LOGGER_NAME = H2DatabaseServer.class.getPackage().getName() + ".H2DatabaseServer";
}
