/*
 * Copyright (C) 2004-2015  exedio GmbH (www.exedio.com)
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

package com.exedio.cope.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class CopeAssert
{
	public static void assertContainsList(final List<?> expected, final Collection<?> actual)
	{
		if(expected==null && actual==null)
			return;

		assertNotNull("expected null, but was " + actual, expected);
		assertNotNull("expected " + expected + ", but was null", actual);

		if(expected.size()!=actual.size() ||
				!expected.containsAll(actual) ||
				!actual.containsAll(expected))
			fail("expected "+expected+", but was "+actual);
	}

	public static void assertContains(final Collection<?> actual)
	{
		assertContainsList(Collections.emptyList(), actual);
	}

	public static void assertContains(final Object o, final Collection<?> actual)
	{
		assertContainsList(Collections.singletonList(o), actual);
	}

	public static void assertContains(final Object o1, final Object o2, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2), actual);
	}

	public static void assertContains(final Object o1, final Object o2, final Object o3, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3), actual);
	}

	public static void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4), actual);
	}

	public static void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Object o5, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5), actual);
	}

	public static void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6), actual);
	}

	public static void assertContains(final Object o1, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Collection<?> actual)
	{
		assertContainsList(Arrays.asList(o1, o2, o3, o4, o5, o6, o7), actual);
	}

	private static final String DATE_FORMAT_FULL = "dd.MM.yyyy HH:mm:ss.SSS";

	public static void assertWithin(final Date expectedBefore, final Date expectedAfter, final Date actual)
	{
		final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_FULL, Locale.ENGLISH);
		final String message =
			"expected date within " + df.format(expectedBefore) +
			" and " + df.format(expectedAfter) +
			", but was " + df.format(actual);

		assertTrue(message, !expectedBefore.after(actual));
		assertTrue(message, !expectedAfter.before(actual));
	}

	/**
	 * This method will not return until the result of System.currentTimeMillis() has increased
	 * by the given amount of milli seconds.
	 */
	public static void sleepLongerThan(final long millis) throws InterruptedException
	{
		final long start = System.currentTimeMillis();
		// The loop double-checks that currentTimeMillis() really returns a sufficiently higher
		// value ... needed for Windows.
		do
		{
			//noinspection BusyWait
			Thread.sleep(millis+1);
		}
		while((System.currentTimeMillis()-start)<=millis);
	}

	private CopeAssert()
	{
		// prevent instantiation
	}
}
