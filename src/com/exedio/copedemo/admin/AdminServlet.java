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
import com.exedio.cops.CopsServlet;
import com.exedio.cops.Resource;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class AdminServlet extends CopsServlet
{
	private static final long serialVersionUID = 1l;

	static final Resource stylesheet = new Resource("admin.css");
	static final Resource logo = new Resource("logo.png");

	@Override
	protected void doRequest(
			final HttpServletRequest request,
			final HttpServletResponse response)
		throws IOException
	{
		//System.out.println("request ---" + request.getMethod() + "---" + request.getContextPath() + "---" + request.getServletPath() + "---" + request.getPathInfo() + "---" + request.getQueryString() + "---");

		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy
		response.setHeader("Content-Security-Policy",
				"default-src 'none'; " +
				"style-src 'self'; " +
				"script-src 'unsafe-inline'; " + // TODO get rid of unsafe-inline
				"img-src 'self'; " +
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

		final AdminCop cop = AdminCop.getCop(request.getPathInfo());
		if(Cop.isPost(request))
		{
			cop.post(request);
		}

		final Out out = new Out(request);
		Admin_Jspm.write(out, cop, request, getServletContext().getServletContextName());
		out.sendBody(response);
	}
}
