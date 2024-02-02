package com.figstreet.core.website;

import java.io.File;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtils
{
	private static final HashSet<String> XML_MIMETYPES = new HashSet<String>();
	private static final HashSet<String> PDF_MIMETYPES = new HashSet<String>();
	private static final HashSet<String> TEXT_MIMETYPES = new HashSet<String>();
	private static final HashSet<String> HTML_MIMETYPES = new HashSet<String>();
	private static final Random RANDOM;
	static
	{
		RANDOM = new Random();
		RANDOM.setSeed(System.currentTimeMillis());
		XML_MIMETYPES.add("application/xml");
		PDF_MIMETYPES.add("application/pdf");
		HTML_MIMETYPES.add("text/html");
		TEXT_MIMETYPES.add("text/plain");
	}
	private static final FileNameMap MIMETYPE_MAP = URLConnection.getFileNameMap();

	private static final Pattern URL_HOST_PATTERN = Pattern.compile("http[s]{0,1}://(.+)/{0,1}.*", Pattern.CASE_INSENSITIVE);


	protected static String getMimeType(File pFile)
	{
		if (pFile == null)
			return null;
		return MIMETYPE_MAP.getContentTypeFor(pFile.getName());
	}

	protected static boolean isEmpty(String pValue)
	{
		if (pValue == null || pValue.trim().length() == 0)
			return true;
		return false;
	}

	public static boolean isFileText(File pFile)
	{
		if (pFile == null)
			return false;
		return TEXT_MIMETYPES.contains(getMimeType(pFile));
	}

	public static boolean isFileHtml(File pFile)
	{
		if (pFile == null)
			return false;
		return HTML_MIMETYPES.contains(getMimeType(pFile));
	}

	public static boolean isFileXML(File pFile)
	{
		if (pFile == null)
			return false;
		return XML_MIMETYPES.contains(getMimeType(pFile));
	}

	public static boolean isFilePDF(File pFile)
	{
		if (pFile == null)
			return false;
		return PDF_MIMETYPES.contains(getMimeType(pFile));
	}

	public static File generateUniqueFile(String pParentDir, String pPrefix, String pExtension,
		boolean pAlwaysAppendRandom)
	{
		File toReturn = new File(pParentDir, pPrefix + "_" + RANDOM.nextInt(1000000) + pExtension);
		if (!pAlwaysAppendRandom)
			toReturn = new File(pParentDir, pPrefix + pExtension);
		int i = 0;
		while (i < 5 && toReturn.exists())
		{
			i = i + 1;
			toReturn = new File(pParentDir + File.separator + pPrefix + "_" + RANDOM.nextInt(1000000) + pExtension);
		}
		if (toReturn.exists())
		{
			toReturn = null;
		}
		return toReturn;
	}


	public static String getHostFromURL(URL pUrl)
	{
		if (pUrl == null)
			return null;

		StringBuilder sb = new StringBuilder();
		sb.append(pUrl.getProtocol());
		sb.append("://");
		sb.append(pUrl.getHost());
		sb.append("/");
		return sb.toString();
	}

	public static String getHostNameFromURL(String pResource)
	{
		if (pResource == null)
			return null;

		Matcher matcher = URL_HOST_PATTERN.matcher(pResource);
		if (matcher.matches())
			return matcher.group(1);

		return null;
	}

	private static String getServerPathFromURL(URL pUrl)
	{
		if (pUrl == null)
			return null;

		return getServerPath(pUrl.toExternalForm());
	}

	private static String getServerPath(String pServerPath)
	{
		if (pServerPath == null)
			return null;

		StringBuilder sb = new StringBuilder(pServerPath);
		int lastSlash = sb.lastIndexOf("/");
		if (lastSlash >= 0 && lastSlash > 7)
			return sb.substring(0, (lastSlash+1));
		sb.append("/");
		return sb.toString();
	}

	public static String getRemoteFileLocation(URL pSourcePageUrl, String pSource)
	{
		if (pSourcePageUrl == null || pSource == null)
			return null;
		if (pSource.startsWith("http"))
			return pSource;

		String serverPath = getHostFromURL(pSourcePageUrl);
		if (pSource.startsWith("/"))
			pSource = pSource.substring(1);
		else
		{
			serverPath = getServerPathFromURL(pSourcePageUrl);
			if (pSource.startsWith("../"))
			{
				while (pSource.startsWith("../"))
				{
					pSource = pSource.substring(3);
					serverPath = getServerPath(serverPath.substring(0, serverPath.length()-1));
				}
			}
			else if (pSource.startsWith("./"))
				pSource = pSource.substring(2);
		}
		return serverPath + pSource;
	}

	public static String getRemoteFileName(String pSource)
	{
		if (pSource == null)
			return null;

		int lastSlash = pSource.lastIndexOf("/");
		if (lastSlash < 0)
			return pSource;
		return pSource.substring(lastSlash);
	}

}
