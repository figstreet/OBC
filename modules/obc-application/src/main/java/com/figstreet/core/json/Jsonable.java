package com.figstreet.core.json;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.google.gson.Gson;

public interface Jsonable
{
	public Gson getGson(boolean pIncludeSensitive);

	public Type getListType();

	public String toJsonString(boolean pIncludeSensitive);

	public byte[] toJsonBytes(boolean pIncludeSensitive, Charset pCharset);

	public boolean isExportClassName();

	public JsonValidationResult isValid();
}
