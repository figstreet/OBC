package com.figstreet.data.vendorcontact;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class VendorContactType extends CodeConstant
{
	private static final long serialVersionUID = 7574511136366890454L;

	public static final VendorContactType SALES = new VendorContactType("SALES", "Sales");

	private static class VendorContactTypeFactory extends CodeConstantFactory<VendorContactType>
	{
		private static final String LOGGER_NAME = VendorContactTypeFactory.class.getPackage().getName()
				+ ".VendorContactTypeFactory";

		private VendorContactTypeFactory()
		{
			super(VendorContactType.class, CodesType.VENDOR_CONTACT_TYPE);
		}

		@Override
		public VendorContactType newInstance(String pValue, String pDisplay)
		{
			return new VendorContactType(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final VendorContactTypeFactory fFactory = new VendorContactTypeFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<VendorContactType> VENDORCONTACTTYPE_DESCRIPTOR = new CodeConstantTypeDescriptor<VendorContactType>(
			VendorContactType.class)
	{
		@Override
		public VendorContactType getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public VendorContactType()
	{
		super(VENDORCONTACTTYPE_DESCRIPTOR);
	}

	private VendorContactType(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, VENDORCONTACTTYPE_DESCRIPTOR);
	}

	public static VendorContactType newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<VendorContactType> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}

}
