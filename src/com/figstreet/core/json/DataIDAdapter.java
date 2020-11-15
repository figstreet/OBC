package com.figstreet.core.json;

import java.io.IOException;

import com.figstreet.core.DataID;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

public abstract class DataIDAdapter extends TypeAdapter<DataID>
{

	@Override
	public void write(JsonWriter pJsonWriter, DataID pDataID) throws IOException
	{
		if (pDataID == null)
			pJsonWriter.nullValue();
		else
		{
			String value = pDataID.toString();
			if (value == null)
				pJsonWriter.nullValue();
			else
				pJsonWriter.value(value);
		}
	}

}
