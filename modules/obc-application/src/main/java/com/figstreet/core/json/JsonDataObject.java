package com.figstreet.core.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.figstreet.core.CompareUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public abstract class JsonDataObject implements Jsonable, Serializable
{
	private static final long serialVersionUID = -7061076308897768281L;

	public static Gson generateGson()
	{
		return generateGson((Collection<JsonableAdapter>) null, null);
	}

	public static Gson generateGson(String pTimestampFormat)
	{
		return generateGson((Collection<JsonableAdapter>) null, pTimestampFormat);
	}

	public static Gson generateGson(JsonableAdapter pTypeAdapter)
	{
		ArrayList<JsonableAdapter> list = new ArrayList<>(1);
		if (pTypeAdapter != null)
			list.add(pTypeAdapter);
		return generateGson(list, null);
	}

	public static Gson generateGson(Collection<JsonableAdapter> pTypeAdapters, String pTimestampFormat)
	{
		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		if (pTypeAdapters != null)
		{
			for (JsonableAdapter adapter : pTypeAdapters)
				builder.registerTypeAdapter(adapter.getType(), adapter);
		}
		if (!CompareUtil.isEmpty(pTimestampFormat))
			builder.setDateFormat(pTimestampFormat);

		return builder.create();
	}

	public static Gson generateSecureGson()
	{
		return generateSecureGson((Collection<JsonableAdapter>) null, null);
	}

	public static Gson generateSecureGson(String pTimestampFormat)
	{
		return generateSecureGson((Collection<JsonableAdapter>) null, pTimestampFormat);
	}

	public static Gson generateSecureGson(JsonableAdapter pTypeAdapter)
	{
		ArrayList<JsonableAdapter> list = new ArrayList<>(1);
		if (pTypeAdapter != null)
			list.add(pTypeAdapter);
		return generateSecureGson(list, null);
	}

	public static Gson generateSecureGson(Collection<JsonableAdapter> pTypeAdapters, String pTimestampFormat)
	{
		GsonBuilder builder =
			new GsonBuilder().excludeFieldsWithoutExposeAnnotation().addSerializationExclusionStrategy(
				new JsonSensitiveExclusionStrategy());
		if (pTypeAdapters != null)
		{
			for (JsonableAdapter adapter : pTypeAdapters)
				builder.registerTypeAdapter(adapter.getType(), adapter);
		}
		if (!CompareUtil.isEmpty(pTimestampFormat))
			builder.setDateFormat(pTimestampFormat);
		return builder.create();
	}

	public String toJsonString()
	{
		return this.toJsonString(false);
	}

	@Override
	public String toJsonString(boolean pIncludeSensitive)
	{
		return this.getGson(pIncludeSensitive).toJson(this);
	}

	public byte[] toJsonBytes(Charset pCharset)
	{
		return this.toJsonBytes(false, pCharset);
	}

	@Override
	public byte[] toJsonBytes(boolean pIncludeSensitive, Charset pCharset)
	{
		String json = this.toJsonString(pIncludeSensitive);
		return json.getBytes(pCharset);
	}

	@Override
	public String toString()
	{
		return this.toJsonString(false);
	}

	@Override
	public boolean isExportClassName()
	{
		return false;
	}

	/**
	 * Subclasses can override this and implement any validation required.
	 * In general, GSON is very lenient when converting JSON string to object
	 *
	 * The default JsonValidationResult returned here passes validation
	 */

	@Override
	public JsonValidationResult isValid()
	{
		return new JsonValidationResult();
	}

	private static void validateJsonable(Jsonable pJsonable) throws JsonSyntaxException
	{
		if (pJsonable == null)
			throw new JsonSyntaxException(JsonValidationResult.NULL_OBJECT);

		JsonValidationResult validation = pJsonable.isValid();
		if (!validation.isValid())
			throw new JsonSyntaxException(validation.getMessage());

	}

	public static <T extends Jsonable> T fromJson(Gson pGson, Path pJsonFile, Class<T> pClass)
		throws IOException, JsonSyntaxException
	{
		T jsonData = null;
		BufferedReader reader = null;
		try
		{
			//System.out.println("it is executing 1 " + pJsonFile);
			reader = Files.newBufferedReader(pJsonFile);
			//System.out.println("it is executing 2");
			jsonData = pGson.fromJson(reader, pClass);
		}
		finally
		{
			if (reader != null)
				reader.close();
		}

		validateJsonable(jsonData);
		return jsonData;
	}

	public static <T extends Jsonable> T fromJson(Gson pGson, String pString, Class<T> pClass)
		throws JsonSyntaxException
	{
		T jsonData = pGson.fromJson(pString, pClass);
		validateJsonable(jsonData);
		return jsonData;
	}

	public static <T extends Jsonable> T fromJson(Gson pGson, InputStream pData, Class<T> pClass)
		throws IOException, JsonSyntaxException
	{
		T jsonData = null;
		InputStreamReader reader = null;
		try
		{
			reader = new InputStreamReader(pData);
			jsonData = pGson.fromJson(reader, pClass);
		}
		finally
		{
			if (reader != null)
				reader.close();
		}

		validateJsonable(jsonData);
		return jsonData;
	}

	public static <T extends Jsonable> List<T> listFromJson(Gson pGson, Path pJsonFile, Type pListType)
		throws IOException, JsonSyntaxException
	{
		List<T> jsonList = null;
		BufferedReader reader = null;
		try
		{
			reader = Files.newBufferedReader(pJsonFile);
			jsonList = pGson.fromJson(reader, pListType);
		}
		finally
		{
			if (reader != null)
				reader.close();
		}
		if (jsonList == null)
			jsonList = new ArrayList<>(1);

		if (!jsonList.isEmpty())
		{
			ArrayList<String> msgs = new ArrayList<>(jsonList.size());
			for (int i = 0; i < jsonList.size(); i++)
			{
				Jsonable j = jsonList.get(i);
				if (j != null)
				{
					JsonValidationResult validation = j.isValid();
					if (!validation.isValid())
						msgs.add("Element " + i + ": " + validation.getMessage());
				}
			}

			if (!msgs.isEmpty())
			{
				String message = "";
				for (String errorMsg : msgs)
				{
					message += errorMsg;
					message += "; ";
				}
				throw new JsonSyntaxException(message.trim());
			}
		}

		return jsonList;
	}
}
