package com.figstreet.biz.vendor;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.vendor.VendorID;
import com.google.gson.stream.JsonReader;

public class VendorIDJsonAdapter extends DataIDAdapter
{
	@Override
	public VendorID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new VendorID(pJsonReader.nextString());
		return null;
	}
}