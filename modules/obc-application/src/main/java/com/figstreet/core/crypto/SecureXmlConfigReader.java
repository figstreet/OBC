package com.figstreet.core.crypto;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.figstreet.core.Logging;
import com.figstreet.core.NoEntitySAXBuilder;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class SecureXmlConfigReader
{
	private long fConfigLastModified;
	private String fSHA256Hash;
	private HashMap<String, ArrayList<char[]>> fElements;
	private HashMap<String, ArrayList<char[]>> fSecureElements;

	public SecureXmlConfigReader(File pFile) throws NoSuchAlgorithmException, IOException
	{
		this.initConfig(pFile);
		this.fConfigLastModified = pFile.lastModified();
		this.fSHA256Hash = HashingCrypto.fileHashSHA256(pFile, CryptoUtil.EncodingType.BASE64);
	}

	protected String getLoggerName()
	{
		return LOGGER_NAME;
	}

	public long getConfigLastModified()
	{
		return this.fConfigLastModified;
	}

	public boolean isConfigOutOfDate(File pConfigFile) throws NoSuchAlgorithmException, IOException
	{
		if (pConfigFile != null && pConfigFile.canRead())
		{
			if (pConfigFile.lastModified() != this.fConfigLastModified)
			{
				if (this.fSHA256Hash == null)
					return true;

				String currentHash = HashingCrypto.fileHashSHA256(pConfigFile, CryptoUtil.EncodingType.BASE64);
				if (this.fSHA256Hash.equals(currentHash))
					this.fConfigLastModified = pConfigFile.lastModified();
				else
					return true;
			}
		}
		return false;
	}



	public void clearSecureElements()
	{
		if (this.fSecureElements != null)
		{
			for (ArrayList<char[]> secureElements : this.fSecureElements.values())
			{
				if (secureElements != null)
				{
					for (char[] secureElement : secureElements)
					{
						CryptoUtil.clearArray(secureElement);
					}
				}
			}
			this.fSecureElements.clear();
		}
	}

	public char[] getValue(String pChildName)
	{
		List<char[]> values = this.getValues(pChildName);
		if (!values.isEmpty())
			return values.get(0);
		return null;
	}

	public String getValueStr(String pChildName)
	{
		char[] value = this.getValue(pChildName);
		if (value == null || value.length < 1)
			return null;
		return new String(value);
	}

	public List<char[]> getValues(String pChildName)
	{
		ArrayList<char[]> values = new ArrayList<char[]>();
		boolean found = false;
		if (this.fElements != null)
		{
			ArrayList<char[]> nonSecureElements = this.fElements.get(pChildName);
			if (nonSecureElements != null)
			{
				found = true;
				for (char[] nonSecureElement : nonSecureElements)
				{
					values.add(Arrays.copyOf(nonSecureElement, nonSecureElement.length));
				}
			}
		}

		if (!found && this.fSecureElements != null)
		{
			ArrayList<char[]> secureElements = this.fSecureElements.get(pChildName);
			if (secureElements != null)
			{
				for (char[] secureElement : secureElements)
				{
					values.add(Arrays.copyOf(secureElement, secureElement.length));
				}
			}
		}

		return values;
	}

	private void initConfig(File pFile)
	{
		Document doc = null;
		try
		{
			doc = new NoEntitySAXBuilder().build(pFile);
			Element root = doc.getRootElement();

			this.fElements = new HashMap<String, ArrayList<char[]>>();
			this.fSecureElements = new HashMap<String, ArrayList<char[]>>();

			for (Element child : root.getChildren())
			{
				String text = child.getText();
				if (text != null)
				{
					HashMap<String, ArrayList<char[]>> elementMap;
					Attribute secureAttribute = child.getAttribute("secure");
					if (secureAttribute != null && secureAttribute.getBooleanValue())
						elementMap = this.fSecureElements;
					else
						elementMap = this.fElements;

					ArrayList<char[]> elementList = elementMap.get(child.getName());
					if (elementList == null)
					{
						elementList = new ArrayList<char[]>();
						elementMap.put(child.getName(), elementList);
					}

					elementList.add(text.toCharArray());
				}
			}
		}
		catch (Exception e)
		{
			Logging.error(this.getLoggerName(), "initConfig", e);
			e.printStackTrace(System.err);
		}
		finally
		{
			if (doc != null)
			{
				doc.removeContent();
				doc = null;
			}
		}
	}

	private static final String LOGGER_NAME = SecureXmlConfigReader.class.getPackage().getName() + ".SecureXmlConfigReader";
}
