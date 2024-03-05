package com.figstreet.biz.product;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import com.figstreet.biz.common.MeasurementUnitJsonAdapter;
import com.figstreet.biz.common.PriceCurrencyJsonAdapter;
import com.figstreet.biz.users.UsersIDJsonAdapter;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.common.MeasurementUnit;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.history.History;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class ProductHistory extends JsonDataObject
{
	private static final long serialVersionUID = 7710517823137959076L;
	private static final Gson GSON = generateGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Gson EXCLUDE_SENSITIVE_GSON = generateSecureGson(DateUtil.ISO8601_TIMESTAMP_FORMAT);
	private static final Type GSON_LIST_TYPE = new TypeToken<List<ProductHistory>>()
	{
		/* empty */}.getType();

	@Expose
	@SerializedName(Product.ID_COLUMN)
	@JsonAdapter(ProductIDJsonAdapter.class)
	private ProductID fProductID;

	@Expose
	@SerializedName(Product.ACTIVE_COLUMN)
	private boolean fActive;

	@Expose
	@SerializedName(Product.NAME_COLUMN)
	private String fName;

	@Expose
	@SerializedName(Product.SHORT_DESCRIPTION_COLUMN)
	private String fShortDescription;

	@Expose
	@SerializedName(Product.LONG_DESCRIPTION_COLUMN)
	private String fLongDescription;

	@Expose
	@SerializedName(Product.UPC_COLUMN)
	private String fUpc;

	@Expose
	@SerializedName(Product.SKU_COLUMN)
	private String fSku;

	@Expose
	@SerializedName(Product.LENGTH_COLUMN)
	private Double fLength;

	@Expose
	@SerializedName(Product.WIDTH_COLUMN)
	private Double fWidth;

	@Expose
	@SerializedName(Product.HEIGHT_COLUMN)
	private Double fHeight;

	@Expose
	@SerializedName(Product.WEIGHT_COLUMN)
	private Double fWeight;

	@Expose
	@SerializedName(Product.LENGTH_UNIT_COLUMN)
	@JsonAdapter(MeasurementUnitJsonAdapter.class)
	private MeasurementUnit fLengthUnit;

	@Expose
	@SerializedName(Product.WIDTH_UNIT_COLUMN)
	@JsonAdapter(MeasurementUnitJsonAdapter.class)
	private MeasurementUnit fWidthUnit;

	@Expose
	@SerializedName(Product.HEIGHT_UNIT_COLUMN)
	@JsonAdapter(MeasurementUnitJsonAdapter.class)
	private MeasurementUnit fHeightUnit;

	@Expose
	@SerializedName(Product.WEIGHT_UNIT_COLUMN)
	@JsonAdapter(MeasurementUnitJsonAdapter.class)
	private MeasurementUnit fWeightUnit;

	@Expose
	@SerializedName(Product.IMAGE_URL_COLUMN)
	private String fImageUrl;

	@Expose
	@SerializedName(Product.LIST_PRICE_COLUMN)
	private Double fListPrice;

	@Expose
	@SerializedName(Product.PRICE_CURRENCY_COLUMN)
	@JsonAdapter(PriceCurrencyJsonAdapter.class)
	private PriceCurrency fPriceCurrency;

	@Expose
	@SerializedName(Product.ADDED_COLUMN)
	private Timestamp fAdded;

	@Expose
	@SerializedName(Product.ADDED_BY_COLUMN)
	@JsonAdapter(UsersIDJsonAdapter.class)
	private UsersID fAddedBy;

	protected ProductHistory()
	{
		// empty ctor
	}

	public ProductHistory(ProductID pProductID, Product pLastProduct)
	{
		this.fProductID = pProductID;
		if (pLastProduct != null)
		{
			this.fActive = pLastProduct.isActive();
			this.fName = pLastProduct.getName();
			this.fShortDescription = pLastProduct.getShortDescription();
			this.fLongDescription = pLastProduct.getLongDescription();
			this.fUpc = pLastProduct.getUpc();
			this.fSku = pLastProduct.getSku();
			this.fLength = pLastProduct.getLength();
			this.fWidth = pLastProduct.getWidth();
			this.fHeight = pLastProduct.getHeight();
			this.fWeight = pLastProduct.getWeight();
			this.fLengthUnit = pLastProduct.getLengthUnit();
			this.fWidthUnit = pLastProduct.getWeightUnit();
			this.fHeightUnit = pLastProduct.getHeightUnit();
			this.fWeightUnit = pLastProduct.getWeightUnit();
			this.fImageUrl = pLastProduct.getImageUrl();
			this.fListPrice = pLastProduct.getListPrice();
			this.fPriceCurrency = pLastProduct.getPriceCurrency();
			this.fAdded = pLastProduct.getAdded();
			this.fAddedBy = pLastProduct.getAddedBy();
		}
	}

	public static ProductHistory newInstance(Path pJsonFile) throws IOException
	{
		return fromJson(GSON, pJsonFile, ProductHistory.class);
	}

	public static ProductHistory newInstance(String pJsonString)
	{
		return fromJson(GSON, pJsonString, ProductHistory.class);
	}

	public static boolean hasChanged(Product pProduct, Product pLastProduct)
	{
		if (pLastProduct == null || pLastProduct.isNewRecord())
			return false; // Not creating history for initial insert

		return (pProduct.isActive() != pLastProduct.isActive()
				|| !CompareUtil.equalsString(pProduct.getName(), pLastProduct.getName(), true)
				|| !CompareUtil.equalsString(pProduct.getShortDescription(), pLastProduct.getShortDescription(), true)
				|| !CompareUtil.equalsString(pProduct.getLongDescription(), pLastProduct.getLongDescription(), true)
				|| !CompareUtil.equalsString(pProduct.getUpc(), pLastProduct.getUpc(), true)
				|| !CompareUtil.equalsString(pProduct.getSku(), pLastProduct.getSku(), true)
				|| !CompareUtil.equals(pProduct.getLength(), pLastProduct.getLength())
				|| !CompareUtil.equals(pProduct.getWidth(), pLastProduct.getWidth())
				|| !CompareUtil.equals(pProduct.getHeight(), pLastProduct.getHeight())
				|| !CompareUtil.equals(pProduct.getWeight(), pLastProduct.getWeight())
				|| !CompareUtil.equals(pProduct.getLengthUnit(), pLastProduct.getLengthUnit())
				|| !CompareUtil.equals(pProduct.getWidthUnit(), pLastProduct.getWidthUnit())
				|| !CompareUtil.equals(pProduct.getHeightUnit(), pLastProduct.getHeightUnit())
				|| !CompareUtil.equals(pProduct.getWeightUnit(), pLastProduct.getWeightUnit())
				|| !CompareUtil.equalsString(pProduct.getImageUrl(), pLastProduct.getImageUrl(), true)
				|| !CompareUtil.equals(pProduct.getListPrice(), pLastProduct.getListPrice())
				|| !CompareUtil.equals(pProduct.getPriceCurrency(), pLastProduct.getPriceCurrency()));
	}

	public static History buildProductHistory(Product pProduct, Product pLastProduct, UsersID pChangedBy)
	{
		History history = null;
		if (hasChanged(pProduct, pLastProduct))
		{
			ProductHistory productHistory = new ProductHistory(pProduct.getRecordID(), pLastProduct);
			history = new History(pProduct.getRecordID(), pChangedBy);
			history.setPriorValue(productHistory.toJsonString());
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

	public String getShortDescription()
	{
		return this.fShortDescription;
	}

	public String getLongDescription()
	{
		return this.fLongDescription;
	}

	public String getUpc()
	{
		return this.fUpc;
	}

	public String getSku()
	{
		return this.fSku;
	}

	public Double getLength()
	{
		return this.fLength;
	}

	public Double getWidth()
	{
		return this.fWidth;
	}

	public Double getHeight()
	{
		return this.fHeight;
	}

	public Double getWeight()
	{
		return this.fWeight;
	}

	public Timestamp getAdded()
	{
		return this.fAdded;
	}

	public UsersID getAddedBy()
	{
		return this.fAddedBy;
	}

	public MeasurementUnit getLengthUnit() {
		return fLengthUnit;
	}

	public MeasurementUnit getWidthUnit() {
		return fWidthUnit;
	}

	public MeasurementUnit getHeightUnit() {
		return fHeightUnit;
	}

	public MeasurementUnit getWeightUnit() {
		return fWeightUnit;
	}

	public String getImageUrl() {
		return fImageUrl;
	}

	public Double getListPrice() {
		return fListPrice;
	}

	public PriceCurrency getPriceCurrency() {
		return fPriceCurrency;
	}

}