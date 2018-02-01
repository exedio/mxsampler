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

import com.exedio.cope.util.Properties.Field;
import com.exedio.cope.util.Sources;
import com.exedio.copedemo.MainProperties;
import junit.framework.TestCase;

/**
 * This tests makes sure, that no properties are changed by accident.
 * Adapt if necessary.
 */
public final class MainPropertiesTest extends TestCase
{
	public void testIt()
	{
		final MainProperties p = MainProperties.factory().create(
				Sources.load(getClass().getResource("mainPropertiesTest.properties")));

		for(final Field field : p.getFields())
		{
			final String key = field.getKey();
			if(!key.startsWith("mxSampler.cope.") &&
				!key.startsWith("smtp."))
				assertTrue(key, field.isSpecified());
		}

		p.ensureValidity();
	}
}
