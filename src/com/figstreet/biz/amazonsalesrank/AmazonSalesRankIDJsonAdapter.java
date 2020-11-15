package com.figstreet.biz.amazonsalesrank;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.amazonsalesrank.AmazonSalesRankID;
import com.google.gson.stream.JsonReader;

public class AmazonSalesRankIDJsonAdapter extends DataIDAdapter
{
	@Override
	public AmazonSalesRankID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new AmazonSalesRankID(pJsonReader.nextString());
		return null;
	}
}
