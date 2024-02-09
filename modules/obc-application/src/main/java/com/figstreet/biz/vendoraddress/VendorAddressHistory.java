package com.figstreet.biz.vendoraddress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import com.figstreet.biz.common.CountryJsonAdapter;
import com.figstreet.biz.common.RegionJsonAdapter;
import com.figstreet.biz.users.UsersIDJsonAdapter;
import com.figstreet.biz.vendor.VendorIDJsonAdapter;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.common.Country;
import com.figstreet.data.common.Region;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendoraddress.VendorAddress;
import com.figstreet.data.vendoraddress.VendorAddressID;
import com.figstreet.data.vendoraddress.VendorAddressType;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class VendorAddressHistory extends JsonDataObject
{
	private static final long serialVersionUID = -8547038006744962789L;
	private static final Gson GSON = generateGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Gson EXCLUDE_SENSITIVE_GSON = generateSecureGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Type GSON_LIST_TYPE = new TypeToken<List<VendorAddressHistory>>()
	{
		/* empty */}.getType();

	@Expose
	@SerializedName(VendorAddress.ID_COLUMN)
	@JsonAdapter(VendorAddressIDJsonAdapter.class)
	private VendorAddressID fVendorAddressID;

	@Expose
	@SerializedName(Vendor.ID_COLUMN)
	@JsonAdapter(VendorIDJsonAdapter.class)
	private VendorID fVendorID;

	@Expose
	@SerializedName(VendorAddress.ACTIVE_COLUMN)
	private boolean fActive;

	@Expose
	@SerializedName(VendorAddress.PRIMARY_COLUMN)
	private boolean fPrimary;

	@Expose
	@SerializedName(VendorAddress.TYPE_COLUMN)
	@JsonAdapter(VendorAddressTypeJsonAdapter.class)
	private VendorAddressType fType;

	@Expose
	@SerializedName(VendorAddress.ADDRESS1_COLUMN)
	private String fAddress1;

	@Expose
	@SerializedName(VendorAddress.ADDRESS2_COLUMN)
	private String fAddress2;

	@Expose
	@SerializedName(VendorAddress.CITY_COLUMN)
	private String fCity;

	@Expose
	@SerializedName(VendorAddress.REGION_COLUMN)
	@JsonAdapter(RegionJsonAdapter.class)
	private Region fRegion;

	@Expose
	@SerializedName(VendorAddress.ZIP_COLUMN)
	private String fZip;

	@Expose
	@SerializedName(VendorAddress.COUNTRY_COLUMN)
	@JsonAdapter(CountryJsonAdapter.class)
	private Country fCountry;

	@Expose
	@SerializedName(VendorAddress.ADDED_COLUMN)
	private Timestamp fAdded;

	@Expose
	@SerializedName(VendorAddress.ADDED_BY_COLUMN)
	@JsonAdapter(UsersIDJsonAdapter.class)
	private UsersID fAddedBy;

	protected VendorAddressHistory()
	{
		// empty ctor
	}

	public VendorAddressHistory(VendorAddressID pVendorAddressID, VendorAddress pLastVendorAddress)
	{
		this.fVendorAddressID = pVendorAddressID;
		if (pLastVendorAddress != null)
		{
			this.fActive = pLastVendorAddress.isActive();
			this.fPrimary = pLastVendorAddress.getPrimary();
			this.fType = pLastVendorAddress.getType();
			this.fAddress1 = pLastVendorAddress.getAddress1();
			this.fAddress2 = pLastVendorAddress.getAddress2();
			this.fCity = pLastVendorAddress.getCity();
			this.fRegion = pLastVendorAddress.getRegion();
			this.fZip = pLastVendorAddress.getZip();
			this.fCountry = pLastVendorAddress.getCountry();
			this.fAdded = pLastVendorAddress.getAdded();
			this.fAddedBy = pLastVendorAddress.getAddedBy();
		}
	}

	public static VendorAddressHistory newInstance(Path pJsonFile) throws IOException
	{
		return fromJson(GSON, pJsonFile, VendorAddressHistory.class);
	}

	public static VendorAddressHistory newInstance(String pJsonString)
	{
		return fromJson(GSON, pJsonString, VendorAddressHistory.class);
	}

	public static boolean hasChanged(VendorAddress pVendorAddress, VendorAddress pLastVendorAddress)
	{
		if (pLastVendorAddress == null || pLastVendorAddress.isNewRecord())
			return false; // Not creating history for initial insert

		return (pVendorAddress.isActive() != pLastVendorAddress.isActive()
				|| pVendorAddress.getPrimary() != pLastVendorAddress.getPrimary()
				|| !CompareUtil.equals(pVendorAddress.getType(), pLastVendorAddress.getType())
				|| !CompareUtil.equalsString(pVendorAddress.getAddress1(), pLastVendorAddress.getAddress1(), true)
				|| !CompareUtil.equalsString(pVendorAddress.getAddress2(), pLastVendorAddress.getAddress2(), true)
				|| !CompareUtil.equalsString(pVendorAddress.getCity(), pLastVendorAddress.getCity(), true)
				|| !CompareUtil.equals(pVendorAddress.getRegion(), pLastVendorAddress.getRegion())
				|| !CompareUtil.equalsString(pVendorAddress.getZip(), pLastVendorAddress.getZip(), true)
				|| !CompareUtil.equals(pVendorAddress.getCountry(), pLastVendorAddress.getCountry()));
	}

	public static History buildVendorAddressHistory(VendorAddress pVendorAddress, VendorAddress pLastVendorAddress,
			UsersID pChangedBy)
	{
		History history = null;
		if (hasChanged(pVendorAddress, pLastVendorAddress))
		{
			VendorAddressHistory vendorAddressHistory = new VendorAddressHistory(pVendorAddress.getRecordID(),
					pLastVendorAddress);
			history = new History(pVendorAddress.getRecordID(), pChangedBy);
			history.setPriorValue(vendorAddressHistory.toJsonString());
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

	public VendorAddressType getType()
	{
		return this.fType;
	}

	public String getAddress1()
	{
		return this.fAddress1;
	}

	public String getAddress2()
	{
		return this.fAddress2;
	}

	public String getCity()
	{
		return this.fCity;
	}

	public Region getRegion()
	{
		return this.fRegion;
	}

	public String getZip()
	{
		return this.fZip;
	}

	public Country getCountry()
	{
		return this.fCountry;
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