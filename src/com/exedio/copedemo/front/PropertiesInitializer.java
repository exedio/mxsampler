/*
 * Copyright (C) 2004-2012  exedio GmbH (www.exedio.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.exedio.copedemo.front;

import static com.exedio.cope.misc.ConnectToken.removePropertiesVoid;

import com.exedio.cope.servletutil.ServletSource;
import com.exedio.copedemo.Main;
import com.exedio.copedemo.MainProperties;
import com.exedio.copedemo.cronjob.MxSamplerJob;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.LoggerFactory;

public final class PropertiesInitializer implements ServletContextListener
{
	@Override
	public void contextInitialized(final ServletContextEvent sce)
	{
		// load jdbc driver
		try
		{
			Class.forName(Main.class.getName());
		}
		catch(final ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		final ServletContext ctx = sce.getServletContext();

		final MainProperties properties = MainProperties.instance.set(MainProperties.factory().create(ServletSource.create(ctx)));
		if(properties.mxSampler!=null)
			properties.mxSampler.setProperties(MxSamplerJob.model);
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce)
	{
		MainProperties.instance.remove();
		removePropertiesVoid(MxSamplerJob.model);
		JdbcPurger.clearDriverRegistrations();
		contextDestroyedLogger();
	}

	private static void contextDestroyedLogger()
	{
		try
		{
			Class.forName("ch.qos.logback.classic.LoggerContext").
			getDeclaredMethod("stop").
			invoke(LoggerFactory.getILoggerFactory());
		}
		catch(final ReflectiveOperationException e)
		{
			throw new RuntimeException(e);
		}
	}
}
