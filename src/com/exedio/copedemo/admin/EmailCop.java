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

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.exedio.copedemo.MainProperties;
import com.exedio.copedemo.misc.Hostname;
import com.exedio.sendmail.MailData;

final class EmailCop extends AdminCop
{
	static final String PATH_INFO = "email.html";

	static final String ADDRESS    = "test.email.address";

	EmailCop()
	{
		super(PATH_INFO);
	}

	private String address = null;

	@Override
	protected void post(
			final HttpServletRequest request)
	{
		address = request.getParameter(ADDRESS);
		if(address!=null)
		{
			try
			{
				final MainProperties properties = MainProperties.get();
				final MailData mail = new MailData(
						properties.getMailFrom(),
						"test mail for cope mxsampler (" + Hostname.get() + ") \u00e4");
				mail.addTo(address);
				mail.setTextPlain("test mail for cope mxsampler \u00e4");
				properties.smtp.sendMail(mail);
			}
			catch(final MessagingException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	void writeBody(final Out out, final HttpServletRequest request)
	{
		Email_Jspm.writeBody(out, address);
	}
}
