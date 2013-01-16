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

package com.exedio.copedemo.feature.util;

import com.exedio.cope.util.Properties;

public final class PropertiesInstance<P extends Properties>
{
	private volatile P value = null;

	/**
	 * @return Returns the value of the parameter <code>newInstance</code>.
	 */
	public P set(final P newValue)
	{
		if(newValue==null)
			throw new NullPointerException();

		value = newValue;
		return newValue;
	}

	public void remove()
	{
		value = null;
	}

	/**
	 * @throws IllegalStateException if no value has been set before.
	 */
	public P get()
	{
		final P result = value;
		if(result==null)
			throw new IllegalStateException();
		return result;
	}
}
