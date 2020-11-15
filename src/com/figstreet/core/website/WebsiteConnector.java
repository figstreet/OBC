package com.figstreet.core.website;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import com.figstreet.core.Logging;

public class WebsiteConnector
{
	private static final String LOGGER_NAME = WebsiteConnector.class.getPackage().getName() + ".WebsiteConnector";

	public static final int BUFFER_SIZE = 16 * 1024;
	public static final String CHAR_ENCODING = "UTF-8";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36";
	public static final String ACCEPT_LANGUAGE = "en-US";
	public static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String LOCATION_HEADER = "Location";

	private CloseableHttpClient fHttpClient;
	private int fHttpTimeout;
	private String fEntryPoint;

	public WebsiteConnector(String pEntryPoint, int pHttpTimeout)
	{
		this.fEntryPoint = pEntryPoint;
		this.fHttpTimeout = pHttpTimeout;
	}

	public String getEntryPoint()
	{
		return this.fEntryPoint;
	}

	public int getHttpTimeout()
	{
		return this.fHttpTimeout;
	}

	public void closeHttpClient()
	{
		try
		{
			if (this.fHttpClient != null)
				this.fHttpClient.close();
		}
		catch (IOException ioe)
		{
			Logging.warn(LOGGER_NAME, "closeHttpClient", "Error closing HttpClient");
		}
	}

	private CloseableHttpClient getHttpClient()
	{
		if (this.fHttpClient == null)
		{

			RequestConfig config = RequestConfig.custom().setSocketTimeout(this.fHttpTimeout)
					.setConnectTimeout(this.fHttpTimeout).setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();

		//	HttpHost proxy = new HttpHost("127.0.0.1", 8888);
			this.fHttpClient = HttpClients.custom()
					 //.setProxy(proxy)
					.setDefaultRequestConfig(config).setRetryHandler(new StandardHttpRequestRetryHandler(3, true))
					.setServiceUnavailableRetryStrategy(new ServiceUnavailableRetryStrategy()
					{
						@Override
						public boolean retryRequest(final HttpResponse response, final int executionCount,
								final HttpContext context)
						{
							int statusCode = response.getStatusLine().getStatusCode();
							return statusCode == 403 && executionCount < 5;
						}

						@Override
						public long getRetryInterval()
						{
							return 10000;
						}
					}).build();
		}
		return this.fHttpClient;
	}

	public WebsiteResponse connectToEntryPoint(File pResponseFile) throws WebFatalException
	{
		return this.performGet(this.fEntryPoint, pResponseFile, null);
	}

	private static String getResponseLocation(HttpResponse pResponse)
	{
		return findHeaderValue(pResponse, LOCATION_HEADER);
	}

	private static String getResponseContentType(HttpResponse pResponse)
	{
		return findHeaderValue(pResponse, "Content-Type");
	}

	private static String findHeaderValue(HttpResponse pResponse, String pHeaderName)
	{
		String value = null;
		if (pResponse != null && pHeaderName != null)
		{
			Header header = pResponse.getFirstHeader(pHeaderName);
			if (header != null)
				value = header.getValue();
		}
		return value;
	}

	private static boolean isResponsePdf(String pContentType)
	{
		return "application/pdf".equals(pContentType);
	}

	private static boolean isResponseText(String pContentType)
	{
		if (pContentType == null)
			return false;
		return pContentType.startsWith("text/plain");
	}

	private static boolean isResponseHtml(String pContentType)
	{
		if (pContentType == null)
			return false;
		return pContentType.startsWith("text/html");
	}

	private static boolean httpStatusCodeOK(int pStatusCode)
	{
		return HttpStatus.SC_OK == pStatusCode || HttpStatus.SC_MOVED_PERMANENTLY == pStatusCode
				|| HttpStatus.SC_MOVED_TEMPORARILY == pStatusCode || HttpStatus.SC_NOT_MODIFIED == pStatusCode;
	}

