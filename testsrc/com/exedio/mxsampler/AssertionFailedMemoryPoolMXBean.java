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

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import javax.management.ObjectName;
import junit.framework.AssertionFailedError;

class AssertionFailedMemoryPoolMXBean implements MemoryPoolMXBean
{
	@Override
	public ObjectName getObjectName()
	{
		throw new AssertionFailedError();
	}

	@Override
	public String getName()
	{
		throw new AssertionFailedError();
	}

	@Override
	public MemoryType getType()
	{
		throw new AssertionFailedError();
	}

	@Override
	public MemoryUsage getUsage()
	{
		throw new AssertionFailedError();
	}

	@Override
	public MemoryUsage getPeakUsage()
	{
		throw new AssertionFailedError();
	}

	@Override
	public void resetPeakUsage()
	{
		throw new AssertionFailedError();
	}

	@Override
	public boolean isValid()
	{
		throw new AssertionFailedError();
	}

	@Override
	public String[] getMemoryManagerNames()
	{
		throw new AssertionFailedError();
	}

	@Override
	public long getUsageThreshold()
	{
		throw new AssertionFailedError();
	}

	@Override
	public void setUsageThreshold(final long threshold)
	{
		throw new AssertionFailedError();
	}

	@Override
	public boolean isUsageThresholdExceeded()
	{
		throw new AssertionFailedError();
	}

	@Override
	public long getUsageThresholdCount()
	{
		throw new AssertionFailedError();
	}

	@Override
	public boolean isUsageThresholdSupported()
	{
		throw new AssertionFailedError();
	}

	@Override
	public long getCollectionUsageThreshold()
	{
		throw new AssertionFailedError();
	}

	@Override
	public void setCollectionUsageThreshold(final long threshold)
	{
		throw new AssertionFailedError();
	}

	@Override
	public boolean isCollectionUsageThresholdExceeded()
	{
		throw new AssertionFailedError();
	}

	@Override
	public long getCollectionUsageThresholdCount()
	{
		throw new AssertionFailedError();
	}

	@Override
	public MemoryUsage getCollectionUsage()
	{
		throw new AssertionFailedError();
	}

	@Override
	public boolean isCollectionUsageThresholdSupported()
	{
		throw new AssertionFailedError();
	}
}
