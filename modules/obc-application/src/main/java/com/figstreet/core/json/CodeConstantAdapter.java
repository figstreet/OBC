package com.figstreet.core.json;

import java.io.IOException;

import com.figstreet.data.codes.CodeConstant;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

public abstract class CodeConstantAdapter extends TypeAdapter<CodeConstant>
{

	@Override
	public void write(JsonWriter pJsonWriter, CodeConstant pCodeConstant) throws IOException
	{
		if (pCodeConstant == null)
			pJsonWriter.nullValue();
		else
		{
			String value = pCodeConstant.toString();
			if (value == null)
				pJsonWriter.nullValue();
			else
				pJsonWriter.value(value);
		}
	}

}
