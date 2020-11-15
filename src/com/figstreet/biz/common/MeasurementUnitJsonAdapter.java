package com.figstreet.biz.common;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.common.MeasurementUnit;
import com.google.gson.stream.JsonReader;

public class MeasurementUnitJsonAdapter extends CodeConstantAdapter
{

	@Override
	public MeasurementUnit read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return MeasurementUnit.newInstance(pJsonReader.nextString());
		return null;
	}
}