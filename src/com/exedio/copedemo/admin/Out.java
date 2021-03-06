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

import static java.nio.charset.StandardCharsets.UTF_8;

import com.exedio.cope.util.XMLEncoder;
import com.exedio.cops.BodySender;
import com.exedio.cops.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

final class Out
{
	private StringBuilder bf = new StringBuilder();
	private final HttpServletRequest request;

	Out(final HttpServletRequest request)
	{
		assert request !=null;
		this.request = request;
	}

	void writeStatic(final String s)
	{
		bf.append(s);
	}

	void write(final String s)
	{
		XMLEncoder.append(bf, s);
	}

	void write(final int i)
	{
		bf.append(i);
	}

	void write(final Charset charset)
	{
		bf.append(charset.name());
	}

	void write(final Resource resource)
	{
		bf.append(resource.getURL(request)); // resource urls do not contain special xml characters
	}

	void write(final AdminCop cop)
	{
		write(cop.getURL(request)); // AdminServlet does not use sessions
	}

	void sendBody(final HttpServletResponse response) throws IOException
	{
		final StringBuilder bf = this.bf;
		if(bf==null)
			throw new IllegalStateException();
		this.bf = null; // prevent this instance to be used anymore

		BodySender.send(response, bf, UTF_8);
	}
}
