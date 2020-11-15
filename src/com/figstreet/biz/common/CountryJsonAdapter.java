package com.figstreet.biz.common;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.common.Country;
import com.google.gson.stream.JsonReader;

public class CountryJsonAdapter extends CodeConstantAdapter
{

	@Override
	public Country read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return Country.newInstance(pJsonReader.nextString());
		return null;
	}

}
