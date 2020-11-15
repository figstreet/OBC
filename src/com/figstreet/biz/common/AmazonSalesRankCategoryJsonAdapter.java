package com.figstreet.biz.common;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.google.gson.stream.JsonReader;

public class AmazonSalesRankCategoryJsonAdapter extends CodeConstantAdapter
{
	@Override
	public AmazonSalesRankCategory read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return AmazonSalesRankCategory.newInstance(pJsonReader.nextString());
		return null;
	}
}
