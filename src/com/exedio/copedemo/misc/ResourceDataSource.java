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

package com.exedio.copedemo.misc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import com.exedio.cops.CopsServlet;
import com.exedio.cops.Resource;

public final class ResourceDataSource implements DataSource
{
	private final Class<? extends CopsServlet> servletClass;
	private final Resource resource;

	public ResourceDataSource(
			final Class<? extends CopsServlet> servletClass,
			final Resource resource)
	{
		if(servletClass==null)
			throw new NullPointerException();
		if(resource==null)
			throw new NullPointerException();

		this.servletClass = servletClass;
		this.resource = resource;
	}

	@Override
	public InputStream getInputStream()
	{
		return servletClass.getResourceAsStream(resource.getName());
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		throw new IOException("readonly");
	}

	@Override
	public String getContentType()
	{
		return resource.getContentType();
	}

	@Override
	public String getName()
	{
		return resource.getName();
	}
}
