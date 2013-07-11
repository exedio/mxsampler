/*
 * Copyright (C) 2004-2012  exedio GmbH (www.exedio.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.exedio.mxsampler;

import static com.exedio.mxsampler.Stuff.samplerModel;

import java.io.File;

import com.exedio.cope.ConnectProperties;
import com.exedio.cope.junit.CopeModelTest;
import com.exedio.cope.util.Properties.Source;
import com.exedio.cope.util.Sources;
import com.exedio.copedemo.MainProperties;

public class ConnectedTest extends CopeModelTest
{
	ConnectedTest()
	{
		super(samplerModel);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		MainProperties.instance.set(getMainProperties());
	}

	@Override
	protected void tearDown() throws Exception
	{
		MainProperties.instance.remove();
		super.tearDown();
	}

	@Override
	public ConnectProperties getConnectProperties()
	{
		return getMainProperties().mxSampler.getCope();
	}

	private static MainProperties getMainProperties()
	{
		return MainProperties.factory().create(getPropertiesSource());
	}

	private static Source getPropertiesSource()
	{
		String filename = System.getProperty("com.exedio.copedemo.properties");
		if(filename==null)
			filename = "test.properties";
		return Sources.load(new File(filename));
	}

	@Override
	protected boolean doesManageTransactions()
	{
		return false;
	}
}
