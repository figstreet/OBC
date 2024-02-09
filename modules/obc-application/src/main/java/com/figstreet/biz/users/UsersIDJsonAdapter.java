package com.figstreet.biz.users;

import java.io.IOException;

import com.figstreet.core.json.DataIDAdapter;
import com.figstreet.data.users.UsersID;
import com.google.gson.stream.JsonReader;

public class UsersIDJsonAdapter extends DataIDAdapter
{

	@Override
	public UsersID read(JsonReader pJsonReader) throws IOException
	{
		if (pJsonReader.hasNext())
			return new UsersID(pJsonReader.nextString());
		return null;
	}

}
