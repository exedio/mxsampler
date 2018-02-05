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

import com.exedio.cope.ConnectProperties;
import com.exedio.cope.Model;
import com.exedio.cope.misc.ConnectToken;
import com.exedio.cope.util.JobContext;
import com.exedio.cope.util.Properties;

public final class MxSamplerProperties extends Properties
{
	// cope

	private final ConnectProperties cope = value("cope", mask(ConnectProperties.factory()));

	private static Factory<ConnectProperties> mask(final Factory<ConnectProperties> original)
	{
		return source ->
		{
			// TODO deprecate MxSampler.maskConnectSource when moved into framework
			return original.create(MxSampler.maskConnectSource(source));
		};
	}

	// TODO remove, used for tests only
	ConnectProperties getCope()
	{
		return cope;
	}

	public void setProperties(final Model model)
	{
		ConnectToken.setProperties(model, cope);
	}

	@Probe String probeCope()
	{
		return cope.probe();
	}


	// purge

	private final int purgeDays = value("purgeDays", 8, 0); // amply one week

	public void purge(final MxSampler sampler, final JobContext ctx)
	{
		if(purgeDays>0)
			sampler.purge(purgeDays, ctx);
	}


	// common code

	public static Factory<MxSamplerProperties> factory()
	{
		return source -> new MxSamplerProperties(source);
	}

	private MxSamplerProperties(final Source source)
	{
		super(source);
	}
}
