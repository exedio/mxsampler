/*
 * Copyright (C) 2004-2015  exedio GmbH (www.exedio.com)
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class LogbackFilter implements Filter
{
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
		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy
		response.setHeader("Content-Security-Policy",
				"default-src 'none'; " +
				"style-src 'unsafe-inline'; " +  // TODO get rid of unsafe-inline
				"frame-ancestors 'none'; " +
				"block-all-mixed-content; " +
				"base-uri 'none'");

		// Do not leak information to external servers, not even the (typically private) hostname.
		// We need the referer within the servlet, because typically there is a StrictRefererValidationFilter.
		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Referrer-Policy
		response.setHeader("Referrer-Policy", "same-origin");

		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options
		response.setHeader("X-Content-Type-Options", "nosniff");

		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options
		response.setHeader("X-Frame-Options", "deny");

		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection
		response.setHeader("X-XSS-Protection", "1; mode=block");

		chain.doFilter(request, response);
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
