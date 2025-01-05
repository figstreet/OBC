package com.figstreet.service;

import com.figstreet.core.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitializerServlet extends HttpServlet
{
	private static final String LOG_PARAM = "logConfigPath";
	private static final String DB_CONFIG_PARAM = "dbConfigPath";
	private static final String REFRESH_CACHE_PARAM = "refreshCacheMinutes";

	@Override
	public void init() throws ServletException
	{
		try
		{
			SystemInitializer.initialize(this.locateInitParamResource(LOG_PARAM),
				this.locateInitParamResource(DB_CONFIG_PARAM));

			String refreshCacheMinutes = "";
			try
			{
				refreshCacheMinutes = this.locateInitParamResource(REFRESH_CACHE_PARAM);
				if (!CompareUtil.isEmpty(refreshCacheMinutes))
				{
					Logging.info(LOGGER_NAME, "init", "Setting RefreshCache refresh minutes to " + refreshCacheMinutes);
					long refreshMS = Long.parseLong(refreshCacheMinutes) * DateUtil.MILLIS_PER_MINUTE;
					if (refreshMS > 0)
						RefreshCacheManager.getThe().setRunIntervalMS(refreshMS);
				}
			}
			catch (Exception e)
			{
				Logging.info(LOGGER_NAME, "init", "Error setting RefreshCache refresh minutes to " + refreshCacheMinutes,
					e);
			}
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "init", "Error occurred while initializing resources!", e);
			throw new ServletException(e);
		}
		Logging.debug(LOGGER_NAME, "init", "Initialization finished!");
	}

	private String locateInitParamResource(String initParameter)
	{
		String value = this.getInitParameter(initParameter);
		if (value == null)
			value = this.getServletContext().getInitParameter(initParameter);
		if (value == null)
			return null;
		return value;
	}

	@Override
	protected void doGet(HttpServletRequest pArg0, HttpServletResponse pArg1) throws ServletException, IOException
	{
		throw new ServletException("nothing implemented for get");
	}

	@Override
	protected void doPost(HttpServletRequest pArg0, HttpServletResponse pArg1) throws ServletException, IOException
	{
		throw new ServletException("nothing implemented for post");
	}

	@Override
	public void destroy()
	{
		Logging.debug(LOGGER_NAME, "destroy", "begin");
		SystemInitializer.shutdown();
		super.destroy();
	}

	private static final String LOGGER_NAME = InitializerServlet.class.getPackage().getName() + ".InitializerServlet";
}
