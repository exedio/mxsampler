/*
 * Copyright (C) 2004-2012  exedio GmbH (www.exedio.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.exedio.mxsampler;

import com.exedio.cope.Model;
import com.exedio.cope.SetValue;
import com.exedio.cope.Type;
import com.exedio.cope.misc.ConnectToken;
import com.exedio.cope.util.JobContext;
import com.exedio.copedemo.MainProperties;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MxSampler
{
	private final Model samplerModel;

	private final AtomicInteger runningSource = new AtomicInteger(0);

	public MxSampler()
	{
		this.samplerModel =
			Model.builder().add(
				MxSamplerGlobal.TYPE,
				MxSamplerMemoryPoolName.TYPE,
				MxSamplerMemoryPool.TYPE,
				MxSamplerPurge.TYPE).
				add(new MxSamplerRevisions()).
				build();
		// TODO make a meaningful samplerModel#toString()
	}

	public final Model getModel()
	{
		return samplerModel;
	}

	public final ConnectToken connect(final String tokenName)
	{
		final ConnectToken result = ConnectToken.issue(samplerModel, tokenName);

		boolean mustReturn = true;
		try
		{
			check();
			mustReturn = false;
		}
		finally
		{
			if(mustReturn)
				result.returnItConditionally();
		}
		// DO NOT WRITE ANYTHING HERE,
		// OTHERWISE ConnectTokens MAY BE LOST
		return result;
	}

	void check()
	{
		samplerModel.reviseIfSupportedAndAutoEnabled();
	}

	public final void sample()
	{
		sampleInternal();
	}

	MxSamplerGlobal sampleInternal()
	{
		// prepare
		final MainProperties properties = MainProperties.get();
		final HashMap<MemoryPoolMXBean, MemoryUsage> memoryUsage = new HashMap<>();
		final HashMap<MemoryPoolMXBean, MemoryUsage> memoryCollectionUsage = new HashMap<>();

		// gather data
		final long start = System.nanoTime();
		final Date date = new Date();
		for(final MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans())
		{
			if(pool.isValid() && MxSamplerMemoryPoolName.check(pool))
			{
				memoryUsage.put(pool, pool.getUsage());
				memoryCollectionUsage.put(pool, pool.getCollectionUsage());
			}
		}
		final ClassLoadingMXBean classLoading = ManagementFactory.getClassLoadingMXBean();
		final long classTotalLoaded = classLoading.getTotalLoadedClassCount();
		final int classLoaded = classLoading.getLoadedClassCount();
		final long classUnloaded = classLoading.getUnloadedClassCount();
		final int objectPendingFinalizationCount = ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount();
		final long totalCompilationTime = ManagementFactory.getCompilationMXBean().getTotalCompilationTime();
		final OperatingSystemMXBean operatingSystem = ManagementFactory.getOperatingSystemMXBean();
		final int availableProcessors = operatingSystem.getAvailableProcessors();
		final double systemLoadAverage = operatingSystem.getSystemLoadAverage();
		final ThreadMXBean threads = ManagementFactory.getThreadMXBean();
		final int threadCount = threads.getThreadCount();
		final int peakThreadCount = threads.getPeakThreadCount();
		final long totalStartedThreadCount = threads.getTotalStartedThreadCount();
		final int daemonThreadCount = threads.getDaemonThreadCount();

		final long duration = System.nanoTime() - start;

		// process data
		final ArrayList<SetValue<?>> sv = new ArrayList<>();
		final int running = runningSource.getAndIncrement();

		// save data
		try
		{
			samplerModel.startTransaction(this + " sample");
			final MxSamplerGlobal model;
			{
				sv.clear();
				sv.add(MxSamplerGlobal.date.map(date));
				sv.add(MxSamplerGlobal.duration.map(duration));
				sv.add(MxSamplerGlobal.sampler.map(System.identityHashCode(this)));
				sv.add(MxSamplerGlobal.running.map(running));

				sv.add(MxSamplerGlobal.classTotalLoaded.map(classTotalLoaded));
				sv.add(MxSamplerGlobal.classLoaded.map(classLoaded));
				sv.add(MxSamplerGlobal.classUnloaded.map(classUnloaded));
				sv.add(MxSamplerGlobal.objectPendingFinalizationCount.map(objectPendingFinalizationCount));
				sv.add(MxSamplerGlobal.totalCompilationTime.map(totalCompilationTime));
				sv.add(MxSamplerGlobal.availableProcessors.map(availableProcessors));
				//noinspection FloatingPointEquality
				sv.add(MxSamplerGlobal.systemLoadAverage.map(systemLoadAverage==-1?null:systemLoadAverage));
				sv.add(MxSamplerGlobal.threadCount.map(threadCount));
				sv.add(MxSamplerGlobal.peakThreadCount.map(peakThreadCount));
				sv.add(MxSamplerGlobal.totalStartedThreadCount.map(totalStartedThreadCount));
				sv.add(MxSamplerGlobal.daemonThreadCount.map(daemonThreadCount));

				model = MxSamplerGlobal.TYPE.newItem(sv);
			}
			for(final Map.Entry<MemoryPoolMXBean, MemoryUsage> entry : memoryUsage.entrySet())
			{
				final MemoryPoolMXBean pool = entry.getKey();
				final MemoryUsage collectionUsage = memoryCollectionUsage.get(pool);

				{
					final MemoryUsageLimit limit = properties.limitOld;
					if(limit!=null)
						limit.check(pool, collectionUsage);
				}
				{
					final MemoryUsageLimit limit = properties.limitPerm;
					if(limit!=null)
						limit.check(pool, collectionUsage);
				}

				sv.clear();
				sv.addAll(MxSamplerMemoryPool.map(model));
				sv.add(MxSamplerMemoryPool.name.map(MxSamplerMemoryPoolName.get(pool)));
				sv.add(MxSamplerMemoryPool.usage.map(MxSamplerMemoryUsage.create(entry.getValue())));
				sv.add(MxSamplerMemoryPool.collectionUsage.map(MxSamplerMemoryUsage.create(collectionUsage)));
				MxSamplerMemoryPool.TYPE.newItem(sv);
			}
			samplerModel.commit();
			return model;
		}
		finally
		{
			samplerModel.rollbackIfNotCommitted();
		}
	}

	public final void purge(final int days, final JobContext ctx)
	{
		if(days<=0)
			throw new IllegalArgumentException(String.valueOf(days));

		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.DATE, -days);
		purge(cal.getTime(), ctx);
	}

	public final void purge(final Date limit, final JobContext ctx)
	{
		final String samplerString = toString();
		try
		{
			for(final Type<?> type : samplerModel.getTypes())
				if(MxSamplerGlobal.TYPE!=type && // purge SamplerModel at the end
						MxSamplerMemoryPoolName.TYPE!=type && MxSamplerPurge.TYPE!=type)
				{
					MxSamplerPurge.purge(type, limit, ctx, samplerString);
				}

			MxSamplerPurge.purge(MxSamplerGlobal.TYPE, limit, ctx, samplerString);
		}
		catch (final SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
