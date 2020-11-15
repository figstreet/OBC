package com.figstreet.data.productoption;

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
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;

@Entity
@Table(name = "productoption")
public class ProductOption extends HibernateDatabaseObject<ProductOptionID>
{
	private static final long serialVersionUID = 1879637108741928852L;

	public static final String ID_COLUMN = "proid";
	public static final String ACTIVE_COLUMN = "pro_active";
	public static final String PRODUCTID_COLUMN = "pro_prid";
	public static final String SIMILAR_PRODUCTID_COLUMN = "pro_similar_prid";
	public static final String PRICE_DIFFERENCE_COLUMN = "pro_optionpricediff";
	public static final String ADDED_COLUMN = "pro_added_dt";
	public static final String ADDED_BY_COLUMN = "pro_added_by";
	public static final String LASTUPDATED_COLUMN = "pro_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "pro_lastupdated_by";


	private boolean fActive;
	private ProductID fProductID;
	private ProductID fSimilarProductID;
	private Double fPriceDifference;

	@Transient
	public static final HibernateDBConnector<ProductOption, ProductOptionList, ProductOptionID> DB_CONNECTOR = new HibernateDBConnector<ProductOption, ProductOptionList, ProductOptionID>(
			ProductOption.class, "ProductOption", ProductOptionList.class, ProductOptionID.class, ID_COLUMN);

	protected ProductOption()
	{
		//empty ctor;
	}

	public ProductOption(ProductID pProductID, ProductID pSimilarProductID, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fProductID = pProductID;
		this.fSimilarProductID = pSimilarProductID;
		this.fAddedBy = pAddedBy;
	}

	public static ProductOption findByProductOptionID(ProductOptionID pProductOptionID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pProductOptionID, false);
	}

	public static ProductOption getBypProductOptionID(ProductOptionID pProductOptionID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pProductOptionID, true);
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

	@Column(name = PRODUCTID_COLUMN)
	public ProductID getProductID()
	{
		return this.fProductID;
	}

	public void setProductID(ProductID pProductID)
	{
		this.fProductID = pProductID;
	}

	@Column(name = SIMILAR_PRODUCTID_COLUMN)
	public ProductID getSimilarProductID()
	{
		return this.fSimilarProductID;
	}

	public void setSimilarProductID(ProductID pSimilarProductID)
	{
		this.fSimilarProductID = pSimilarProductID;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.productoption.ProductOptionID")
	public ProductOptionID getRecordID()
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
	public HibernateDBConnector<ProductOption, ProductOptionList, ProductOptionID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
