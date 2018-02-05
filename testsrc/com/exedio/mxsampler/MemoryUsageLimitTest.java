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

import ch.qos.logback.classic.Logger;
import com.exedio.cope.util.Properties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collection;
import org.slf4j.LoggerFactory;

public final class MemoryUsageLimitTest extends ConnectedTest
{
	public void testExceeds()
	{
		final MemoryUsageLimit limit = MemoryUsageLimit.factory("myPoolName").create(source(45));

		final MyUsage collectionUsage = new MyUsage(46, 100);
		limit.check(new MyBean("myPoolName"), collectionUsage);
		appender.assertError("MxSampler MemoryUsageLimit myPoolName exceeded: 46 > 45 = 45% of 100");
		appender.assertEmpty();

		// do not send twice
		limit.check(new MyBean("myPoolName"), collectionUsage);
		appender.assertEmpty();

		// emulate Full GC
		final MyUsage collectionUsageGC = new MyUsage(47, 100);
		limit.check(new MyBean("myPoolName"), collectionUsageGC);
		appender.assertError("MxSampler MemoryUsageLimit myPoolName exceeded: 47 > 45 = 45% of 100");
		appender.assertEmpty();

		// do not send twice
		limit.check(new MyBean("myPoolName"), collectionUsageGC);
		appender.assertEmpty();
	}

	public void testAtBorder()
	{
		final MemoryUsageLimit limit = MemoryUsageLimit.factory("myPoolName").create(source(45));

		final MyUsage collectionUsage = new MyUsage(45, 100);
		limit.check(new MyBean("myPoolName"), collectionUsage);
		appender.assertEmpty();
	}

	public void testNameMismatch()
	{
		final MemoryUsageLimit limit = MemoryUsageLimit.factory("myPoolName").create(source(45));

		final MyUsage collectionUsage = new MyUsage(46, 100);
		limit.check(new MyBean("myPoolNameX"), collectionUsage);
		appender.assertEmpty();
	}


	private MockAppender appender;

	@Override
	@SuppressWarnings({"LoggerInitializedWithForeignClass", "OverlyStrongTypeCast"})
	protected void setUp() throws Exception
	{
		super.setUp();
		assertNull(appender);
		appender = new MockAppender();
		((Logger)LoggerFactory.getLogger(MemoryUsageLimit.class)).addAppender(appender);
	}

	@Override
	@SuppressWarnings({"LoggerInitializedWithForeignClass", "OverlyStrongTypeCast"})
	protected void tearDown() throws Exception
	{
		((Logger)LoggerFactory.getLogger(MemoryUsageLimit.class)).detachAppender(appender);
		appender = null;
		super.tearDown();
	}



	private static final class MyBean extends AssertionFailedMemoryPoolMXBean
	{
		private final String name;

		MyBean(final String name)
		{
			this.name = name;
		}

		@Override
		public String getName()
		{
			return name;
		}
	}

	private static final class MyUsage extends AssertionFailedMemoryUsage
	{
		private final long used;
		private final long max;

		MyUsage(final long used, final long max)
		{
			this.used = used;
			this.max  = max;
		}

		@Override
		public long getUsed()
		{
			return used;
		}

		@Override
		public long getMax()
		{
			return max;
		}
	}

	@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
	private static Properties.Source source(final int ratioPercent)
	{
		return new Properties.Source(){

			@Override public String get(final String key)
			{
				if("poolName".equals(key))
					return null;
				else if("ratioPercent".equals(key))
					return String.valueOf(ratioPercent);
				else
					throw new RuntimeException(key);
			}

			@Override public Collection<String> keySet()
			{
				throw new RuntimeException();
			}

			@Override public String getDescription()
			{
				return "description";
			}
		};
	}
}
