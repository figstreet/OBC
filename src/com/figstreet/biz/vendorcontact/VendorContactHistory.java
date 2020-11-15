package com.figstreet.biz.vendorcontact;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import com.figstreet.biz.users.UsersIDJsonAdapter;
import com.figstreet.biz.vendor.VendorIDJsonAdapter;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorcontact.VendorContact;
import com.figstreet.data.vendorcontact.VendorContactID;
import com.figstreet.data.vendorcontact.VendorContactType;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class VendorContactHistory extends JsonDataObject
{
	private static final long serialVersionUID = 2492170635186357140L;
	private static final Gson GSON = generateGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Gson EXCLUDE_SENSITIVE_GSON = generateSecureGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Type GSON_LIST_TYPE = new TypeToken<List<VendorContactHistory>>()
	{
		/* empty */}.getType();

	@Expose
	@SerializedName(VendorContact.ID_COLUMN)
	@JsonAdapter(VendorContactIDJsonAdapter.class)
	private VendorContactID fVendorContactID;

	@Expose
	@SerializedName(Vendor.ID_COLUMN)
	@JsonAdapter(VendorIDJsonAdapter.class)
	private VendorID fVendorID;

	@Expose
	@SerializedName(VendorContact.ACTIVE_COLUMN)
	private boolean fActive;

	@Expose
	@SerializedName(VendorContact.PRIMARY_COLUMN)
	private boolean fPrimary;

	@Expose
	@SerializedName(VendorContact.TYPE_COLUMN)
	@JsonAdapter(VendorContactTypeJsonAdapter.class)
	private VendorContactType fType;

	@Expose
	@SerializedName(VendorContact.EMAIL_COLUMN)
	private String fEmail;

	@Expose
	@SerializedName(VendorContact.PHONE_COUNTRY_CODE_COLUMN)
	private String fPhoneCountryCode;

	@Expose
	@SerializedName(VendorContact.PHONE1_COLUMN)
	private String fPhone1;

	@Expose
	@SerializedName(VendorContact.PHONE2_COLUMN)
	private String fPhone2;

	@Expose
	@SerializedName(VendorContact.PHONE3_COLUMN)
	private String fPhone3;

	@Expose
	@SerializedName(VendorContact.PHONE_EXT_COLUMN)
	private String fPhoneExt;

	@Expose
	@SerializedName(VendorContact.NAME1_COLUMN)
	private String fName1;

	@Expose
	@SerializedName(VendorContact.NAME2_COLUMN)
	private String fName2;

	@Expose
	@SerializedName(VendorContact.ADDED_COLUMN)
	private Timestamp fAdded;

	@Expose
	@SerializedName(VendorContact.ADDED_BY_COLUMN)
	@JsonAdapter(UsersIDJsonAdapter.class)
	private UsersID fAddedBy;

	protected VendorContactHistory()
	{
		// empty ctor
	}

	public VendorContactHistory(VendorContactID pVendorContactID, VendorContact pLastVendorContact)
	{
		this.fVendorContactID = pVendorContactID;
		if (pLastVendorContact != null)
		{
			this.fActive = pLastVendorContact.isActive();
			this.fPrimary = pLastVendorContact.isPrimary();
			this.fType = pLastVendorContact.getType();
			this.fEmail = pLastVendorContact.getEmail();
			this.fPhoneCountryCode = pLastVendorContact.getPhoneCountryCode();
			this.fPhone1 = pLastVendorContact.getPhone1();
			this.fPhone2 = pLastVendorContact.getPhone2();
			this.fPhone3 = pLastVendorContact.getPhone3();
			this.fPhoneExt = pLastVendorContact.getPhoneExt();
			this.fName1 = pLastVendorContact.getName1();
			this.fName2 = pLastVendorContact.getName2();
			this.fAdded = pLastVendorContact.getAdded();
			this.fAddedBy = pLastVendorContact.getAddedBy();
		}
	}

	public static VendorContactHistory newInstance(Path pJsonFile) throws IOException
	{
		return fromJson(GSON, pJsonFile, VendorContactHistory.class);
	}

	public static VendorContactHistory newInstance(String pJsonString)
	{
		return fromJson(GSON, pJsonString, VendorContactHistory.class);
	}

	public static boolean hasChanged(VendorContact pVendorContact, VendorContact pLastVendorContact)
	{
		if (pLastVendorContact == null || pLastVendorContact.isNewRecord())
			return false; // Not creating history for initial insert

		return (pVendorContact.isActive() != pLastVendorContact.isActive()
				|| pVendorContact.isPrimary() != pLastVendorContact.isPrimary()
				|| !CompareUtil.equals(pVendorContact.getType(), pLastVendorContact.getType())
				|| !CompareUtil.equalsString(pVendorContact.getEmail(), pLastVendorContact.getEmail(), true)
				|| !CompareUtil.equalsString(pVendorContact.getPhoneCountryCode(),
						pLastVendorContact.getPhoneCountryCode(), true)
				|| !CompareUtil.equalsString(pVendorContact.getPhone1(), pLastVendorContact.getPhone1(), true)
				|| !CompareUtil.equalsString(pVendorContact.getPhone2(), pLastVendorContact.getPhone2(), true)
				|| !CompareUtil.equalsString(pVendorContact.getPhone3(), pLastVendorContact.getPhone3(), true)
				|| !CompareUtil.equalsString(pVendorContact.getPhoneExt(), pLastVendorContact.getPhoneExt(), true)
				|| !CompareUtil.equalsString(pVendorContact.getName1(), pLastVendorContact.getName1(), true)
				|| !CompareUtil.equalsString(pVendorContact.getName2(), pLastVendorContact.getName2(), true));
	}

	public static History buildVendorContactHistory(VendorContact pVendorContact, VendorContact pLastVendorContact,
			UsersID pChangedBy)
	{
		History history = null;
		if (hasChanged(pVendorContact, pLastVendorContact))
		{
			VendorContactHistory vendorContactHistory = new VendorContactHistory(pVendorContact.getRecordID(),
					pLastVendorContact);
			history = new History(pVendorContact.getRecordID(), pChangedBy);
			history.setPriorValue(vendorContactHistory.toJsonString());
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

	public boolean isPrimary()
	{
		return this.fPrimary;
	}

	public VendorContactType getType()
	{
		return this.fType;
	}

	public String getEmail()
	{
		return this.fEmail;
	}

	public String getPhoneCountryCode()
	{
		return this.fPhoneCountryCode;
	}

	public String getPhone1()
	{
		return this.fPhone1;
	}

	public String getPhone2()
	{
		return this.fPhone2;
	}

	public String getPhone3()
	{
		return this.fPhone3;
	}

	public String getPhoneExt()
	{
		return this.fPhoneExt;
	}

	public String getName1()
	{
		return this.fName1;
	}

	public String getName2()
	{
		return this.fName2;
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