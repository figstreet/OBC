package com.figstreet.core.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonableAdapter implements JsonSerializer<Jsonable>, JsonDeserializer<Jsonable>
{
	private final String fPackageName;
	private final Type fType;

	public JsonableAdapter(Type pForType, String pPackageName)
	{
		this.fType = pForType;
		this.fPackageName = pPackageName;
	}

	public String getPackageName()
	{
		return this.fPackageName;
	}

	public Type getType()
	{
		return this.fType;
	}

	@Override
	public JsonElement serialize(Jsonable pJsonable, Type pType, JsonSerializationContext pContext)
	{
		Class<? extends Jsonable> jsonClass = pJsonable.getClass();

		JsonElement data = pContext.serialize(pJsonable, jsonClass);
		if (!pJsonable.isExportClassName())
			return data;

		JsonObject result = new JsonObject();
		result.add("className", new JsonPrimitive(jsonClass.getSimpleName()));
		result.add("objectData", data);

		return result;
	}

	@Override
	public Jsonable deserialize(JsonElement pElement, Type pType, JsonDeserializationContext pContext)
		throws JsonParseException
	{
		JsonObject jsonObject = pElement.getAsJsonObject();
		JsonElement className = jsonObject.get("className");
		if (className == null)
			return pContext.deserialize(pElement, pType);

		String type = className.getAsString();
		JsonElement element = jsonObject.get("objectData");

		try
		{
			return pContext.deserialize(element, Class.forName(this.fPackageName + "." + type));
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new JsonParseException("Unknown element type: " + type, cnfe);
		}
	}
}