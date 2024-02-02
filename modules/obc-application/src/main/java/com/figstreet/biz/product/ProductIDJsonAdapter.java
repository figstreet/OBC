package com.figstreet.biz.product;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.product.ProductID;
import com.google.gson.stream.JsonReader;

public class ProductIDJsonAdapter extends DataIDAdapter
{

	@Override
	public ProductID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new ProductID(pJsonReader.nextString());
		return null;
	}

}