	public WebsiteResponse performGet(String pResource, File pResponse, String pReferer) throws WebFatalException
	{
		HttpGet method = new HttpGet(pResource);
		if (pReferer != null)
		{
			method.addHeader(HttpHeaders.REFERER, pReferer);
			method.addHeader(HttpHeaders.HOST, WebUtils.getHostNameFromURL(pReferer));
		}
		method.addHeader("Upgrade-Insecure-Requests", "1");
		method.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
		method.addHeader(HttpHeaders.ACCEPT_LANGUAGE, ACCEPT_LANGUAGE);
		method.addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");

		return this.performGet(method, pResponse);
	}

	public WebsiteResponse performGet(HttpGet pMethod, File pResponse) throws WebFatalException
	{
		String url = pMethod.getURI().toString();
		String errorMsg = null;
		File response = null;

		CloseableHttpClient client = this.getHttpClient();

		int httpStatusCode = -1;
		CloseableHttpResponse httpGetResponse = null;
		WebsiteResponse toReturn = null;
		try
		{
			httpGetResponse = client.execute(pMethod);
			httpStatusCode = httpGetResponse.getStatusLine().getStatusCode();
			toReturn = new WebsiteResponse(httpStatusCode, getResponseLocation(httpGetResponse));

			HttpEntity responseEntity = httpGetResponse.getEntity();
			if (responseEntity != null)
			{
				OutputStream outputStream = null;
				InputStream inputStream = null;
				try
				{
					String contentType = getResponseContentType(httpGetResponse);

					outputStream = new BufferedOutputStream(new FileOutputStream(pResponse));
					inputStream = responseEntity.getContent();
					int bytesRead;
					byte[] buf = new byte[BUFFER_SIZE];
					while ((bytesRead = inputStream.read(buf)) != -1)
					{
						outputStream.write(buf, 0, bytesRead);
					}

					outputStream.close();
					inputStream.close();

					if (isResponsePdf(contentType) && !WebUtils.isFilePDF(pResponse))
					{
						File pdfVersion = new File(pResponse.getParentFile(), pResponse.getName() + ".pdf");
						if (!pResponse.renameTo(pdfVersion))
							throw new WebFatalException(String.format("Failed to rename response from %s to %s",
									pResponse.getAbsolutePath(), pdfVersion.getAbsolutePath()));
						response = pdfVersion;
					}
					else if (isResponseText(contentType) && !WebUtils.isFileText(pResponse))
					{
						File textVersion = new File(pResponse.getParentFile(), pResponse.getName() + ".txt");
						if (!pResponse.renameTo(textVersion))
							throw new WebFatalException(String.format("Failed to rename response from %s to %s",
									pResponse.getAbsolutePath(), textVersion.getAbsolutePath()));
						response = textVersion;
					}
					else if (isResponseHtml(contentType) && !WebUtils.isFileHtml(pResponse))
					{
						File textVersion = new File(pResponse.getParentFile(), pResponse.getName() + ".html");
						if (!pResponse.renameTo(textVersion))
							throw new WebFatalException(String.format("Failed to rename response from %s to %s",
									pResponse.getAbsolutePath(), textVersion.getAbsolutePath()));
						response = textVersion;
					}
					else
						response = pResponse;

					toReturn.setResponseFile(response);
				}
				finally
				{
					if (outputStream != null)
						outputStream.close();

					if (inputStream != null)
						inputStream.close();
				}
			}
		}
		catch (IOException e)
		{
			errorMsg = String.format("IOException connecting to %s: %s", url, e.getMessage());
		}
		catch (Exception e)
		{
			errorMsg = String.format("Exception connecting to %s: %s", url, e.getMessage());
		}
		finally
		{
			pMethod.releaseConnection();
			try
			{
				if (httpGetResponse != null)
					httpGetResponse.close();
			}
			catch (IOException e)
			{
				Logging.warn(LOGGER_NAME, "performGet", "Error closing HttpResponse");
			}
		}

		if (!httpStatusCodeOK(httpStatusCode))
		{
			if (errorMsg == null)
				errorMsg = String.format("Non-OK status returned from %s: %d", url, httpStatusCode);

			throw new WebFatalException(errorMsg);
		}

		return toReturn;
	}


//	public static void main(String[] args) throws Exception
//	{
//		String url = "https://www.target.com";
//		File working = new File("/tempTarget");
//		working.mkdirs();
//
//		File homepage = File.createTempFile("homepage", ".html", working);
//		WebsiteConnector website = new WebsiteConnector(url, 10000);
//		WebsiteResponse response = website.connectToEntryPoint(homepage);
//		System.out.println("Http response connecting to " + url + ": " + response.getHttpStatusCode());
//		if (httpStatusCodeOK(response.getHttpStatusCode()))
//		{
//			File apiResponseFile = File.createTempFile("apiResponse_", ".json", working);
//			WebsiteResponse apiResponse = website.performGet(
//					"https://redsky.target.com/v2/plp/search/?category=5tg3d&default_purchasability_filter=true&pricing_store_id=1085&excludes=available_to_promise_qualitative%2Cavailable_to_promise_location_qualitative&key=ff457966e64d5e877fdbad070f276d18ecec4a01",
//					apiResponseFile, null); //Target server doesn't accept the Host header
//			System.out.println("API response saved to " + apiResponseFile.getAbsolutePath() + "; HTTP response: "
//					+ apiResponse.getHttpStatusCode());
//		}
//		website.closeHttpClient();
//
//	}

