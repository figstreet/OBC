package com.figstreet.data.common;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class AmazonMarketplace extends CodeConstant
{
	private static final long serialVersionUID = 2956922990695695144L;
	public static final AmazonMarketplace UNITED_STATES = new AmazonMarketplace("ATVPDKIKX0DER", "United States");

	private static class AmazonMarketplaceFactory extends CodeConstantFactory<AmazonMarketplace>
	{
		private static final String LOGGER_NAME = AmazonMarketplaceFactory.class.getPackage().getName()
				+ ".AmazonMarketplaceFactory";

		private AmazonMarketplaceFactory()
		{
			super(AmazonMarketplace.class, CodesType.AMAZON_MARKETPLACE);
		}

		@Override
		public AmazonMarketplace newInstance(String pValue, String pDisplay)
		{
			return new AmazonMarketplace(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final AmazonMarketplaceFactory fFactory = new AmazonMarketplaceFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<AmazonMarketplace> AMAZON_MARKETPLACE_DESCRIPTOR = new CodeConstantTypeDescriptor<AmazonMarketplace>(
			AmazonMarketplace.class)
	{
		@Override
		public AmazonMarketplace getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public AmazonMarketplace()
	{
		super(AMAZON_MARKETPLACE_DESCRIPTOR);
	}

	private AmazonMarketplace(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, AMAZON_MARKETPLACE_DESCRIPTOR);
	}

	public static AmazonMarketplace newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<AmazonMarketplace> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}

}
