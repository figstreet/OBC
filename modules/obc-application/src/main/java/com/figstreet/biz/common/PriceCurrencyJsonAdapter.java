package com.figstreet.biz.common;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.common.PriceCurrency;
import com.google.gson.stream.JsonReader;

public class PriceCurrencyJsonAdapter extends CodeConstantAdapter
{

	@Override
	public PriceCurrency read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return PriceCurrency.newInstance(pJsonReader.nextString());
		return null;
	}
}
