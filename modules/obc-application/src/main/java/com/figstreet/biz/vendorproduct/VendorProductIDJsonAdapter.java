package com.figstreet.biz.vendorproduct;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.vendorproduct.VendorProductID;
import com.google.gson.stream.JsonReader;

public class VendorProductIDJsonAdapter extends DataIDAdapter
{
	@Override
	public VendorProductID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new VendorProductID(pJsonReader.nextString());
		return null;
	}
}
