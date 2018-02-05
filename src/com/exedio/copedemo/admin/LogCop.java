/*
 * Copyright (C) 2004-2015  exedio GmbH (www.exedio.com)
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

package com.exedio.copedemo.admin;

import static com.exedio.cops.CopsServlet.getAuthentication;

import com.exedio.copedemo.misc.Hostname;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LogCop extends AdminCop
{
	private static final Logger defaultLogger = LoggerFactory.getLogger(LogCop.class);

	enum Level
	{
		trace {
			@Override boolean isEnabled(final Logger logger)
			{
				return logger.isTraceEnabled();
			}
			@Override void log(final Logger logger, final String msg, final Throwable t)
			{
				if(t!=null)
					logger.trace(msg, t);
				else
					logger.trace(msg);
			}
		},
		debug {
			@Override boolean isEnabled(final Logger logger)
			{
				return logger.isDebugEnabled();
			}
			@Override void log(final Logger logger, final String msg, final Throwable t)
			{
				if(t!=null)
					logger.debug(msg, t);
				else
					logger.debug(msg);
			}
		},
		info {
			@Override boolean isEnabled(final Logger logger)
			{
				return logger.isInfoEnabled();
			}
			@Override void log(final Logger logger, final String msg, final Throwable t)
			{
				if(t!=null)
					logger.info(msg, t);
				else
					logger.info(msg);
			}
		},
		warn {
			@Override boolean isEnabled(final Logger logger)
			{
				return logger.isWarnEnabled();
			}
			@Override void log(final Logger logger, final String msg, final Throwable t)
			{
				if(t!=null)
					logger.warn(msg, t);
				else
					logger.warn(msg);
			}
		},
		error {
			@Override boolean isEnabled(final Logger logger)
			{
				return logger.isErrorEnabled();
			}
			@Override void log(final Logger logger, final String msg, final Throwable t)
			{
				if(t!=null)
					logger.error(msg, t);
				else
					logger.error(msg);
			}
		};

		abstract boolean isEnabled(Logger logger);
		abstract void log(Logger logger, String msg, Throwable t);
	}

	static final String PATH_INFO = "log.html";

	static final String LOGGER_NAME = "LOGGER_NAME";
	static final String LOG = "LOG";
	static final String THROWABLE = "THROWABLE";

	LogCop()
	{
		super(PATH_INFO);
	}

	private String loggerName = defaultLogger.getName();

	@Override
	protected void post(
			final HttpServletRequest request)
	{
		loggerName = request.getParameter(LOGGER_NAME);
		for(final Level level : Level.values())
		{
			final String msg = request.getParameter(LOG + level.name());
			if(msg!=null && !msg.isEmpty())
			{
				final Throwable t = getBooleanParameter(request, THROWABLE)
						? new Exception("test exception message", new RuntimeException("test cause message"))
						: null;
				final Logger logger = LoggerFactory.getLogger(loggerName);
				final String fullMsg =
						msg + " TEST " +
						Hostname.get() + ' ' +
						getAuthentication(request);
				level.log(logger, fullMsg, t);
			}
		}
	}

	@Override
	void writeBody(final Out out, final HttpServletRequest request)
	{
		Log_Jspm.writeBody(out, LoggerFactory.getLogger(loggerName));
	}
}
