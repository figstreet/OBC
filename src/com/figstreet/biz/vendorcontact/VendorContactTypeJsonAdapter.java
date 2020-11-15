package com.figstreet.biz.vendorcontact;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.vendorcontact.VendorContactType;
import com.google.gson.stream.JsonReader;

public class VendorContactTypeJsonAdapter extends CodeConstantAdapter
{
	@Override
	public VendorContactType read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return VendorContactType.newInstance(pJsonReader.nextString());
		return null;
	}
}
