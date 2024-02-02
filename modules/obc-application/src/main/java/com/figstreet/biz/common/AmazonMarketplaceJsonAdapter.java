package com.figstreet.biz.common;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.common.AmazonMarketplace;
import com.google.gson.stream.JsonReader;

public class AmazonMarketplaceJsonAdapter extends CodeConstantAdapter
{
	@Override
	public AmazonMarketplace read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return AmazonMarketplace.newInstance(pJsonReader.nextString());
		return null;
	}

}
