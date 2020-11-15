package com.figstreet.data.common;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class PriceCurrency extends CodeConstant
{
	private static final long serialVersionUID = -607727184769750707L;
	public static final PriceCurrency USD = new PriceCurrency("USD", "United States Dollar");

	private static class PriceCurrencyFactory extends CodeConstantFactory<PriceCurrency>
	{
		private static final String LOGGER_NAME = PriceCurrencyFactory.class.getPackage().getName()
				+ ".PriceCurrencyFactory";

		private PriceCurrencyFactory()
		{
			super(PriceCurrency.class, CodesType.PRICE_CURRENCY);
		}

		@Override
		public PriceCurrency newInstance(String pValue, String pDisplay)
		{
			return new PriceCurrency(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final PriceCurrencyFactory fFactory = new PriceCurrencyFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<PriceCurrency> PRICE_CURRENCY_DESCRIPTOR = new CodeConstantTypeDescriptor<PriceCurrency>(
			PriceCurrency.class)
	{
		@Override
		public PriceCurrency getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public PriceCurrency()
	{
		super(PRICE_CURRENCY_DESCRIPTOR);
	}

	private PriceCurrency(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, PRICE_CURRENCY_DESCRIPTOR);
	}

	public static PriceCurrency newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<PriceCurrency> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}
}