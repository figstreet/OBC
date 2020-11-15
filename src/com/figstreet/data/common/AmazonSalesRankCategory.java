package com.figstreet.data.common;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class AmazonSalesRankCategory extends CodeConstant
{
	private static final long serialVersionUID = -5566505897714092540L;

	public static final Pattern DISPLAY_ON_WEBSITE_PATTERN = Pattern.compile("(.+)_display_on_website",
			Pattern.CASE_INSENSITIVE);
	public static final String DOW = "_d-o-w";

	public static final AmazonSalesRankCategory TOYS_AND_GAMES = new AmazonSalesRankCategory("toy_d-o-w",
			"Toys & Games");
	public static final AmazonSalesRankCategory GROCERY_AND_GOURMET_FOOD = new AmazonSalesRankCategory("grocery_d-o-w",
			"Grocery & Gourmet Food");

	private static class AmazonSalesRankCategoryFactory extends CodeConstantFactory<AmazonSalesRankCategory>
	{
		private static final String LOGGER_NAME = AmazonSalesRankCategoryFactory.class.getPackage().getName()
				+ ".AmazonSalesRankCategoryFactory";

		private AmazonSalesRankCategoryFactory()
		{
			super(AmazonSalesRankCategory.class, CodesType.AMAZON_SALESRANK_CATEGORY);
		}

		@Override
		public AmazonSalesRankCategory newInstance(String pValue, String pDisplay)
		{
			return new AmazonSalesRankCategory(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final AmazonSalesRankCategoryFactory fFactory = new AmazonSalesRankCategoryFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<AmazonSalesRankCategory> AMAZON_SALESRANK_CATEGORY_DESCRIPTOR = new CodeConstantTypeDescriptor<AmazonSalesRankCategory>(
			AmazonSalesRankCategory.class)
	{
		@Override
		public AmazonSalesRankCategory getValue(String pValue)
		{
			return fFactory.getValue(convertDisplayOnWebsite(pValue));
		}
	};

	public AmazonSalesRankCategory()
	{
		super(AMAZON_SALESRANK_CATEGORY_DESCRIPTOR);
	}

	private AmazonSalesRankCategory(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, AMAZON_SALESRANK_CATEGORY_DESCRIPTOR);
	}

	public static AmazonSalesRankCategory newInstance(String pValue)
	{
		return fFactory.getValue(convertDisplayOnWebsite(pValue), false, true);
	}

	public static Collection<AmazonSalesRankCategory> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}

	public static String convertDisplayOnWebsite(String pValue)
	{
		if (pValue == null)
			return null;
		Matcher matcher = DISPLAY_ON_WEBSITE_PATTERN.matcher(pValue);
		if (matcher.matches())
			pValue = matcher.group(1) + DOW;
		return pValue;
	}
}
