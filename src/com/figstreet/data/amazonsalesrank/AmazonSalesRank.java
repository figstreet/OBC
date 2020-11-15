package com.figstreet.data.amazonsalesrank;

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
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;

@Entity
@Table(name = "amazonsalesrank")
public class AmazonSalesRank extends HibernateDatabaseObject<AmazonSalesRankID>
{
	private static final long serialVersionUID = -7614221495770616857L;

	public static final String ID_COLUMN = "azsrid";
	public static final String VENDORPRODUCTID_COLUMN = "azsr_vpid";
	public static final String PRIMARY_RANK_COLUMN = "azsr_primary_rank";
	public static final String PRIMARY_RANK_CATEGORY_COLUMN = "azsr_primary_rank_category";
	public static final String SECONDARY_RANK_COLUMN = "azsr_secondary_rank";
	public static final String SECONDARY_RANK_CATEGORY_COLUMN = "azsr_secondary_rank_category";
	public static final String DOWNLOADED_COLUMN = "azsr_downloaded";
	public static final String ADDED_COLUMN = "azsr_added_dt";
	public static final String ADDED_BY_COLUMN = "azsr_added_by";
	public static final String LASTUPDATED_COLUMN = "azsr_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "azsr_lastupdated_by";

	private VendorProductID fVendorProductID;
	private Integer fPrimaryRank;
	private AmazonSalesRankCategory fPrimarySalesRankCategory;
	private Integer fSecondaryRank;
	private AmazonSalesRankCategory fSecondarySalesRankCategory;
	private Timestamp fDownloaded;

	@Transient
	public static final HibernateDBConnector<AmazonSalesRank, AmazonSalesRankList, AmazonSalesRankID> DB_CONNECTOR = new HibernateDBConnector<AmazonSalesRank, AmazonSalesRankList, AmazonSalesRankID>(
			AmazonSalesRank.class, "AmazonSalesRank", AmazonSalesRankList.class, AmazonSalesRankID.class, ID_COLUMN);

	protected AmazonSalesRank()
	{
		// empty ctor;
	}

	public AmazonSalesRank(VendorProductID pVendorProductID, UsersID pAddedBy)
	{
		this.fVendorProductID = pVendorProductID;
		this.fAddedBy = pAddedBy;
	}

	public static AmazonSalesRank findByAmazonSalesRankID(AmazonSalesRankID pAmazonSalesRankID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pAmazonSalesRankID, false);
	}

	public static AmazonSalesRank getByAmazonSalesRankID(AmazonSalesRankID pAmazonSalesRankID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pAmazonSalesRankID, true);
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

	@Column(name = PRIMARY_RANK_COLUMN)
	public Integer getPrimaryRank()
	{
		return this.fPrimaryRank;
	}

	public void setPrimaryRank(Integer pPrimaryRank)
	{
		this.fPrimaryRank = pPrimaryRank;
	}

	@Column(name = PRIMARY_RANK_CATEGORY_COLUMN)
	@Type(type = "com.figstreet.data.common.AmazonSalesRankCategory")
	public AmazonSalesRankCategory getPrimarySalesRankCategory()
	{
		return this.fPrimarySalesRankCategory;
	}

	public void setPrimarySalesRankCategory(AmazonSalesRankCategory pAmazonSalesRankCategory)
	{
		this.fPrimarySalesRankCategory = pAmazonSalesRankCategory;
	}

	@Column(name = SECONDARY_RANK_COLUMN)
	public Integer getSecondaryRank()
	{
		return this.fSecondaryRank;
	}

	public void setSecondaryRank(Integer pSecondaryRank)
	{
		this.fSecondaryRank = pSecondaryRank;
	}

	@Column(name = SECONDARY_RANK_CATEGORY_COLUMN)
	@Type(type = "com.figstreet.data.common.AmazonSalesRankCategory")
	public AmazonSalesRankCategory getSecondarySalesRankCategory()
	{
		return this.fSecondarySalesRankCategory;
	}

	public void setSecondarySalesRankCategory(AmazonSalesRankCategory pAmazonSalesRankCategory)
	{
		this.fSecondarySalesRankCategory = pAmazonSalesRankCategory;
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
	@Type(type = "com.figstreet.data.amazonsalesrank.AmazonSalesRankID")
	public AmazonSalesRankID getRecordID()
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
	public HibernateDBConnector<AmazonSalesRank, AmazonSalesRankList, AmazonSalesRankID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
