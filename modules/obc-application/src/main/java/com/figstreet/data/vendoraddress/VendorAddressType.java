package com.figstreet.data.vendoraddress;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class VendorAddressType extends CodeConstant
{
	private static final long serialVersionUID = -9153235713602663330L;

	public static final VendorAddressType MAILING_ADDRESS = new VendorAddressType("MAILING", "Mailing Address");

	private static class VendorAddressTypeFactory extends CodeConstantFactory<VendorAddressType>
	{
		private static final String LOGGER_NAME = VendorAddressTypeFactory.class.getPackage().getName()
				+ ".VendorAddressTypeFactory";

		private VendorAddressTypeFactory()
		{
			super(VendorAddressType.class, CodesType.VENDOR_ADDRESS_TYPE);
		}

		@Override
		public VendorAddressType newInstance(String pValue, String pDisplay)
		{
			return new VendorAddressType(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final VendorAddressTypeFactory fFactory = new VendorAddressTypeFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<VendorAddressType> VENDORADDRESSTYPE_DESCRIPTOR = new CodeConstantTypeDescriptor<VendorAddressType>(
			VendorAddressType.class)
	{
		@Override
		public VendorAddressType getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public VendorAddressType()
	{
		super(VENDORADDRESSTYPE_DESCRIPTOR);
	}

	private VendorAddressType(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, VENDORADDRESSTYPE_DESCRIPTOR);
	}

	public static VendorAddressType newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<VendorAddressType> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}

}
