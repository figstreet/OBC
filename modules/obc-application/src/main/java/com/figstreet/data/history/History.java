package com.figstreet.data.history;

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

import com.figstreet.core.DateUtil;
import com.figstreet.core.HibernateDBConnector;
import com.figstreet.core.HibernateDatabaseObject;
import com.figstreet.data.amazonpricing.AmazonPricingID;
import com.figstreet.data.amazonsalesrank.AmazonSalesRankID;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendoraddress.VendorAddressID;
import com.figstreet.data.vendorcontact.VendorContactID;

@Entity
@Table(name = "history")
public class History extends HibernateDatabaseObject<HistoryID>
{
	private static final long serialVersionUID = -1329431905655794949L;

	public static final String ID_COLUMN = "hiid";
	public static final String PRODUCTID_COLUMN = "hi_prid";
	public static final String VENDORID_COLUMN = "hi_vdid";
	public static final String VENDORADDRESSID_COLUMN = "hi_vdaid";
	public static final String VENDORCONTACTID_COLUMN = "hi_vdcid";
	public static final String TYPE_COLUMN = "hi_type";
	public static final String PRIOR_VALUE_COLUMN = "hi_prior_value";
	public static final String AMAZONPRICINGID_COLUMN = "hi_azpid";
	public static final String AMAZONSALESRANKID_COLUMN = "hi_azsrid";
	public static final String CHANGED_COLUMN = "hi_changed_dt";
	public static final String ADDED_COLUMN = "hi_added_dt";
	public static final String ADDED_BY_COLUMN = "hi_added_by";

	private ProductID fProductID;
	private VendorID fVendorID;
	private VendorAddressID fVendorAddressID;
	private VendorContactID fVendorContactID;
	private HistoryType fType;
	private String fPriorValue;
	private AmazonPricingID fAmazonPricingID;
	private AmazonSalesRankID fAmazonSalesRankID;

	private Timestamp fChanged;

	@Transient
	public static final HibernateDBConnector<History, HistoryList, HistoryID> DB_CONNECTOR = new HibernateDBConnector<History, HistoryList, HistoryID>(
			History.class, "History", HistoryList.class, HistoryID.class, ID_COLUMN);

	protected History()
	{
		// empty ctor;
	}

	public History(ProductID pProductID, UsersID pAddedBy)
	{
		this(pProductID, null, DateUtil.now(), pAddedBy);
	}

	public History(VendorID pVendorID, UsersID pAddedBy)
	{
		this(null, pVendorID, DateUtil.now(), pAddedBy);
	}

	public History(ProductID pProductID, VendorID pVendorID, UsersID pAddedBy)
	{
		this(pProductID, pVendorID, DateUtil.now(), pAddedBy);
	}

	public History(ProductID pProductID, VendorID pVendorID, Timestamp pChanged, UsersID pAddedBy)
	{
		HistoryType type = null;
		if (pProductID != null && pVendorID != null)
			type = HistoryType.VENDOR_PRODUCT_HISTORY;
		else if (pProductID != null)
			type = HistoryType.PRODUCT_HISTORY;
		else if (pVendorID != null)
			type = HistoryType.VENDOR_HISTORY;

		this.fType = type;
		this.fProductID = pProductID;
		this.fVendorID = pVendorID;
		this.fChanged = pChanged;
		this.fAddedBy = pAddedBy;
	}

	public History(VendorAddressID pVendorAddressID, UsersID pAddedBy)
	{
		this.fVendorAddressID = pVendorAddressID;
		this.fType = HistoryType.VENDOR_ADDRESS_HISTORY;
		this.fChanged = DateUtil.now();
		this.fAddedBy = pAddedBy;
	}

	public History(VendorContactID pVendorContactID, UsersID pAddedBy)
	{
		this.fVendorContactID = pVendorContactID;
		this.fType = HistoryType.VENDOR_CONTACT_HISTORY;
		this.fChanged = DateUtil.now();
		this.fAddedBy = pAddedBy;
	}

