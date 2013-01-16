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

package com.exedio.copedemo.cronjob;

import com.exedio.cope.util.JobContext;
import com.exedio.copedemo.MainProperties;
import com.exedio.cronjob.Job;

abstract class AbstractJob implements Job
{
	private final String name;

	AbstractJob()
	{
		final String className = getClass().getSimpleName();
		final String POSTFIX = "Job";
		this.name =
			(className.endsWith(POSTFIX))
			? className.substring(0, className.length()-POSTFIX.length())
			: className;
	}

	@Override
	public void init()
	{
		// empty
	}

	@Override
	public void destroy()
	{
		// empty
	}

	@Override
	public final boolean isActiveInitially()
	{
		return true;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public long getInitialDelayInMilliSeconds()
	{
		return getMinutesBetweenExecutions() * 60l*1000l;
	}

	@Override
	public final long getStopTimeout()
	{
		return 1000;
	}


	public abstract void runWatched(JobContext ctx) throws Exception;

	/**
	 * TODO remove when cronjob provides onException
	 */
	@Override
	public void run(final JobContext ctx) throws Exception
	{
		try
		{
			runWatched(ctx);
		}
		catch(final Exception e)
		{
			MainProperties.get().errorMailSource.createMail("AbstractJob", e);
			throw e;
		}
	}
}
