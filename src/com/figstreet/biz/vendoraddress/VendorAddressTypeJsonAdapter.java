package com.figstreet.biz.vendoraddress;

import java.io.IOException;

import com.figstreet.core.json.CodeConstantAdapter;
import com.figstreet.data.vendoraddress.VendorAddressType;
import com.google.gson.stream.JsonReader;

public class VendorAddressTypeJsonAdapter extends CodeConstantAdapter
{
	@Override
	public VendorAddressType read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return VendorAddressType.newInstance(pJsonReader.nextString());
		return null;
	}
}
