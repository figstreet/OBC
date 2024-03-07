package com.figstreet.core;

import java.io.StringReader;

import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.input.sax.XMLReaders;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * This class does not resolve entities or replace their placeholders.
 * Protects against XXE vulnerability http://www.jdom.org/docs/faq.html#a0350
 */
public class NoEntitySAXBuilder extends SAXBuilder
{
	public NoEntitySAXBuilder()
	{
		super(XMLReaders.NONVALIDATING);
		this.setExpandEntities(false);
		this.setEntityResolver(new NoOpEntityResolver());
	}

	public NoEntitySAXBuilder(boolean pValidate)
	{
		super(new XMLReaderSAX2Factory(pValidate));
		if (!pValidate)
		{
			this.setExpandEntities(false);
			this.setEntityResolver(new NoOpEntityResolver());
		}
	}

	public NoEntitySAXBuilder(String pSaxDriverClass, boolean pValidate)
	{
		super(new XMLReaderSAX2Factory(pValidate, pSaxDriverClass));
		if (!pValidate)
		{
			this.setExpandEntities(false);
			this.setEntityResolver(new NoOpEntityResolver());
		}
	}

	class NoOpEntityResolver implements EntityResolver
	{
		@Override
		public InputSource resolveEntity(String publicId, String systemId)
		{
			return new InputSource(new StringReader(""));
		}
	}
}
