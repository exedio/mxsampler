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

package com.exedio.copedemo.admin;

import javax.servlet.http.HttpServletRequest;

final class OutOfMemoryErrorCop extends AdminCop
{
	static final String PATH_INFO = "outOfMemoryError.html";

	static final String LOG_OUT  = "logOut";
	static final String LOG_ERR  = "logErr";
	static final String THROW    = "throw";
	static final String ALLOCATE = "allocate";

	OutOfMemoryErrorCop()
	{
		super(PATH_INFO);
	}

	@Override
	protected void post(
			final HttpServletRequest request)
	{
		if(request.getParameter(LOG_OUT)!=null)
			System.out.println(OutOfMemoryError.class.getName());
		if(request.getParameter(LOG_ERR)!=null)
			System.err.println(OutOfMemoryError.class.getName());
		if(request.getParameter(THROW)!=null)
			throw new OutOfMemoryError();
		if(request.getParameter(ALLOCATE)!=null)
		{
			final double[] b = new double[Integer.MAX_VALUE];
			System.out.println("Allocated " + b.length + " doubles.");
		}
	}

	@Override
	void writeBody(final Out out, final HttpServletRequest request)
	{
		OutOfMemoryError_Jspm.writeBody(out);
	}
}
