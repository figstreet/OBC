package com.figstreet.biz.vendorcontact;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.vendorcontact.VendorContactID;
import com.google.gson.stream.JsonReader;

public class VendorContactIDJsonAdapter extends DataIDAdapter
{
	@Override
	public VendorContactID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new VendorContactID(pJsonReader.nextString());
		return null;
	}
}
