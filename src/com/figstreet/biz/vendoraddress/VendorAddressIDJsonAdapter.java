package com.figstreet.biz.vendoraddress;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.vendoraddress.VendorAddressID;
import com.google.gson.stream.JsonReader;

public class VendorAddressIDJsonAdapter extends DataIDAdapter
{
	@Override
	public VendorAddressID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new VendorAddressID(pJsonReader.nextString());
		return null;
	}
}
