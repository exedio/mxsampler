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

package com.exedio.mxsampler;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;
import java.util.List;
import junit.framework.AssertionFailedError;

class AssertionErrorAppender implements Appender<ILoggingEvent>
{
	@Override
	public String getName()
	{
		throw new AssertionFailedError();
	}

	@Override
	public void doAppend(final ILoggingEvent iLoggingEvent) throws LogbackException
	{
		throw new AssertionFailedError("" + iLoggingEvent);
	}

	@Override
	public void setName(final String s)
	{
		throw new AssertionFailedError(s);
	}

	@Override
	public void setContext(final Context context)
	{
		throw new AssertionFailedError("" + context);
	}

	@Override
	public Context getContext()
	{
		throw new AssertionFailedError();
	}

	@Override
	public void addStatus(final Status status)
	{
		throw new AssertionFailedError("" + status);
	}

	@Override
	public void addInfo(final String s)
	{
		throw new AssertionFailedError();
	}

	@Override
	public void addInfo(final String s, final Throwable throwable)
	{
		throw new AssertionFailedError(s);
	}

	@Override
	public void addWarn(final String s)
	{
		throw new AssertionFailedError(s);
	}

	@Override
	public void addWarn(final String s, final Throwable throwable)
	{
		throw new AssertionFailedError(s);
	}

	@Override
	public void addError(final String s)
	{
		throw new AssertionFailedError(s);
	}

	@Override
	public void addError(final String s, final Throwable throwable)
	{
		throw new AssertionFailedError(s);
	}

	@Override
	public void addFilter(final Filter<ILoggingEvent> filter)
	{
		throw new AssertionFailedError("" + filter);
	}

	@Override
	public void clearAllFilters()
	{
		throw new AssertionFailedError();
	}

	@Override
	public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList()
	{
		throw new AssertionFailedError();
	}

	@Override
	public FilterReply getFilterChainDecision(final ILoggingEvent iLoggingEvent)
	{
		throw new AssertionFailedError("" + iLoggingEvent);
	}

	@Override
	public void start()
	{
		throw new AssertionFailedError();
	}

	@Override
	public void stop()
	{
		throw new AssertionFailedError();
	}

	@Override
	public boolean isStarted()
	{
		throw new AssertionFailedError();
	}
}
