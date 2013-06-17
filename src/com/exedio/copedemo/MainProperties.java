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

package com.exedio.copedemo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.mail.MessagingException;

import com.exedio.cope.util.Properties;
import com.exedio.copedemo.feature.sendmail.MailSenderProperties;
import com.exedio.copedemo.feature.util.PropertiesInstance;
import com.exedio.copedemo.misc.Hostname;
import com.exedio.mxsampler.MxSamplerProperties;
import com.exedio.sendmail.ErrorMailSource;
import com.exedio.sendmail.MailData;
import com.exedio.sendmail.MailSender;

public final class MainProperties extends Properties
{
	/**
	 * This field relies on
	 * {@link com.exedio.cope.servletutil.ServletProperties#create(javax.servlet.ServletContext)},
	 * which provides a key <tt>contextPath</tt> containing the result of
	 * {@link javax.servlet.ServletContext#getContextPath()}.
	 *
	 * Its used to customize other fields default values.
	 */
	private final String contextPath = value("contextPath", (String)null);

	public final MxSamplerProperties mxSampler = value("mxSampler", MxSamplerProperties.factory());


	// mail

	private final String mailFrom = value("mail.from", (String)null);

	public String getMailFrom()
	{
		return mailFrom;
	}


	// errorMail

	private final String errorMailTo = value("errorMail.to", (String)null);
	public final ErrorMailSource errorMailSource =
			new ErrorMailSource(
				errorMailTo,
				errorMailTo,
				"copedemo error (" + Hostname.get() + ')');


	// smtp

	public final MailSender smtp = value("smtp", MailSenderProperties.factory()).get();

	public void testSmtp() throws MessagingException
	{
		final String subject = "Properties#testMailSender copedemo (" + Hostname.get() + ") \u00e4";
		final MailData mail = new MailData(getMailFrom(), subject);
		mail.addTo(errorMailTo);
		mail.setTextPlain(subject);
		smtp.sendMail(mail);
	}

	private Callable<?> getSmtpTest()
	{
		return new Callable<Void>(){
			@Override public Void call() throws MessagingException
			{
				testSmtp();
				return null;
			}
			@Override public String toString()
			{
				return "smtp";
			}
		};
	}


	public final String adminRefererPrefix = value("admin.refererPrefix", "https://localhost:8443" + contextPath + "/");


	// common code

	public static Factory<MainProperties> factory()
	{
		return new Factory<MainProperties>()
		{
			@Override
			public MainProperties create(final Source source)
			{
				return new MainProperties(source);
			}
		};
	}

	private MainProperties(final Source source)
	{
		super(source);
	}

	@Override
	public List<? extends Callable<?>> getTests()
	{
		return Arrays.<Callable<?>>asList(
				getSmtpTest());
	}


	// instance

	public static final PropertiesInstance<MainProperties> instance = new PropertiesInstance<MainProperties>();

	public static final MainProperties get()
	{
		return instance.get();
	}
}
