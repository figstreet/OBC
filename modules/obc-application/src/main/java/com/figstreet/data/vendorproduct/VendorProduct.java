package com.figstreet.data.vendorproduct;

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
import com.figstreet.data.common.AmazonMarketplace;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;

@Entity
@Table(name = VendorProduct.TABLE_NAME)
public class VendorProduct extends HibernateDatabaseObject<VendorProductID>
{
	private static final long serialVersionUID = -2184983325917377383L;

	public static final String TABLE_NAME = "vendor_product";

	public static final String ID_COLUMN = "vpid";
	public static final String VENDORID_COLUMN  = "vp_vdid";
	public static final String PRODUCTID_COLUMN  = "vp_prid";
	public static final String ACTIVE_COLUMN = "vp_active";
	public static final String PRICE_COLUMN = "vp_price";
	public static final String QUANTITY_COLUMN = "vp_quantity";
	public static final String ALTERNATIVE_PRICE_COLUMN = "vp_alternative_price";
	public static final String MINIMUM_ORDER_QUANTITY_COLUMN = "vp_min_order_quantity";
	public static final String AVAILABLE_ONLINE_COLUMN = "vp_available_online";
	public static final String VENDOR_IDENTIFIER_COLUMN = "vp_vendor_identifier";
	public static final String AMAZON_MARKETPLACE_COLUMN = "vp_amazon_marketplace";
	public static final String PRICE_CURRENCY_COLUMN = "vp_price_currency";
	public static final String DOWNLOADED_COLUMN = "vp_downloaded";
	public static final String ADDED_COLUMN = "vp_added_dt";
	public static final String ADDED_BY_COLUMN = "vp_added_by";
	public static final String LASTUPDATED_COLUMN = "vp_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "vp_lastupdated_by";


	private VendorID fVendorID;
	private ProductID fProductID;
	private boolean fActive;
	private double fPrice;
	private PriceCurrency fPriceCurrency;
	private int fQuantity;
	private Double fAlternativePrice;
	private Integer fMinimumOrderQuantity;
	private boolean fAvailableOnline;
	private String fVendorIdentifier;
	private AmazonMarketplace fAmazonMarketplace;
	private Timestamp fDownloaded;

	@Transient
	public static final HibernateDBConnector<VendorProduct, VendorProductList, VendorProductID> DB_CONNECTOR = new HibernateDBConnector<VendorProduct, VendorProductList, VendorProductID>(
			VendorProduct.class, "VendorProduct", VendorProductList.class, VendorProductID.class, ID_COLUMN);

	protected VendorProduct()
	{
		//empty ctor;
	}

	public VendorProduct(VendorID pVendorID, ProductID pProductID, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fVendorID = pVendorID;
		this.fProductID = pProductID;
		this.fAddedBy = pAddedBy;
	}

	public static VendorProduct findByVendorProductID(VendorProductID pVendorProductID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorProductID, false);
	}

	public static VendorProduct getByVendorProductID(VendorProductID pVendorProductID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorProductID, true);
	}

	@Column(name = ACTIVE_COLUMN)
	public boolean isActive()
	{
		return this.fActive;
	}

	public void setActive(boolean pActive)
	{
		this.fActive = pActive;
	}

	@Column(name = VENDORID_COLUMN)
	public VendorID getVendorID()
	{
		return this.fVendorID;
	}

	public void setVendorID(VendorID pVendorID)
	{
		this.fVendorID = pVendorID;
	}

	@Column(name = PRODUCTID_COLUMN)
	public ProductID getProductID()
	{
		return this.fProductID;
	}

	public void setProductID(ProductID pProductID)
	{
		this.fProductID = pProductID;
	}


	@Column(name = PRICE_COLUMN)
	public double getPrice()
	{
		return this.fPrice;
	}

	public void setPrice(double pPrice)
	{
		this.fPrice = pPrice;
	}

	@Column(name = QUANTITY_COLUMN)
	public int getQuantity()
	{
		return this.fQuantity;
	}

	public void setQuantity(int pQuantity)
	{
		this.fQuantity = pQuantity;
	}

	@Column(name = ALTERNATIVE_PRICE_COLUMN)
	public Double getAlternativePrice()
	{
		return this.fAlternativePrice;
	}

	public void setAlternativePrice(Double pAlternativePrice)
	{
		this.fAlternativePrice = pAlternativePrice;
	}

	@Column(name = MINIMUM_ORDER_QUANTITY_COLUMN)
	public Integer getMinimumOrderQuantity()
	{
		return this.fMinimumOrderQuantity;
	}

	public void setMinimumOrderQuantity(Integer pMinimumOrderQuantity)
	{
		this.fMinimumOrderQuantity = pMinimumOrderQuantity;
	}

	@Column(name = AVAILABLE_ONLINE_COLUMN)
	public boolean isAvailableOnline()
	{
		return this.fAvailableOnline;
	}

	public void setAvailableOnline(boolean pAvailableOnline)
	{
		this.fAvailableOnline = pAvailableOnline;
	}

	@Column(name = VENDOR_IDENTIFIER_COLUMN)
	public String getVendorIdentifier()
	{
		return this.fVendorIdentifier;
	}

	public void setVendorIdentifier(String pVendorIdentifier)
	{
		this.fVendorIdentifier = pVendorIdentifier;
	}

	@Column(name = AMAZON_MARKETPLACE_COLUMN)
	@Type(type = "com.figstreet.data.common.AmazonMarketplace")
	public AmazonMarketplace getAmazonMarketplace()
	{
		return this.fAmazonMarketplace;
	}

	public void setAmazonMarketplace(AmazonMarketplace pAmazonMarketplace)
	{
		this.fAmazonMarketplace = pAmazonMarketplace;
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
	@Type(type = "com.figstreet.data.vendorproduct.VendorProductID")
	public VendorProductID getRecordID()
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
	public HibernateDBConnector<VendorProduct, VendorProductList, VendorProductID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
