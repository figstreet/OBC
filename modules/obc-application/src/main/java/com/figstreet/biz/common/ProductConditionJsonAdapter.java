package com.figstreet.biz.common;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.common.ProductCondition;
import com.google.gson.stream.JsonReader;

public class ProductConditionJsonAdapter extends CodeConstantAdapter
{
	@Override
	public ProductCondition read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return ProductCondition.newInstance(pJsonReader.nextString());
		return null;
	}
}