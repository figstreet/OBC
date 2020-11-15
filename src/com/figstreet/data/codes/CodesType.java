package com.figstreet.data.codes;

import java.util.List;

import com.figstreet.core.TypeConstant;
import com.figstreet.core.TypeConstantTypeDescriptor;

public class CodesType extends TypeConstant
{
	private static final long serialVersionUID = -1695304553308157536L;

	public static final CodesType MASTER_CODES = new CodesType("00", "Master Codes");
	public static final CodesType VENDOR_ADDRESS_TYPE = new CodesType("VAT", "Vendor Address Type");
	public static final CodesType VENDOR_CONTACT_TYPE = new CodesType("VCT", "Vendor Contact Type");
	public static final CodesType HISTORY_TYPE = new CodesType("HT", "History Type");
	public static final CodesType COUNTRY = new CodesType("COUNTRY", "Countries");
	public static final CodesType REGION = new CodesType("REGION", "Regions (States)");
	public static final CodesType USER_PERMISSION_TYPE = new CodesType("UPT", "User Permission Type");
	public static final CodesType AMAZON_MARKETPLACE = new CodesType("AZM", "Amazon Marketplace");
	public static final CodesType AMAZON_SALESRANK_CATEGORY = new CodesType("AZSRC", "Amazon SalesRank Category");
	public static final CodesType PRICE_CURRENCY = new CodesType("PCUR", "Price Currency");
	public static final CodesType PRODUCT_CONDITION  = new CodesType("PCON", "Produce Condition");
	public static final CodesType MEASUREMENT_UNIT = new CodesType("MU", "Measurement Unit");
	
	public static class CodesTypeFactory extends TypeConstantFactory<CodesType>
	{
		private static final String LOGGER_NAME = CodesTypeFactory.class.getPackage().getName() + ".CodesTypeFactory";

		protected CodesTypeFactory()
		{
			super(CodesType.class);
		}

		@Override
		protected CodesType newInstance(String pValue, String pDisplay)
		{
			return new CodesType(pValue, pDisplay);
		}

		@Override
		protected String getLoggerName()
		{
			return LOGGER_NAME;
		}

	}

	private static final CodesTypeFactory FACTORY = new CodesTypeFactory();

	@SuppressWarnings("serial")
	private static final TypeConstantTypeDescriptor<CodesType> CODESTYPE_DESCRIPTOR = new TypeConstantTypeDescriptor<CodesType>(
			CodesType.class)
	{
		@Override
		public CodesType getValue(String pValue)
		{
			return FACTORY.getValue(pValue, false);
		}
	};

	public CodesType()
	{
		super(CODESTYPE_DESCRIPTOR); // for JPA
	}

	protected CodesType(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, CODESTYPE_DESCRIPTOR);
	}

	public static List<CodesType> getAllValues()
	{
		return FACTORY.getAllValues();
	}

	public static CodesType newInstance(String pValue)
	{
		return FACTORY.getValue(pValue);
	}

	@Override
	public String getName()
	{
		return this.getClass().getName();
	}
}
