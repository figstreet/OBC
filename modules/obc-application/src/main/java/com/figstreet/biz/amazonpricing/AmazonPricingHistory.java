package com.figstreet.biz.amazonpricing;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import com.figstreet.biz.common.PriceCurrencyJsonAdapter;
import com.figstreet.biz.common.ProductConditionJsonAdapter;
import com.figstreet.biz.users.UsersIDJsonAdapter;
import com.figstreet.biz.vendorproduct.VendorProductIDJsonAdapter;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.amazonpricing.AmazonPricing;
import com.figstreet.data.amazonpricing.AmazonPricingID;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.common.ProductCondition;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;
import com.figstreet.data.vendorproduct.VendorProduct;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class AmazonPricingHistory extends JsonDataObject
{
	private static final long serialVersionUID = 8513454265920152221L;
	private static final Gson GSON = generateGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Gson EXCLUDE_SENSITIVE_GSON = generateSecureGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Type GSON_LIST_TYPE = new TypeToken<List<AmazonPricingHistory>>()
	{
		/* empty */}.getType();

	@Expose
	@SerializedName(AmazonPricing.ID_COLUMN)
	@JsonAdapter(AmazonPricingIDJsonAdapter.class)
	private AmazonPricingID fAmazonPricingID;

	@Expose
	@SerializedName(VendorProduct.ID_COLUMN)
	@JsonAdapter(VendorProductIDJsonAdapter.class)
	private VendorProductID fVendorProductID;

	@Expose
	@SerializedName(AmazonPricing.PRODUCT_CONDITION_COLUMN)
	@JsonAdapter(ProductConditionJsonAdapter.class)
	private ProductCondition fProductCondition;

	@Expose
	@SerializedName(AmazonPricing.OFFER_COUNT_COLUMN)
	private int fOfferCount;

	@Expose
	@SerializedName(AmazonPricing.PRICE_CURRENCY_COLUMN)
	@JsonAdapter(PriceCurrencyJsonAdapter.class)
	private PriceCurrency fPriceCurrency;

	@Expose
	@SerializedName(AmazonPricing.BUYBOX_ITEM_PRICE_COLUMN)
	private Double fBuyboxItemPrice;

	@Expose
	@SerializedName(AmazonPricing.BUYBOX_SHIPPING_PRICE_COLUMN)
	private Double fBuyboxShippingPrice;

	@Expose
	@SerializedName(AmazonPricing.BUYBOX_FBA_COLUMN)
	private boolean fBuyboxFBA;

	@Expose
	@SerializedName(AmazonPricing.BUYBOX_SELLER_AMAZON_COLUMN)
	private boolean fBuyboxSellerAmazon;

	@Expose
	@SerializedName(AmazonPricing.SECONDARY_ITEM_PRICE_COLUMN)
	private Double fSecondaryItemPrice;

	@Expose
	@SerializedName(AmazonPricing.SECONDARY_SHIPPING_PRICE_COLUMN)
	private Double fSecondaryShippingPrice;

	@Expose
	@SerializedName(AmazonPricing.SECONDARY_FBA_COLUMN)
	private boolean fSecondaryFBA;

	@Expose
	@SerializedName(AmazonPricing.DOWNLOADED_COLUMN)
	private Timestamp fDownloaded;

	@Expose
	@SerializedName(AmazonPricing.ADDED_COLUMN)
	private Timestamp fAdded;

	@Expose
	@SerializedName(AmazonPricing.ADDED_BY_COLUMN)
	@JsonAdapter(UsersIDJsonAdapter.class)
	private UsersID fAddedBy;

	protected AmazonPricingHistory()
	{
		// empty ctor
	}

	public AmazonPricingHistory(AmazonPricingID pAmazonPricingID, AmazonPricing pLastAmazonPricing)
	{
		this.fAmazonPricingID = pAmazonPricingID;
		if (pLastAmazonPricing != null)
		{
			this.fVendorProductID = pLastAmazonPricing.getVendorProductID();
			this.fProductCondition = pLastAmazonPricing.getProductCondition();
			this.fOfferCount = pLastAmazonPricing.getOfferCount();
			this.fPriceCurrency = pLastAmazonPricing.getPriceCurrency();
			this.fBuyboxItemPrice = pLastAmazonPricing.getBuyboxItemPrice();
			this.fBuyboxShippingPrice = pLastAmazonPricing.getBuyboxShippingPrice();
			this.fBuyboxFBA = pLastAmazonPricing.isBuyboxFBA();
			this.fBuyboxSellerAmazon = pLastAmazonPricing.getBuyboxSellerAmazon();
			this.fSecondaryItemPrice = pLastAmazonPricing.getSecondaryItemPrice();
			this.fSecondaryShippingPrice = pLastAmazonPricing.getSecondaryShippingPrice();
			this.fSecondaryFBA = pLastAmazonPricing.isSecondaryFBA();
			this.fDownloaded = pLastAmazonPricing.getDownloaded();
			this.fAdded = pLastAmazonPricing.getAdded();
			this.fAddedBy = pLastAmazonPricing.getAddedBy();
		}
	}
/*
    public AmazonPricingHistory(
			AmazonPricingID pricingID,
	VendorProductID vendorProductID,
	ProductCondition fProductCondition,
	int fOffertCount,
	PriceCurrency priceCurrency,
	Double fBuyboxItemPrice,
	Double fBuyboxShippingPrice,
	boolean fBuyboxFBA,
	boolean fBuyboxSellerAmazon,
	Double fSecondaryItemPrice,
	Double fSecondaryShippingPrice,
	boolean fSecondaryFBA,
	Timestamp fDownloaded,
	Timestamp fAdded,
	UsersID fAddedBy)
	{
		this.fAmazonPricingID = pricingID;
		this.fVendorProductID =vendorProductID;
		this.fProductCondition =fProductCondition;
		this.fOfferCount =fOffertCount;
		this.fPriceCurrency =priceCurrency;
		this.fBuyboxItemPrice =fBuyboxItemPrice;
		this.fBuyboxShippingPrice =fBuyboxShippingPrice;
		this.fBuyboxFBA =fBuyboxFBA;
		this.fBuyboxSellerAmazon =fBuyboxSellerAmazon;
		this.fSecondaryItemPrice =fSecondaryItemPrice;
		this.fSecondaryShippingPrice =fSecondaryShippingPrice;
		this.fSecondaryFBA =fSecondaryFBA;
		this.fDownloaded =fDownloaded;
		this.fAdded =fAdded;
		this.fAddedBy =fAddedBy;
	}
*/
	public static AmazonPricingHistory newInstance(Path pJsonFile) throws IOException
	{
		return fromJson(GSON, pJsonFile, AmazonPricingHistory.class);
	}

	public static AmazonPricingHistory newInstance(String pJsonString)
	{
		return fromJson(GSON, pJsonString, AmazonPricingHistory.class);
	}

	public static boolean hasChanged(AmazonPricing pAmazonPricing, AmazonPricing pLastAmazonPricing)
	{
		if (pLastAmazonPricing == null || pLastAmazonPricing.isNewRecord())
			return false; // Not creating history for initial insert

		return (!CompareUtil.equals(pAmazonPricing.getVendorProductID(), pLastAmazonPricing.getVendorProductID())
				|| !CompareUtil.equals(pAmazonPricing.getProductCondition(), pLastAmazonPricing.getProductCondition())
				|| pAmazonPricing.getOfferCount() != pLastAmazonPricing.getOfferCount()
				|| !CompareUtil.equals(pAmazonPricing.getPriceCurrency(), pLastAmazonPricing.getPriceCurrency())
				|| pAmazonPricing.getBuyboxItemPrice() != pLastAmazonPricing.getBuyboxItemPrice()
				|| pAmazonPricing.getBuyboxShippingPrice() != pLastAmazonPricing.getBuyboxShippingPrice()
				|| pAmazonPricing.isBuyboxFBA() != pLastAmazonPricing.isBuyboxFBA()
				|| pAmazonPricing.getBuyboxSellerAmazon() != pLastAmazonPricing.getBuyboxSellerAmazon()
				|| pAmazonPricing.getSecondaryItemPrice() != pLastAmazonPricing.getSecondaryItemPrice()
				|| pAmazonPricing.getSecondaryShippingPrice() != pLastAmazonPricing.getSecondaryShippingPrice()
				|| pAmazonPricing.isSecondaryFBA() != pLastAmazonPricing.isSecondaryFBA()
				|| !CompareUtil.equals(pAmazonPricing.getDownloaded(), pLastAmazonPricing.getDownloaded())
		);
	}

	public static History buildAmazonPricingHistory(AmazonPricing pAmazonPricing, AmazonPricing pLastAmazonPricing,
			UsersID pChangedBy)
	{
		History history = null;
		if (hasChanged(pAmazonPricing, pLastAmazonPricing))
		{
			AmazonPricingHistory amazonPricingHistory = new AmazonPricingHistory(pAmazonPricing.getRecordID(),
					pLastAmazonPricing);
			history = new History(pAmazonPricing.getRecordID(), pChangedBy);
			history.setPriorValue(amazonPricingHistory.toJsonString());
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

	public VendorProductID getVendorProductID()
	{
		return this.fVendorProductID;
	}

	public ProductCondition getProductCondition()
	{
		return this.fProductCondition;
	}

	public int getOfferCount()
	{
		return this.fOfferCount;
	}

	public PriceCurrency getPriceCurrency()
	{
		return this.fPriceCurrency;
	}

	public Double getBuyboxItemPrice()
	{
		return this.fBuyboxItemPrice;
	}

	public Double getBuyboxShippingPrice()
	{
		return this.fBuyboxShippingPrice;
	}

	public boolean getBuyBoxFBA()
	{
		return this.fBuyboxFBA;
	}

	public boolean getBuyboxSellerAmazon()
	{
		return this.fBuyboxSellerAmazon;
	}

	public Double getSecondaryItemPrice()
	{
		return this.fSecondaryItemPrice;
	}

	public Double getSecondaryShippingPrice()
	{
		return this.fSecondaryShippingPrice;
	}

	public boolean getSecondaryFBA()
	{
		return this.fSecondaryFBA;
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
