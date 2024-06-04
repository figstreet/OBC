package com.figstreet.data.product;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.figstreet.core.RecordNotExistException;
import org.hibernate.annotations.Type;

import com.figstreet.core.HibernateDBConnector;
import com.figstreet.core.HibernateDatabaseObject;
import com.figstreet.data.common.MeasurementUnit;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.users.UsersID;

@Entity
@Table(name = "product")
public class Product extends HibernateDatabaseObject<ProductID>
{
	private static final long serialVersionUID = -8924060033670179548L;

	public static final String ID_COLUMN = "prid";
	public static final String ACTIVE_COLUMN = "pr_active";
	public static final String NAME_COLUMN = "pr_name";
	public static final String SHORT_DESCRIPTION_COLUMN = "pr_short_desc";
	public static final String LONG_DESCRIPTION_COLUMN = "pr_long_desc";
	public static final String UPC_COLUMN = "pr_upc";
	public static final String SKU_COLUMN = "pr_sku";
	public static final String LENGTH_COLUMN = "pr_length";
	public static final String WIDTH_COLUMN = "pr_width";
	public static final String HEIGHT_COLUMN = "pr_height";
	public static final String WEIGHT_COLUMN = "pr_weight";
	public static final String LENGTH_UNIT_COLUMN = "pr_length_unit";
	public static final String WIDTH_UNIT_COLUMN = "pr_width_unit";
	public static final String HEIGHT_UNIT_COLUMN = "pr_height_unit";
	public static final String WEIGHT_UNIT_COLUMN = "pr_weight_unit";
	public static final String IMAGE_URL_COLUMN = "pr_image_url";
	public static final String LIST_PRICE_COLUMN = "pr_list_price";
	public static final String PRICE_CURRENCY_COLUMN = "pr_price_currency";
	public static final String ADDED_COLUMN = "pr_added_dt";
	public static final String ADDED_BY_COLUMN = "pr_added_by";
	public static final String LASTUPDATED_COLUMN = "pr_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "pr_lastupdated_by";

	private boolean fActive;
	private String fName;
	private String fShortDescription;
	private String fLongDescription;
	private String fUpc;
	private String fSku;
	private Double fLength;
	private Double fWidth;
	private Double fHeight;
	private Double fWeight;
	private MeasurementUnit fLengthUnit;
	private MeasurementUnit fWidthUnit;
	private MeasurementUnit fHeightUnit;
	private MeasurementUnit fWeightUnit;
	private String fImageUrl;
	private Double fListPrice;
	private PriceCurrency fPriceCurrency;

	@Transient
	public static final HibernateDBConnector<Product, ProductList, ProductID> DB_CONNECTOR = new HibernateDBConnector<Product, ProductList, ProductID>(
			Product.class, "Product", ProductList.class, ProductID.class, ID_COLUMN);

	protected Product()
	{
		// empty ctor;
	}

	public Product(String pName, String pUpc, String pSku, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fName = pName;
		this.fUpc = pUpc;
		this.fSku = pSku;
		this.fAddedBy = pAddedBy;
	}

	public static Product findByProductID(ProductID pProductID)
			throws SQLException, RecordNotExistException
	{
		return DB_CONNECTOR.loadRecord(pProductID, false);
	}

	public static Product getBypProductID(ProductID pProductID)
			throws SQLException, RecordNotExistException
	{
		return DB_CONNECTOR.loadRecord(pProductID, true);
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

	@Column(name = NAME_COLUMN)
	public String getName()
	{
		return this.fName;
	}

	public void setName(String pName)
	{
		this.fName = pName;
	}

	@Column(name = SHORT_DESCRIPTION_COLUMN)
	public String getShortDescription()
	{
		return this.fShortDescription;
	}

	public void setShortDescription(String pShortDescription)
	{
		this.fShortDescription = pShortDescription;
	}

	@Column(name = LONG_DESCRIPTION_COLUMN)
	public String getLongDescription()
	{
		return this.fLongDescription;
	}

	public void setLongDescription(String pLongDescription)
	{
		this.fLongDescription = pLongDescription;
	}

	@Column(name = LENGTH_COLUMN)
	public Double getLength()
	{
		return this.fLength;
	}

	public void setLength(Double pLength)
	{
		this.fLength = pLength;
	}

	@Column(name = WIDTH_COLUMN)
	public Double getWidth()
	{
		return this.fWidth;
	}

	public void setWidth(Double pWidth)
	{
		this.fWidth = pWidth;
	}

	@Column(name = HEIGHT_COLUMN)
	public Double getHeight()
	{
		return this.fHeight;
	}

	public void setHeight(Double pHeight)
	{
		this.fHeight = pHeight;
	}

	@Column(name = WEIGHT_COLUMN)
	public Double getWeight()
	{
		return this.fWeight;
	}

	public void setWeight(Double pWeight)
	{
		this.fWeight = pWeight;
	}

	@Column(name = UPC_COLUMN)
	public String getUpc()
	{
		return this.fUpc;
	}

	public void setUpc(String pUpc)
	{
		this.fUpc = pUpc;
	}

	@Column(name = SKU_COLUMN)
	public String getSku()
	{
		return this.fSku;
	}

	public void setSku(String pSku)
	{
		this.fSku = pSku;
	}

	@Column(name = LENGTH_UNIT_COLUMN)
	@Type(type = "com.figstreet.data.common.MeasurementUnit")
	public MeasurementUnit getLengthUnit()
	{
		return fLengthUnit;
	}

	public void setLengthUnit(MeasurementUnit pLengthUnit)
	{
		this.fLengthUnit = pLengthUnit;
	}

	@Column(name = WIDTH_UNIT_COLUMN)
	@Type(type = "com.figstreet.data.common.MeasurementUnit")
	public MeasurementUnit getWidthUnit()
	{
		return fWidthUnit;
	}

	public void setWidthUnit(MeasurementUnit pWidthUnit)
	{
		this.fWidthUnit = pWidthUnit;
	}

	@Column(name = HEIGHT_UNIT_COLUMN)
	@Type(type = "com.figstreet.data.common.MeasurementUnit")
	public MeasurementUnit getHeightUnit()
	{
		return fHeightUnit;
	}

	public void setHeightUnit(MeasurementUnit pHeightUnit)
	{
		this.fHeightUnit = pHeightUnit;
	}

	@Column(name = WEIGHT_UNIT_COLUMN)
	@Type(type = "com.figstreet.data.common.MeasurementUnit")
	public MeasurementUnit getWeightUnit()
	{
		return fWeightUnit;
	}

	public void setWeightUnit(MeasurementUnit pWeightUnit)
	{
		this.fWeightUnit = pWeightUnit;
	}

	@Column(name = IMAGE_URL_COLUMN)
	public String getImageUrl()
	{
		return fImageUrl;
	}

	public void setImageUrl(String pImageUrl)
	{
		this.fImageUrl = pImageUrl;
	}

	@Column(name = LIST_PRICE_COLUMN)
	public Double getListPrice()
	{
		return fListPrice;
	}

	public void setListPrice(Double pListPrice)
	{
		this.fListPrice = pListPrice;
	}

	@Column(name = PRICE_CURRENCY_COLUMN)
	@Type(type = "com.figstreet.data.common.PriceCurrency")
	public PriceCurrency getPriceCurrency()
	{
		return fPriceCurrency;
	}

	public void setPriceCurrency(PriceCurrency pPriceCurrency)
	{
		this.fPriceCurrency = pPriceCurrency;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.product.ProductID")
	public ProductID getRecordID()
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
	public HibernateDBConnector<Product, ProductList, ProductID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
