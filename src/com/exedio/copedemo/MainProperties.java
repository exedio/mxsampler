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

package com.exedio.copedemo;

import com.exedio.cope.util.Properties;
import com.exedio.cope.util.PropertiesInstance;
import com.exedio.mxsampler.MemoryUsageLimit;
import com.exedio.mxsampler.MxSamplerProperties;

public final class MainProperties extends Properties
{
	/**
	 * This field relies on
	 * {@link com.exedio.cope.servletutil.ServletSource#create(javax.servlet.ServletContext)},
	 * which provides a key <tt>contextPath</tt> containing the result of
	 * {@link javax.servlet.ServletContext#getContextPath()}.
	 *
	 * Its used to customize other fields default values.
	 */
	private final String contextPath = value("contextPath", (String)null);

	public final MxSamplerProperties mxSampler = value("mxSampler", MxSamplerProperties.factory());


	// mail

	private final String mailFrom = value("mail.from", (String)null);

	public String getMailFrom()
	{
		return mailFrom;
	}


	// limit

	public final MemoryUsageLimit limitOld  = value("limit.old",  true, MemoryUsageLimit.factory("PS Old Gen" ));
	public final MemoryUsageLimit limitPerm = value("limit.perm", true, MemoryUsageLimit.factory("PS Perm Gen"));


	public final String adminRefererPrefix = value("admin.refererPrefix", "https://localhost:8443" + contextPath + "/");


	// common code

	private MainProperties(final Source source)
	{
		super(source);
	}


	// instance

	public static final PropertiesInstance<MainProperties> instance = PropertiesInstance.create(MainProperties::new);

	public static MainProperties get()
	{
		return instance.get();
	}
}
