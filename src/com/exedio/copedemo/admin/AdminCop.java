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

import com.exedio.cops.Cop;
import javax.servlet.http.HttpServletRequest;

abstract class AdminCop extends Cop
{
	AdminCop(final String pathInfo)
	{
		super(pathInfo);
	}

	abstract void post(HttpServletRequest request);
	abstract void writeBody(Out out, HttpServletRequest request);

	@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
	static final AdminCop getCop(final String pathInfo)
	{
		switch(pathInfo)
		{
			case '/' + LogCop.PATH_INFO:
				return new LogCop();
			case '/' + OutOfMemoryErrorCop.PATH_INFO:
				return new OutOfMemoryErrorCop();
			default:
				return new HomeCop();
		}
	}
}
