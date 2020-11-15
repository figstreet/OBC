package com.figstreet.data.common;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class ProductCondition extends CodeConstant
{
	private static final long serialVersionUID = -2180415398029539647L;
	public static final ProductCondition NEW = new ProductCondition("new", "New");
	public static final ProductCondition USED = new ProductCondition("used", "Used");
	public static final ProductCondition COLLECTIBLE = new ProductCondition("collectible", "Collectible");

	private static class ProductConditionFactory extends CodeConstantFactory<ProductCondition>
	{
		private static final String LOGGER_NAME = ProductConditionFactory.class.getPackage().getName()
				+ ".ProductConditionFactory";

		private ProductConditionFactory()
		{
			super(ProductCondition.class, CodesType.PRODUCT_CONDITION);
		}

		@Override
		public ProductCondition newInstance(String pValue, String pDisplay)
		{
			return new ProductCondition(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final ProductConditionFactory fFactory = new ProductConditionFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<ProductCondition> PRODUCT_CONDITION_DESCRIPTOR = new CodeConstantTypeDescriptor<ProductCondition>(
			ProductCondition.class)
	{
		@Override
		public ProductCondition getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public ProductCondition()
	{
		super(PRODUCT_CONDITION_DESCRIPTOR);
	}

	private ProductCondition(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, PRODUCT_CONDITION_DESCRIPTOR);
	}

	public static ProductCondition newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<ProductCondition> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}
}