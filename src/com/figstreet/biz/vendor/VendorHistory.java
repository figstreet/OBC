package com.figstreet.biz.vendor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import com.figstreet.biz.users.UsersIDJsonAdapter;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.history.History;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class VendorHistory extends JsonDataObject
{

	private static final long serialVersionUID = 5282081106019551711L;
	private static final Gson GSON = generateGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Gson EXCLUDE_SENSITIVE_GSON = generateSecureGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Type GSON_LIST_TYPE = new TypeToken<List<VendorHistory>>()
	{
		/* empty */}.getType();

	@Expose
	@SerializedName(Vendor.ID_COLUMN)
	@JsonAdapter(VendorIDJsonAdapter.class)
	private VendorID fVendorID;

	@Expose
	@SerializedName(Vendor.ACTIVE_COLUMN)
	private boolean fActive;

	@Expose
	@SerializedName(Vendor.NAME_COLUMN)
	private String fName;

	@Expose
	@SerializedName(Vendor.WEBSITE_COLUMN)
	private String fWebsite;

	@Expose
	@SerializedName(Vendor.ADDED_COLUMN)
	private Timestamp fAdded;

	@Expose
	@SerializedName(Vendor.ADDED_BY_COLUMN)
	@JsonAdapter(UsersIDJsonAdapter.class)
	private UsersID fAddedBy;

	protected VendorHistory()
	{
		// empty ctor
	}

	public VendorHistory(VendorID pVendorID, Vendor pLastVendor)
	{
		this.fVendorID = pVendorID;
		if (pLastVendor != null)
		{
			this.fActive = pLastVendor.isActive();
			this.fName = pLastVendor.getName();
			this.fWebsite = pLastVendor.getWebsite();
			this.fAdded = pLastVendor.getAdded();
			this.fAddedBy = pLastVendor.getAddedBy();
		}
	}

	public static VendorHistory newInstance(Path pJsonFile) throws IOException
	{
		return fromJson(GSON, pJsonFile, VendorHistory.class);
	}

	public static VendorHistory newInstance(String pJsonString)
	{
		return fromJson(GSON, pJsonString, VendorHistory.class);
	}

	public static boolean hasChanged(Vendor pVendor, Vendor pLastVendor)
	{
		if (pLastVendor == null || pLastVendor.isNewRecord())
			return false; // Not creating history for initial insert

		return (pVendor.isActive() != pLastVendor.isActive()
				|| !CompareUtil.equalsString(pVendor.getName(), pLastVendor.getName(), true)
				|| !CompareUtil.equalsString(pVendor.getWebsite(), pLastVendor.getWebsite(), true));
	}

	public static History buildVendorHistory(Vendor pVendor, Vendor pLastVendor, UsersID pChangedBy)
	{
		History history = null;
		if (hasChanged(pVendor, pLastVendor))
		{
			VendorHistory vendorHistory = new VendorHistory(pVendor.getRecordID(), pLastVendor);
			history = new History(pVendor.getRecordID(), pChangedBy);
			history.setPriorValue(vendorHistory.toJsonString());
		}
		return history;
	}

	@Override
	public Gson getGson(boolean pIncludeSensitive)
	{
		if (pIncludeSensitive)
			return GSON;
		return EXCLUDE_SENSITIVE_GSON;
	}

	@Override
	public Type getListType()
	{
		return GSON_LIST_TYPE;
	}

	public boolean isActive()
	{
		return this.fActive;
	}

	public String getName()
	{
		return this.fName;
	}

	public String getWebsite()
	{
		return this.fWebsite;
	}

	public Timestamp getAdded()
	{
		return this.fAdded;
	}

	public UsersID getAddedBy()
	{
		return this.fAddedBy;
	}
}