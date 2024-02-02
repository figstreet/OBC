package com.figstreet.data.userpermission;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class UserPermissionType extends CodeConstant
{
	private static final long serialVersionUID = -3191353988298351844L;

	public static final UserPermissionType PRODUCT_DELETE = new UserPermissionType("PRODUCT_DELETE",
			"Delete a Product");
	public static final UserPermissionType VENDOR_DELETE = new UserPermissionType("VENDOR_DELETE", "Delete a Vendor");
	public static final UserPermissionType USER_DELETE = new UserPermissionType("USER_DELETE", "Delete a User");
	public static final UserPermissionType USER_MODIFY = new UserPermissionType("USER_MODIFY", "Modify a User");

	private static class UserPermissionTypeFactory extends CodeConstantFactory<UserPermissionType>
	{
		private static final String LOGGER_NAME = UserPermissionTypeFactory.class.getPackage().getName()
				+ ".UserPermissionTypeFactory";

		private UserPermissionTypeFactory()
		{
			super(UserPermissionType.class, CodesType.USER_PERMISSION_TYPE);
		}

		@Override
		public UserPermissionType newInstance(String pValue, String pDisplay)
		{
			return new UserPermissionType(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final UserPermissionTypeFactory fFactory = new UserPermissionTypeFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<UserPermissionType> USERPERMISSIONTYPE_DESCRIPTOR = new CodeConstantTypeDescriptor<UserPermissionType>(
			UserPermissionType.class)
	{
		@Override
		public UserPermissionType getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public UserPermissionType()
	{
		super(USERPERMISSIONTYPE_DESCRIPTOR);
	}

	private UserPermissionType(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, USERPERMISSIONTYPE_DESCRIPTOR);
	}

	public static UserPermissionType newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<UserPermissionType> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}

}
