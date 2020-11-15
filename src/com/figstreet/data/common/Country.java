package com.figstreet.data.common;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class Country extends CodeConstant
{
	private static final long serialVersionUID = -692235741782417668L;
	public static final Country CANADA = new Country("CA", "Canada");
	public static final Country US = new Country("US", "United States");

	private static class CountryFactory extends CodeConstantFactory<Country>
	{
		private static final String LOGGER_NAME = CountryFactory.class.getPackage().getName() + ".CountryFactory";

		private CountryFactory()
		{
			super(Country.class, CodesType.COUNTRY);
		}

		@Override
		public Country newInstance(String pValue, String pDisplay)
		{
			return new Country(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final CountryFactory fFactory = new CountryFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<Country> COUNTRY_DESCRIPTOR = new CodeConstantTypeDescriptor<Country>(
			Country.class)
	{
		@Override
		public Country getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public Country()
	{
		super(COUNTRY_DESCRIPTOR);
	}

	private Country(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, COUNTRY_DESCRIPTOR);
	}

	public static Country newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<Country> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}

}
