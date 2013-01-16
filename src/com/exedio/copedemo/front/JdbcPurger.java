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

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class JdbcPurger
{
	private static final Logger logger = Logger.getLogger(JdbcPurger.class.getName());

	static void clearDriverRegistrations()
	{
		final ClassLoader cl = JdbcPurger.class.getClassLoader();

		for(final Enumeration<Driver> drivers = DriverManager.getDrivers(); drivers.hasMoreElements(); )
		{
			final Driver driver = drivers.nextElement();

			if(driver.getClass().getClassLoader()!=cl)
				continue;

			if(logger.isLoggable(Level.INFO))
				logger.log(Level.INFO, "Deregistering jdbc driver {0}", new Object[]{driver.getClass().getName()});

			try
			{
				DriverManager.deregisterDriver(driver);
			}
			catch(final SQLException e)
			{
				if(logger.isLoggable(Level.SEVERE))
					logger.log(Level.SEVERE, "Deregistering jdbc driver", e);
			}
		}
	}

	private JdbcPurger()
	{
		// prevent instantiation
	}
}
