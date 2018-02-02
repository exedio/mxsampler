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

package com.exedio.copedemo.cronjob;

import com.exedio.cope.Model;
import com.exedio.cope.misc.ConnectToken;
import com.exedio.mxsampler.MxSampler;

/**
 * Must be public to allow access to field model by reflection.
 */
public abstract class MxSamplerJob extends AbstractJob
{
	protected static final MxSampler sampler = new MxSampler();

	/**
	 * Must be public to allow access by reflection.
	 */
	public static final Model model = sampler.getModel();

	static
	{
		model.enableSerialization(MxSamplerJob.class, "model");
	}

	private ConnectToken connectToken = null;

	@Override
	public final void init()
	{
		super.init();

		connectToken = sampler.connect("cronjob " + getName());
		// DO NOT WRITE ANYTHING HERE
		// OTHERWISE ConnectTokens MAY BE LOST
	}

	@Override
	public void destroy()
	{
		if(connectToken!=null)
		{
			connectToken.returnItConditionally();
			connectToken = null;
		}

		super.destroy();
	}
}
