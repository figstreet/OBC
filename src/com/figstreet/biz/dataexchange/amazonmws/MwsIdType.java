package com.figstreet.biz.dataexchange.amazonmws;

import java.util.List;

import com.figstreet.core.TypeConstant;
import com.figstreet.core.TypeConstantTypeDescriptor;

public class MwsIdType extends TypeConstant
{
	private static final long serialVersionUID = 9045836249547739582L;

	public static final MwsIdType ASIN = new MwsIdType("ASIN", "ASIN");
	public static final MwsIdType UPC = new MwsIdType("UPC", "UPC");

	public static class MwsIdTypeFactory extends TypeConstantFactory<MwsIdType>
	{
		private static final String LOGGER_NAME = MwsIdTypeFactory.class.getPackage().getName() + ".MwsIdTypeFactory";

		protected MwsIdTypeFactory()
		{
			super(MwsIdType.class);
		}

		@Override
		protected MwsIdType newInstance(String pValue, String pDisplay)
		{
			return new MwsIdType(pValue, pDisplay);
		}

		@Override
		protected String getLoggerName()
		{
			return LOGGER_NAME;
		}

	}

	private static final MwsIdTypeFactory FACTORY = new MwsIdTypeFactory();

	@SuppressWarnings("serial")
	private static final TypeConstantTypeDescriptor<MwsIdType> MWSIDTYPE_DESCRIPTOR = new TypeConstantTypeDescriptor<MwsIdType>(
			MwsIdType.class)
	{
		@Override
		public MwsIdType getValue(String pValue)
		{
			return FACTORY.getValue(pValue, false);
		}
	};

	public MwsIdType()
	{
		super(MWSIDTYPE_DESCRIPTOR); // for JPA
	}

	protected MwsIdType(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, MWSIDTYPE_DESCRIPTOR);
	}

	public static List<MwsIdType> getAllValues()
	{
		return FACTORY.getAllValues();
	}

	public static MwsIdType newInstance(String pValue)
	{
		return FACTORY.getValue(pValue);
	}

	@Override
	public String getName()
	{
		return this.getClass().getName();
	}
}
