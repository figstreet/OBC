package com.figstreet.data.common;

import java.util.Collection;

import com.figstreet.data.codes.CodeConstant;
import com.figstreet.data.codes.CodeConstantTypeDescriptor;
import com.figstreet.data.codes.CodesType;

public class MeasurementUnit extends CodeConstant
{
	private static final long serialVersionUID = 3473493098121486167L;
	public static final MeasurementUnit INCHES = new MeasurementUnit("inches", "inches");

	private static class MeasurementUnitFactory extends CodeConstantFactory<MeasurementUnit>
	{
		private static final String LOGGER_NAME = MeasurementUnitFactory.class.getPackage().getName()
				+ ".MeasurementUnitFactory";

		private MeasurementUnitFactory()
		{
			super(MeasurementUnit.class, CodesType.MEASUREMENT_UNIT);
		}

		@Override
		public MeasurementUnit newInstance(String pValue, String pDisplay)
		{
			return new MeasurementUnit(pValue, pDisplay);
		}

		@Override
		public String getLoggerName()
		{
			return LOGGER_NAME;
		}
	}

	private static final MeasurementUnitFactory fFactory = new MeasurementUnitFactory();
	@SuppressWarnings("serial")
	private static final CodeConstantTypeDescriptor<MeasurementUnit> MEASUREMENT_UNIT_DESCRIPTOR = new CodeConstantTypeDescriptor<MeasurementUnit>(
			MeasurementUnit.class)
	{
		@Override
		public MeasurementUnit getValue(String pValue)
		{
			return fFactory.getValue(pValue, false);
		}
	};

	public MeasurementUnit()
	{
		super(MEASUREMENT_UNIT_DESCRIPTOR);
	}

	private MeasurementUnit(String pValue, String pDisplay)
	{
		super(pValue, pDisplay, MEASUREMENT_UNIT_DESCRIPTOR);
	}

	public static MeasurementUnit newInstance(String pValue)
	{
		return fFactory.getValue(pValue);
	}

	public static Collection<MeasurementUnit> getAllValues(boolean pIncludeNonCodes)
	{
		return fFactory.getNativeLinkValues(pIncludeNonCodes);
	}
}
