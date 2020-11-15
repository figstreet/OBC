package com.figstreet.data.common;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.figstreet.core.Logging;
import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class Region extends CodeConstant
{
	private static final long serialVersionUID = 7532367380356034479L;

	private static class RegionFactory extends CodeConstantFactory<Region>
	{
		private static final String LOGGER_NAME = RegionFactory.class.getPackage().getName() + ".RegionFactory";
		private static Map<Country, Set<Region>> fCountryMap;

		private RegionFactory()
		{
			super(Region.class, CodesType.REGION);
		}

		@Override
		public Region newInstance(String pValue, String pDisplay)
		{
			return new Region(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}

		@Override
		protected void populateAllValues()
		{
			super.populateAllValues();
			Collection<Region> allValues = super.getAllValues();
			Map<Country, Set<Region>> countryMap = new LinkedHashMap<>();
			for (Region region : allValues)
			{
				Country country = region.getCountry();
				if (country != null)
				{
					Set<Region> forCountry = countryMap.get(country);
					if (forCountry == null)
					{
						forCountry = new LinkedHashSet<>();
						countryMap.put(country, forCountry);
					}
					forCountry.add(region);
				}
			}

			for (Map.Entry<Country, Set<Region>> entry : countryMap.entrySet())
			{
				Country country = entry.getKey();
				Set<Region> regions = entry.getValue();
				countryMap.put(country, Collections.unmodifiableSet(regions));
			}

			fCountryMap = Collections.unmodifiableMap(countryMap);
		}
	}

	private static final String LOGGER_NAME = Region.class.getPackage().getName() + ".Region";
	public static final String DELIMITER = "~";

	private static final RegionFactory fFactory = new RegionFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<Region> REGION_DESCRIPTOR = new CodeConstantTypeDescriptor<Region>(
			Region.class)
	{
		@Override
		public Region getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	private String fCountryValue;
	private String fRegionValue;

	public Region()
	{
		super(REGION_DESCRIPTOR);
	}

	private Region(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, REGION_DESCRIPTOR);
		String[] elements = pValue.split(DELIMITER);
		if (elements.length == 2)
		{
			this.fCountryValue = elements[0];
			this.fRegionValue = elements[1];
		}
		else
		{
			Logging.error(LOGGER_NAME, "ctor", "The value " + pValue + " did not contain two elements split by "
					+ DELIMITER + "; using entire value as the region and leaving country null");
			this.fRegionValue = pValue;
		}
	}

	public Country getCountry()
	{
		if (this.fCountryValue == null)
			return null;
		return Country.newInstance(this.fCountryValue);
	}

	public String getCountryValue()
	{
		return this.fCountryValue;
	}

	public String getRegionValue()
	{
		return this.fRegionValue;
	}

	public static Region newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Set<Region> getRegionsForCountry(Country pCountry)
	{
		return RegionFactory.fCountryMap.get(pCountry);
	}
}
