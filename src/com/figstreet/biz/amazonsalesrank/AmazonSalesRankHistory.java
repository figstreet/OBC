package com.figstreet.biz.amazonsalesrank;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import com.figstreet.biz.common.AmazonSalesRankCategoryJsonAdapter;
import com.figstreet.biz.users.UsersIDJsonAdapter;
import com.figstreet.biz.vendorproduct.VendorProductIDJsonAdapter;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.amazonsalesrank.AmazonSalesRankID;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;
import com.figstreet.data.vendorproduct.VendorProduct;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class AmazonSalesRankHistory extends JsonDataObject
{
	
	private static final long serialVersionUID = 794346814043336605L;
	private static final Gson GSON = generateGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Gson EXCLUDE_SENSITIVE_GSON = generateSecureGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Type GSON_LIST_TYPE = new TypeToken<List<AmazonSalesRankHistory>>()   
	{  	 /* empty */}.getType();
		
	@Expose
	@SerializedName(AmazonSalesRank.ID_COLUMN)
	@JsonAdapter(AmazonSalesRankIDJsonAdapter.class)
	private AmazonSalesRankID fAmazonSalesRankID;
	
	@Expose
	@SerializedName(VendorProduct.ID_COLUMN)
	@JsonAdapter(VendorProductIDJsonAdapter.class)
	private VendorProductID fVendorProductID;
	
	@Expose
	@SerializedName(AmazonSalesRank.PRIMARY_RANK_COLUMN)
	private Integer fPrimaryRank;
	
	@Expose
	@SerializedName(AmazonSalesRank.PRIMARY_RANK_CATEGORY_COLUMN)
	@JsonAdapter(AmazonSalesRankCategoryJsonAdapter.class)
	private AmazonSalesRankCategory fPrimarySalesRankCategory;
	
	@Expose
	@SerializedName(AmazonSalesRank.SECONDARY_RANK_COLUMN)
	private Integer fSecondaryRank;
	
	@Expose
	@SerializedName(AmazonSalesRank.SECONDARY_RANK_CATEGORY_COLUMN)
	private AmazonSalesRankCategory fSecondarySalesRankCategory;
	
	@Expose
	@SerializedName(AmazonSalesRank.DOWNLOADED_COLUMN)
	private Timestamp fDownloaded;
	
	@Expose
	@SerializedName(AmazonSalesRank.ADDED_COLUMN)
	private Timestamp fAdded;
	
	@Expose
	@SerializedName(AmazonSalesRank.ADDED_BY_COLUMN)
	@JsonAdapter(UsersIDJsonAdapter.class)
	private UsersID fAddedBy;


	protected AmazonSalesRankHistory()
	{
		// empty ctor
	}

	public AmazonSalesRankHistory(AmazonSalesRankID pAmazonSalesRankID, AmazonSalesRank pLastAmazonSalesRank)
	{
		this.fAmazonSalesRankID = pAmazonSalesRankID;
		if (pLastAmazonSalesRank != null)
		{
			this.fVendorProductID = pLastAmazonSalesRank.getVendorProductID();
			this.fPrimaryRank = pLastAmazonSalesRank.getPrimaryRank();
			this.fPrimarySalesRankCategory = pLastAmazonSalesRank.getPrimarySalesRankCategory();
			this.fSecondaryRank = pLastAmazonSalesRank.getSecondaryRank();
			this.fSecondarySalesRankCategory = pLastAmazonSalesRank.getSecondarySalesRankCategory();
			this.fDownloaded = pLastAmazonSalesRank.getDownloaded();
			this.fAdded = pLastAmazonSalesRank.getAdded();
			this.fAddedBy = pLastAmazonSalesRank.getAddedBy();
		}
	}
	
	public static AmazonSalesRankHistory newInstance(Path pJsonFile) throws IOException
	{
		return fromJson(GSON, pJsonFile, AmazonSalesRankHistory.class);
	}
	
	public static AmazonSalesRankHistory newInstance(String pJsonString)
	{
		return fromJson(GSON, pJsonString, AmazonSalesRankHistory.class);
	}
	
	public static boolean hasChanged(AmazonSalesRank pAmazonSalesRank, AmazonSalesRank pLastAmazonSalesRank)
	{
		if (pLastAmazonSalesRank == null || pLastAmazonSalesRank.isNewRecord())
			return false; // Not creating history for initial insert

		return (!CompareUtil.equals(pAmazonSalesRank.getVendorProductID(), pLastAmazonSalesRank.getVendorProductID())
				|| pAmazonSalesRank.getPrimaryRank() != pLastAmazonSalesRank.getPrimaryRank()
				|| !CompareUtil.equals(pAmazonSalesRank.getPrimarySalesRankCategory(), pLastAmazonSalesRank.getPrimarySalesRankCategory())
				|| pAmazonSalesRank.getSecondaryRank() != pLastAmazonSalesRank.getSecondaryRank()
				|| !CompareUtil.equals(pAmazonSalesRank.getSecondarySalesRankCategory(), pLastAmazonSalesRank.getSecondarySalesRankCategory())
				|| !CompareUtil.equals(pAmazonSalesRank.getDownloaded(), pLastAmazonSalesRank.getDownloaded())	
				);
	}

	public static History buildAmazonSalesRankHistory(AmazonSalesRank pAmazonSalesRank, AmazonSalesRank pLastAmazonSalesRank, UsersID pChangedBy)
	{
		History history = null;
		if (hasChanged(pAmazonSalesRank, pLastAmazonSalesRank))
		{
			AmazonSalesRankHistory amazonSalesRankHistory = new AmazonSalesRankHistory(pAmazonSalesRank.getRecordID(), pLastAmazonSalesRank);
			history = new History(pAmazonSalesRank.getRecordID(), pChangedBy);
			history.setPriorValue(amazonSalesRankHistory.toJsonString());
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
	
	public AmazonSalesRankID getAmazonSalesRankID()
	{
		return this.fAmazonSalesRankID;
	}
	
	public VendorProductID getVendorProductID()
	{
		return this.fVendorProductID;
	}
	
	public int getPrimaryRank()
	{
		return this.fPrimaryRank;
	}
	
	public AmazonSalesRankCategory getPrimarySalesRankCategory()
	{
		return this.fPrimarySalesRankCategory;
	}
	
	public int getSecondaryRank()
	{
		return this.fSecondaryRank;
	}
	
	public AmazonSalesRankCategory getSeAmazonSalesRankCategory()
	{
		return this.fSecondarySalesRankCategory;
	}
	
	public Timestamp getDownloaded()
	{
		return this.fDownloaded;
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
