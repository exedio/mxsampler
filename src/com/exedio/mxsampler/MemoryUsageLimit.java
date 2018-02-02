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

import com.exedio.cope.util.Properties;
import com.exedio.sendmail.ErrorMailSource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

public final class MemoryUsageLimit extends Properties
{
	private final String poolName;
	private final int ratioPercent = value("ratioPercent", 75, 1);

	private long lastUsed = Long.MIN_VALUE;


	public void check(
			final MemoryPoolMXBean pool,
			final MemoryUsage collectionUsage,
			final ErrorMailSource errorMailSource)
	{
		if(matches(pool))
		{
			final long used = collectionUsage.getUsed();
			if(used==0 || lastUsed==used)
				return;

			// Full GC happened
			lastUsed = used;

			final long max = collectionUsage.getMax();
			final long usedLimit = (ratioPercent * max) / 100l;
			if(used > usedLimit)
				errorMailSource.createMail(
						"MxSampler MemoryUsageLimit " + pool.getName() + " exceeded: " +
						used + " > " + usedLimit + " = "+ ratioPercent +"% of " + max);
		}
	}

	@Probe String probePoolName()
	{
		for(final MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans())
		{
			if(matches(pool))
			{
				if(!MxSamplerMemoryPoolName.check(pool))
					throw new IllegalArgumentException("invalid pool name: >" + poolName + '<');
				if(!pool.isValid())
					throw new IllegalArgumentException("pool is not valid: >" + poolName + '<');

				return "valid pool: >" + poolName + '<';
			}
		}
		throw new IllegalArgumentException("pool does not exist: >" + poolName + '<');
	}

	private boolean matches(final MemoryPoolMXBean pool)
	{
		return poolName.equals(pool.getName());
	}


	// common code

	public static Factory<MemoryUsageLimit> factory(final String poolName)
	{
		return new Factory<MemoryUsageLimit>()
		{
			@Override
			public MemoryUsageLimit create(final Source source)
			{
				return new MemoryUsageLimit(source, poolName);
			}
		};
	}

	private MemoryUsageLimit(final Source source, final String poolName)
	{
		super(source);
		this.poolName = value("poolName", poolName);
	}
}
