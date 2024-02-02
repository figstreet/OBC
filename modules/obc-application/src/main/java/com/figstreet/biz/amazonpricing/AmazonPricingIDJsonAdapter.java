package com.figstreet.biz.amazonpricing;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.amazonpricing.AmazonPricingID;
import com.google.gson.stream.JsonReader;

public class AmazonPricingIDJsonAdapter extends DataIDAdapter
{
	@Override
	public AmazonPricingID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new AmazonPricingID(pJsonReader.nextString());
		return null;
	}
}
