package com.figstreet.data.amazonpricing;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.figstreet.core.HibernateDBConnector;
import com.figstreet.core.HibernateDatabaseObject;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.common.ProductCondition;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;

@Entity
@Table(name = "amazonpricing")
public class AmazonPricing extends HibernateDatabaseObject<AmazonPricingID>
{
	private static final long serialVersionUID = -4097425889161860337L;

	public static final String ID_COLUMN = "azpid";
	public static final String VENDORPRODUCTID_COLUMN = "azp_vpid";
	public static final String PRODUCT_CONDITION_COLUMN = "azp_product_condition";
	public static final String OFFER_COUNT_COLUMN = "azp_offer_count";
	public static final String PRICE_CURRENCY_COLUMN = "azp_price_currency";
	public static final String BUYBOX_ITEM_PRICE_COLUMN = "azp_buybox_item_price";
	public static final String BUYBOX_SHIPPING_PRICE_COLUMN = "azp_buybox_shipping_price";
	public static final String BUYBOX_FBA_COLUMN = "azp_buybox_fba";
	public static final String BUYBOX_SELLER_AMAZON_COLUMN = "azp_buybox_seller_amazon";
	public static final String SECONDARY_ITEM_PRICE_COLUMN = "azp_secondary_item_price";
	public static final String SECONDARY_SHIPPING_PRICE_COLUMN = "azp_secondary_shipping_price";
	public static final String SECONDARY_FBA_COLUMN = "azp_secondary_fba";
	public static final String DOWNLOADED_COLUMN = "azp_downloaded";
	public static final String ADDED_COLUMN = "azp_added_dt";
	public static final String ADDED_BY_COLUMN = "azp_added_by";
	public static final String LASTUPDATED_COLUMN = "azp_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "azp_lastupdated_by";

	private VendorProductID fVendorProductID;
	private ProductCondition fProductCondition;
	private int fOfferCount;
	private PriceCurrency fPriceCurrency;
	private Double fBuyboxItemPrice;
	private Double fBuyboxShippingPrice;
	private boolean fBuyboxFBA;
	private boolean fBuyboxSellerAmazon;
	private Double fSecondaryItemPrice;
	private Double fSecondaryShippingPrice;
	private boolean fSecondaryFBA;
	private Timestamp fDownloaded;

	@Transient
	public static final HibernateDBConnector<AmazonPricing, AmazonPricingList, AmazonPricingID> DB_CONNECTOR = new HibernateDBConnector<AmazonPricing, AmazonPricingList, AmazonPricingID>(
			AmazonPricing.class, "AmazonPricing", AmazonPricingList.class, AmazonPricingID.class, ID_COLUMN);

	protected AmazonPricing()
	{
		// empty ctor;
	}

	public AmazonPricing(VendorProductID pVendorProductID, UsersID pAddedBy)
	{
		this.fVendorProductID = pVendorProductID;
		this.fAddedBy = pAddedBy;
	}

