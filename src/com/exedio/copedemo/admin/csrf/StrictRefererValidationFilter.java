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

package com.exedio.copedemo.admin.csrf;

import com.exedio.copedemo.MainProperties;
import com.exedio.cops.Cop;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class StrictRefererValidationFilter implements Filter
{
	private static final Logger logger = Logger.getLogger(StrictRefererValidationFilter.class.getName());

	@Override
	@SuppressFBWarnings("BC_UNCONFIRMED_CAST")
	public void doFilter(
			final ServletRequest request,
			final ServletResponse response,
			final FilterChain chain)
	throws IOException, ServletException
	{
		doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}

	private static void doFilter(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final FilterChain chain)
	throws IOException, ServletException
	{
		if(!Cop.isPost(request))
		{
			chain.doFilter(request, response);
			return;
		}

		final String referer = request.getHeader("Referer");
		if(referer!=null && referer.startsWith(MainProperties.get().adminRefererPrefix))
		{
			chain.doFilter(request, response);
			return;
		}

		if(logger.isLoggable(Level.WARNING))
		{
			final StringBuilder parameters = new StringBuilder();
			for(final Enumeration<?> e = request.getParameterNames(); e.hasMoreElements(); )
			{
				parameters.append('{');
				final String name = (String)e.nextElement();
				parameters.append(name);
				parameters.append('=');
				boolean first = true;
				for(final String value : request.getParameterValues(name))
				{
					if(first)
						first = false;
					else
						parameters.append(',');

					parameters.append(value);
				}
				parameters.append('}');
			}
			logger.log(
					Level.WARNING,
					"rejected potential Cross Site Request Forgery, path={0}, referer={1}, parameters={2}",
					new Object[]{
							request.getContextPath() + request.getServletPath() + request.getPathInfo(),
							referer,
							parameters});
		}
		response.sendError(
				HttpServletResponse.SC_BAD_REQUEST,
				"Bad Request, potential CSRF");
	}

	@Override
	public void init(final FilterConfig config)
	{
		// empty
	}

	@Override
	public void destroy()
	{
		// empty
	}
}