	public static void main(String[] args) throws Exception
	{
		String referer = "https://www.bedbathandbeyond.com";
		String url = "https://www.bedbathandbeyond.com";
		File working = new File("/tempBedBath");
		working.mkdirs();

		File homepage = File.createTempFile("homepage", ".html", working);
		WebsiteConnector website = new WebsiteConnector(url, 10000);
		WebsiteResponse homepageResponse = website.connectToEntryPoint(homepage);

		if (!httpStatusCodeOK(homepageResponse.getHttpStatusCode()))
		{
			System.out.println("URL: " + url + "; received non-OK HTTP response: " + homepageResponse.getHttpStatusCode() + "; results stored in " + homepage.getAbsolutePath());
		}

		url="https://www.bedbathandbeyond.com/apis/stateless/v1.0/navigation/category-navigation";
		File catalogNavigationFile = File.createTempFile("catalogNavigation", ".json", working);
		WebsiteResponse catalogNavigationResponse = website.performGet(url, catalogNavigationFile, referer);
		if (!httpStatusCodeOK(catalogNavigationResponse.getHttpStatusCode()))
		{
			System.out.println("URL: " + url + "; received non-OK HTTP response: " + catalogNavigationResponse.getHttpStatusCode() + "; results stored in " + catalogNavigationFile.getAbsolutePath());
		}

		url = "https://www.bedbathandbeyond.com/store/category/kitchen/kitchen-tools-gadgets/10617/";
		File kitchenGadgetsFile = File.createTempFile("kitchenGadgets", ".json", working);
		WebsiteResponse kitchenGadgetsResponse = website.performGet(url, kitchenGadgetsFile, referer);
		if (!httpStatusCodeOK(kitchenGadgetsResponse.getHttpStatusCode()))
		{
			System.out.println("URL: " + url + "; received non-OK HTTP response: " + kitchenGadgetsResponse.getHttpStatusCode() + "; results stored in " + kitchenGadgetsFile.getAbsolutePath());
		}

			File apiResponseFile = File.createTempFile("apiResponse_", ".json", working);
			WebsiteResponse apiResponse = website.performGet(
					"https://www.bedbathandbeyond.com/api/apps/bedbath/query/v1?start=0&perPage=48&view=grid&sws=&storeOnlyProducts=false&countOnly=false&storeId=207&facets=%7B%7D&noFacet=true&categoryId=10617&higherShippingThreshhold=0.1&site=BedBathUS&currencyCode=USD&country=US&solrCat=true&web3feo=true&sddAttribute=&rT=xtCompat&isBrowser=true",
					apiResponseFile, "https://www.bedbathandbeyond.com");
			System.out.println("API response saved to " + apiResponseFile.getAbsolutePath() + "; HTTP response: "
					+ apiResponse.getHttpStatusCode());

		website.closeHttpClient();

	}
}