	public History(AmazonPricingID pAmazonPricingID, UsersID pAddedBy)
	{
		this.fAmazonPricingID = pAmazonPricingID;
		this.fType = HistoryType.AMAZON_PRICING_HISTORY;
		this.fChanged = DateUtil.now();
		this.fAddedBy = pAddedBy;
	}

	public History(AmazonSalesRankID pAmazonSalesRankID, UsersID pAddedBy)
	{
		this.fAmazonSalesRankID = pAmazonSalesRankID;
		this.fType = HistoryType.AMAZON_SALES_RANK_HISTORY;
		this.fChanged = DateUtil.now();
		this.fAddedBy = pAddedBy;
	}

	public static History findByHistoryID(HistoryID pHistoryID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pHistoryID, false);
	}

	public static History getByHistoryID(HistoryID pHistoryID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pHistoryID, true);
	}

	@Column(name = PRODUCTID_COLUMN)
	@Type(type = "com.figstreet.data.product.ProductID")
	public ProductID getProductID()
	{
		return this.fProductID;
	}

	public void setProductID(ProductID pProductID)
	{
		this.fProductID = pProductID;
	}

	@Column(name = VENDORID_COLUMN)
	@Type(type = "com.figstreet.data.vendor.VendorID")
	public VendorID getVendorID()
	{
		return this.fVendorID;
	}

	public void setVendorID(VendorID pVendorID)
	{
		this.fVendorID = pVendorID;
	}

	@Column(name = VENDORADDRESSID_COLUMN)
	@Type(type = "com.figstreet.data.vendoraddress.VendorAddressID")
	public VendorAddressID getVendorAddressID()
	{
		return this.fVendorAddressID;
	}

	public void setVendorAddressID(VendorAddressID pVendorAddressID)
	{
		this.fVendorAddressID = pVendorAddressID;
	}

	@Column(name = VENDORCONTACTID_COLUMN)
	@Type(type = "com.figstreet.data.vendorcontact.VendorContactID")
	public VendorContactID getVendorContactID()
	{
		return this.fVendorContactID;
	}

	public void setVendorContactID(VendorContactID pVendorContactID)
	{
		this.fVendorContactID = pVendorContactID;
	}

	@Column(name = TYPE_COLUMN)
	@Type(type = "com.figstreet.data.history.HistoryType")
	public HistoryType getType()
	{
		return this.fType;
	}

	public void setType(HistoryType pType)
	{
		this.fType = pType;
	}

	@Column(name = PRIOR_VALUE_COLUMN)
	public String getPriorValue()
	{
		return this.fPriorValue;
	}

	public void setPriorValue(String pPriorValue)
	{
		this.fPriorValue = pPriorValue;
	}

	@Column(name = AMAZONPRICINGID_COLUMN)
	@Type(type = "com.figstreet.data.amazonpricing.AmazonPricingID")
	public AmazonPricingID getAmazonPricingID()
	{
		return this.fAmazonPricingID;
	}

	public void setAmazonPricingID(AmazonPricingID pAmazonPricingID)
	{
		this.fAmazonPricingID = pAmazonPricingID;
	}

	@Column(name = AMAZONSALESRANKID_COLUMN)
	@Type(type = "com.figstreet.data.amazonsalesrank.AmazonSalesRankID")
	public AmazonSalesRankID getAmazonSalesRankID()
	{
		return this.fAmazonSalesRankID;
	}

	public void setAmazonSalesRankID(AmazonSalesRankID pAmazonSalesRankID)
	{
		this.fAmazonSalesRankID = pAmazonSalesRankID;
	}

	@Column(name = CHANGED_COLUMN)
	public Timestamp getChanged()
	{
		return this.fChanged;
	}

	public void setChanged(Timestamp pChanged)
	{
		this.fChanged = pChanged;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.history.HistoryID")
	public HistoryID getRecordID()
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
	@Transient
	public HibernateDBConnector<History, HistoryList, HistoryID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