	public static AmazonPricing findByAmazonPricingID(AmazonPricingID pAmazonPricingID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pAmazonPricingID, false);
	}

	public static AmazonPricing getByAmazonPricingID(AmazonPricingID pAmazonPricingID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pAmazonPricingID, true);
	}

	@Column(name = VENDORPRODUCTID_COLUMN)
	@Type(type = "com.figstreet.data.vendorproduct.VendorProductID")
	public VendorProductID getVendorProductID()
	{
		return this.fVendorProductID;
	}

	public void setVendorProductID(VendorProductID pVendorProductID)
	{
		this.fVendorProductID = pVendorProductID;
	}

	@Column(name = PRODUCT_CONDITION_COLUMN)
	@Type(type = "com.figstreet.data.common.ProductCondition")
	public ProductCondition getProductCondition()
	{
		return this.fProductCondition;
	}

	public void setProductCondition(ProductCondition pProductCondition)
	{
		this.fProductCondition = pProductCondition;
	}

	@Column(name = OFFER_COUNT_COLUMN)
	public int getOfferCount()
	{
		return this.fOfferCount;
	}

	public void setOfferCount(int pOfferCount)
	{
		this.fOfferCount = pOfferCount;
	}

	@Column(name = PRICE_CURRENCY_COLUMN)
	@Type(type = "com.figstreet.data.common.PriceCurrency")
	public PriceCurrency getPriceCurrency()
	{
		return this.fPriceCurrency;
	}

	public void setPriceCurrency(PriceCurrency pPriceCurrency)
	{
		this.fPriceCurrency = pPriceCurrency;
	}

	@Column(name = BUYBOX_ITEM_PRICE_COLUMN)
	public Double getBuyboxItemPrice()
	{
		return this.fBuyboxItemPrice;
	}

	public void setBuyboxItemPrice(Double pBuyboxItemPrice)
	{
		this.fBuyboxItemPrice = pBuyboxItemPrice;
	}

	@Column(name = BUYBOX_SHIPPING_PRICE_COLUMN)
	public Double getBuyboxShippingPrice()
	{
		return this.fBuyboxShippingPrice;
	}

	public void setBuyboxShippingPrice(Double pBuyboxShippingPrice)
	{
		this.fBuyboxShippingPrice = pBuyboxShippingPrice;
	}

	@Column(name = BUYBOX_FBA_COLUMN)
	public boolean isBuyboxFBA()
	{
		return this.fBuyboxFBA;
	}

	public void setBuyboxFBA(boolean pBuyboxFBA)
	{
		this.fBuyboxFBA = pBuyboxFBA;
	}

	@Column(name = BUYBOX_SELLER_AMAZON_COLUMN)
	public boolean getBuyboxSellerAmazon()
	{
		return this.fBuyboxSellerAmazon;
	}

	public void setBuyboxSellerAmazon(boolean pBuyboxSellerAmazon)
	{
		this.fBuyboxSellerAmazon = pBuyboxSellerAmazon;
	}

	@Column(name = SECONDARY_ITEM_PRICE_COLUMN)
	public Double getSecondaryItemPrice()
	{
		return this.fSecondaryItemPrice;
	}

	public void setSecondaryItemPrice(Double pSecondaryItemPrice)
	{
		this.fSecondaryItemPrice = pSecondaryItemPrice;
	}

	@Column(name = SECONDARY_SHIPPING_PRICE_COLUMN)
	public Double getSecondaryShippingPrice()
	{
		return this.fSecondaryShippingPrice;
	}

	public void setSecondaryShippingPrice(Double pSecondaryShippingPrice)
	{
		this.fSecondaryShippingPrice = pSecondaryShippingPrice;
	}

	@Column(name = SECONDARY_FBA_COLUMN)
	public boolean isSecondaryFBA()
	{
		return this.fSecondaryFBA;
	}

	public void setSecondaryFBA(boolean pSecondaryFBA)
	{
		this.fSecondaryFBA = pSecondaryFBA;
	}

	@Column(name = DOWNLOADED_COLUMN)
	public Timestamp getDownloaded()
	{
		return this.fDownloaded;
	}

	public void setDownloaded(Timestamp pDownloaded)
	{
		this.fDownloaded = pDownloaded;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.amazonpricing.AmazonPricingID")
	public AmazonPricingID getRecordID()
	{
		return this.fRecordID;
	}

	@Override
	@Column(name = ADDED_COLUMN)
	public Timestamp getAdded()
	{
		return super.getAdded();
	}

	@Override
	@Column(name = ADDED_BY_COLUMN)
	@Type(type = "com.figstreet.data.users.UsersID")
	public UsersID getAddedBy()
	{
		return super.getAddedBy();
	}

	@Override
	@Column(name = LASTUPDATED_COLUMN)
	public Timestamp getLastUpdated()
	{
		return super.getLastUpdated();
	}

	@Override
	@Column(name = LASTUPDATED_BY_COLUMN)
	@Type(type = "com.figstreet.data.users.UsersID")
	public UsersID getLastUpdatedBy()
	{
		return super.getLastUpdatedBy();
	}

	@Override
	@Transient
	public HibernateDBConnector<AmazonPricing, AmazonPricingList, AmazonPricingID> getDBConnector()
	{
		return DB_CONNECTOR;
	}

	public void clearPricingFields()
	{
		this.fOfferCount = 0;
		this.fPriceCurrency = null;
		this.fBuyboxItemPrice = null;
		this.fBuyboxShippingPrice = null;
		this.fBuyboxFBA = false;
		this.fBuyboxSellerAmazon = false;
		this.fSecondaryItemPrice = null;
		this.fSecondaryShippingPrice = null;
		this.fSecondaryFBA = false;
	}

}
