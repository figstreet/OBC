package com.figstreet.data.vendor;

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
import com.figstreet.data.users.UsersID;

@Entity
@Table(name = "vendor")
public class Vendor extends HibernateDatabaseObject<VendorID>
{

	private static final long serialVersionUID = -4443551121712313492L;

	public static final String ID_COLUMN = "vdid";
	public static final String ACTIVE_COLUMN = "vd_active";
	public static final String NAME_COLUMN = "vd_name";
	public static final String WEBSITE_COLUMN = "vd_website";
	public static final String ADDED_COLUMN = "vd_added_dt";
	public static final String ADDED_BY_COLUMN = "vd_added_by";
	public static final String LASTUPDATED_COLUMN = "vd_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "vd_lastupdated_by";

	private boolean fActive;
	private String fName;
	private String fWebsite;

	@Transient
	public static final HibernateDBConnector<Vendor, VendorList, VendorID> DB_CONNECTOR = new HibernateDBConnector<Vendor, VendorList, VendorID>(
			Vendor.class, "Vendor", VendorList.class, VendorID.class, ID_COLUMN);

	protected Vendor()
	{
		// empty ctor;
	}

	public Vendor(String pName, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fName = pName;
		this.fAddedBy = pAddedBy;
	}

	public static Vendor findByVendorID(VendorID pVendorID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorID, false);
	}

	public static Vendor getByVendorID(VendorID pVendorID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorID, true);
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

	@Column(name = WEBSITE_COLUMN)
	public String getWebsite()
	{
		return this.fWebsite;
	}

	public void setWebsite(String pWebsite)
	{
		this.fWebsite = pWebsite;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.vendor.VendorID")
	public VendorID getRecordID()
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
	public HibernateDBConnector<Vendor, VendorList, VendorID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
