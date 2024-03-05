package com.figstreet.biz.vendorproduct;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import com.figstreet.biz.common.AmazonMarketplaceJsonAdapter;
import com.figstreet.biz.common.PriceCurrencyJsonAdapter;
import com.figstreet.biz.product.ProductIDJsonAdapter;
import com.figstreet.biz.users.UsersIDJsonAdapter;
import com.figstreet.biz.vendor.VendorIDJsonAdapter;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.common.AmazonMarketplace;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.history.History;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorproduct.VendorProduct;
import com.figstreet.data.vendorproduct.VendorProductID;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class VendorProductHistory extends JsonDataObject
{
	private static final long serialVersionUID = -4100612292337347067L;

	private static final Gson GSON = generateGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Gson EXCLUDE_SENSITIVE_GSON = generateSecureGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Type GSON_LIST_TYPE = new TypeToken<List<VendorProductHistory>>()
	{
		/* empty */}.getType();

	@Expose
	@SerializedName(VendorProduct.ID_COLUMN)
	@JsonAdapter(VendorProductIDJsonAdapter.class)
	private VendorProductID fVendorProductID;

	@Expose
	@SerializedName(Vendor.ID_COLUMN)
	@JsonAdapter(VendorIDJsonAdapter.class)
	private VendorID fVendorID;

	@Expose
	@SerializedName(Product.ID_COLUMN)
	@JsonAdapter(ProductIDJsonAdapter.class)
	private ProductID fProductID;

	@Expose
	@SerializedName(VendorProduct.ACTIVE_COLUMN)
	private boolean fActive;

	@Expose
	@SerializedName(VendorProduct.PRICE_COLUMN)
	private double fPrice;

	@Expose
	@SerializedName(VendorProduct.QUANTITY_COLUMN)
	private int fQuantity;

	@Expose
	@SerializedName(VendorProduct.ALTERNATIVE_PRICE_COLUMN)
	private Double fAlternativePrice;

	@Expose
	@SerializedName(VendorProduct.MINIMUM_ORDER_QUANTITY_COLUMN)
	private Integer fMinimumOrderQuantity;

	@Expose
	@SerializedName(VendorProduct.AVAILABLE_ONLINE_COLUMN)
	private boolean fAvailableOnline;

	@Expose
	@SerializedName(VendorProduct.VENDOR_IDENTIFIER_COLUMN)
	private String fVendorIdentifier;

	@Expose
	@SerializedName(VendorProduct.AMAZON_MARKETPLACE_COLUMN)
	@JsonAdapter(AmazonMarketplaceJsonAdapter.class)
	private AmazonMarketplace fAmazonMarketplace;

	@Expose
	@SerializedName(VendorProduct.PRICE_CURRENCY_COLUMN)
	@JsonAdapter(PriceCurrencyJsonAdapter.class)
	private PriceCurrency fPriceCurrency;

	@Expose
	@SerializedName(VendorProduct.ADDED_COLUMN)
	private Timestamp fAdded;

	@Expose
	@SerializedName(VendorProduct.ADDED_BY_COLUMN)
	@JsonAdapter(UsersIDJsonAdapter.class)
	private UsersID fAddedBy;

	protected VendorProductHistory()
	{
		// empty ctor
	}

	public VendorProductHistory(VendorProductID pVendorProductID, VendorProduct pLastVendorProduct)
	{
		this.fVendorProductID = pVendorProductID;
		if (pLastVendorProduct != null)
		{
			this.fActive = pLastVendorProduct.isActive();
			this.fPrice = pLastVendorProduct.getPrice();
			this.fQuantity = pLastVendorProduct.getQuantity();
			this.fAlternativePrice = pLastVendorProduct.getAlternativePrice();
			this.fMinimumOrderQuantity = pLastVendorProduct.getMinimumOrderQuantity();
			this.fAvailableOnline = pLastVendorProduct.isAvailableOnline();
			this.fVendorIdentifier = pLastVendorProduct.getVendorIdentifier();
			this.fAmazonMarketplace = pLastVendorProduct.getAmazonMarketplace();
			this.fPriceCurrency = pLastVendorProduct.getPriceCurrency();
			this.fAdded = pLastVendorProduct.getAdded();
			this.fAddedBy = pLastVendorProduct.getAddedBy();
		}
	}

	public static VendorProductHistory newInstance(Path pJsonFile) throws IOException
	{
		return fromJson(GSON, pJsonFile, VendorProductHistory.class);
	}

	public static VendorProductHistory newInstance(String pJsonString)
	{
		return fromJson(GSON, pJsonString, VendorProductHistory.class);
	}

	public static boolean hasChanged(VendorProduct pVendorProduct, VendorProduct pLastVendorProduct)
	{
		if (pLastVendorProduct == null || pLastVendorProduct.isNewRecord())
			return false; // Not creating history for initial insert

		return (pVendorProduct.isActive() != pLastVendorProduct.isActive()
				|| pVendorProduct.getPrice() != pLastVendorProduct.getPrice()
				|| pVendorProduct.getQuantity() != pLastVendorProduct.getQuantity()
				|| pVendorProduct.getAlternativePrice() != pLastVendorProduct.getAlternativePrice()
				|| pVendorProduct.getMinimumOrderQuantity() != pLastVendorProduct.getMinimumOrderQuantity()
				|| pVendorProduct.isAvailableOnline() != pLastVendorProduct.isAvailableOnline()
				|| !CompareUtil.equalsString(pVendorProduct.getVendorIdentifier(),
						pLastVendorProduct.getVendorIdentifier(), true)
				|| !CompareUtil.equals(pVendorProduct.getAmazonMarketplace(), pLastVendorProduct.getAmazonMarketplace())
				|| !CompareUtil.equals(pVendorProduct.getPriceCurrency(), pLastVendorProduct.getPriceCurrency())
				//Not evaluating Downloaded for history
				);
	}

	public static History buildVendorProductHistory(VendorProduct pVendorProduct, VendorProduct pLastVendorProduct,
			UsersID pChangedBy)
	{
		History history = null;
		if (hasChanged(pVendorProduct, pLastVendorProduct))
		{
			VendorProductHistory vendorProductHistory = new VendorProductHistory(pVendorProduct.getRecordID(),
					pLastVendorProduct);
			history = new History(pVendorProduct.getProductID(), pVendorProduct.getVendorID(), pChangedBy);
			history.setPriorValue(vendorProductHistory.toJsonString());
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

	public Double getPrice()
	{
		return this.fPrice;
	}

	public Integer getQuantity()
	{
		return this.fQuantity;
	}

	public Double getAlternativePrice()
	{
		return this.fAlternativePrice;
	}

	public Integer getMinimumOrderQuantity()
	{
		return this.fMinimumOrderQuantity;
	}

	public boolean isAvailableOnline()
	{
		return this.fAvailableOnline;
	}

	public String getVendorIdentifier()
	{
		return this.fVendorIdentifier;
	}

	public Timestamp getAdded()
	{
		return this.fAdded;
	}

	public UsersID getAddedBy()
	{
		return this.fAddedBy;
	}


			public AmazonMarketplace getAmazonMarketplace() {
			return fAmazonMarketplace;
		}

			public PriceCurrency getPriceCurrency() {
			return fPriceCurrency;
		}

}