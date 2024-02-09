package com.figstreet.core.json;

import java.io.IOException;
import java.sql.Date;

import com.figstreet.core.DateUtil;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DateAdapter extends TypeAdapter<Date>
{
	private String fFormat = DateUtil.DATE_FORMAT_YYYYMMDD;

	public DateAdapter()
	{
		super();
	}

	public DateAdapter(String pFormat)
	{
		super();
		this.fFormat = pFormat;
	}

	@Override
	public Date read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
		{
			String value = pJsonReader.nextString();
			return readDate(value, this.fFormat);
		}
		return null;
	}

	@Override
	public void write(JsonWriter pJsonWriter, Date pDate) throws IOException
	{
		if (pDate == null)
			pJsonWriter.nullValue();
		else
		{
			String value = writeDate(pDate, this.fFormat);
			if (value == null)
				pJsonWriter.nullValue();
			else
				pJsonWriter.value(value);
		}
	}

	public static Date readDate(String pDateVal, String pFormat)
	{
		return DateUtil.parseDate(pDateVal, pFormat);
	}

	public static String writeDate(Date pDateVal, String pFormat)
	{
		return DateUtil.formatDate(pDateVal, pFormat);
	}

}
