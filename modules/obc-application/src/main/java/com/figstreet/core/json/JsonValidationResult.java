package com.figstreet.core.json;

public class JsonValidationResult
{
	private String fMessage = null;
	private boolean fValid = false;

	public static final String SUCCESS = "Success";
	public static final String ERROR = "An unexpected error occurred";
	public static final String NULL_OBJECT = "Gson returned a null object";

	public JsonValidationResult()
	{
		this.fMessage = SUCCESS;
		this.fValid = true;
	}

	public JsonValidationResult(String pErrorMessage)
	{
		this.fMessage = pErrorMessage;
		this.fValid = false;
	}

	public String getMessage()
	{
		return this.fMessage;
	}

	public boolean isValid()
	{
		return this.fValid;
	}

}
