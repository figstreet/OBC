package com.figstreet.data.history;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class HistoryType extends CodeConstant
{
	private static final long serialVersionUID = -4669180682776654242L;

	public static final HistoryType PRODUCT_HISTORY = new HistoryType("PRODUCT", "Product");
	public static final HistoryType VENDOR_HISTORY = new HistoryType("VENDOR", "Vendor");
	public static final HistoryType VENDOR_ADDRESS_HISTORY = new HistoryType("VENDOR_ADDR", "Vendor Address");
	public static final HistoryType VENDOR_CONTACT_HISTORY = new HistoryType("VENDOR_CONT", "Vendor Contact");
	public static final HistoryType VENDOR_PRODUCT_HISTORY = new HistoryType("VENDOR_PROD", "Vendor Product");
	public static final HistoryType AMAZON_PRICING_HISTORY = new HistoryType("AZ_PRICING", "Amazon Pricing");
	public static final HistoryType AMAZON_SALES_RANK_HISTORY = new HistoryType("AZ_SALES_RANK", "Amazon Sales Rank");

	private static class HistoryTypeFactory extends CodeConstantFactory<HistoryType>
	{
		private static final String LOGGER_NAME = HistoryTypeFactory.class.getPackage().getName()
				+ ".HistoryTypeFactory";

		private HistoryTypeFactory()
		{
			super(HistoryType.class, CodesType.HISTORY_TYPE);
		}

		@Override
		public HistoryType newInstance(String pValue, String pDisplay)
		{
			return new HistoryType(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final HistoryTypeFactory fFactory = new HistoryTypeFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<HistoryType> HISTORYTYPE_DESCRIPTOR = new CodeConstantTypeDescriptor<HistoryType>(
			HistoryType.class)
	{
		@Override
		public HistoryType getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public HistoryType()
	{
		super(HISTORYTYPE_DESCRIPTOR);
	}

	private HistoryType(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, HISTORYTYPE_DESCRIPTOR);
	}

	public static HistoryType newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<HistoryType> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}

}
