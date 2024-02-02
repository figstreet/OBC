package com.figstreet.data.productrating;

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
@Table(name = "productrating")
public class ProductRating extends HibernateDatabaseObject<ProductRatingID>
{
	private static final long serialVersionUID = -7654521542950363502L;

	public static final String ID_COLUMN = "prrid";
	public static final String PRODUCTID_COLUMN = "prr_prid";
	public static final String USERID_COLUMN = "prr_usid";
	public static final String RATING_COLUMN = "prr_rating";
	public static final String NOTES_COLUMN = "prr_notes";
	public static final String ADDED_COLUMN = "prr_added_dt";
	public static final String ADDED_BY_COLUMN = "prr_added_by";
	public static final String LASTUPDATED_COLUMN = "prr_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "prr_lastupdated_by";

	private ProductID fProductID;
	private UsersID fUsersID;
	private double fRating;
	private String fNotes;


	@Transient
	public static final HibernateDBConnector<ProductRating, ProductRatingList, ProductRatingID> DB_CONNECTOR = new HibernateDBConnector<ProductRating, ProductRatingList, ProductRatingID>(
			ProductRating.class, "ProductRating", ProductRatingList.class, ProductRatingID.class, ID_COLUMN);

	protected ProductRating()
	{
		//empty ctor;
	}

	public ProductRating(ProductID pProductID, UsersID pUsersID, Double pRating, UsersID pAddedBy)
	{

		this.fProductID = pProductID;
		this.fUsersID = pUsersID;
		this.fRating = pRating;
		this.fAddedBy = pAddedBy;
	}

	public static ProductRating findByProductRatingID(ProductRatingID pProductRatingID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pProductRatingID, false);
	}

	public static ProductRating getByProductRatingID(ProductRatingID pProductRatingID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pProductRatingID, true);
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

	@Column(name = USERID_COLUMN)
	public UsersID getUsersID()
	{
		return this.fUsersID;
	}

	public void setUsersID(UsersID pUsersID)
	{
		this.fUsersID = pUsersID;
	}

	@Column(name = RATING_COLUMN)
	public Double getRating()
	{
		return this.fRating;
	}

	public void setRating(Double pRating)
	{
		this.fRating = pRating;
	}

	@Column(name = NOTES_COLUMN)
	public String getNotes()
	{
		return this.fNotes;
	}

	public void setNotes(String pNotes)
	{
		this.fNotes = pNotes;
	}


	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.productrating.ProductRatingID")
	public ProductRatingID getRecordID()
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
	public HibernateDBConnector<ProductRating, ProductRatingList, ProductRatingID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
