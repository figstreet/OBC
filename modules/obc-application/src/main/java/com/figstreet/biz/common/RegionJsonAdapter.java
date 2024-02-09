package com.figstreet.biz.common;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.common.Region;
import com.google.gson.stream.JsonReader;

public class RegionJsonAdapter extends CodeConstantAdapter
{
	@Override
	public Region read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return Region.newInstance(pJsonReader.nextString());
		return null;
	}
}