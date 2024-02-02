package com.figstreet.data.vendoraddress;

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
import com.figstreet.data.common.Country;
import com.figstreet.data.common.Region;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;

@Entity
@Table(name = "vendoraddress")
public class VendorAddress extends HibernateDatabaseObject<VendorAddressID>
{
	private static final long serialVersionUID = -3978585852303965475L;

	public static final String ID_COLUMN = "vdaid";
	public static final String ACTIVE_COLUMN = "vda_active";
	public static final String VENDORID_COLUMN = "vda_vdid";
	public static final String PRIMARY_COLUMN = "vda_primary";
	public static final String TYPE_COLUMN = "vda_type";
	public static final String ADDRESS1_COLUMN = "vda_addr1";
	public static final String ADDRESS2_COLUMN = "vda_addr2";
	public static final String CITY_COLUMN = "vda_city";
	public static final String REGION_COLUMN = "vda_region";
	public static final String ZIP_COLUMN = "vda_zip";
	public static final String COUNTRY_COLUMN = "vda_country";
	public static final String ADDED_COLUMN = "vda_added_dt";
	public static final String ADDED_BY_COLUMN = "vda_added_by";
	public static final String LASTUPDATED_COLUMN = "vda_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "vda_lastupdated_by";

	private VendorID fVendorID;
	private boolean fActive;
	private boolean fPrimary;
	private VendorAddressType fType;
	private String fAddress1;
	private String fAddress2;
	private String fCity;
	private Region fRegion;
	private String fZip;
	private Country fCountry;

	@Transient
	public static final HibernateDBConnector<VendorAddress, VendorAddressList, VendorAddressID> DB_CONNECTOR = new HibernateDBConnector<VendorAddress, VendorAddressList, VendorAddressID>(
			VendorAddress.class, "VendorAddress", VendorAddressList.class, VendorAddressID.class, ID_COLUMN);

	protected VendorAddress()
	{
		// empty ctor;
	}

	public VendorAddress(VendorID pVendorID, VendorAddressType pType, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fType = pType;
		this.fVendorID = pVendorID;
		this.fAddedBy = pAddedBy;
	}

	public static VendorAddress findByVendorAddressID(VendorAddressID pVendorAddressID)
			throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorAddressID, false);
	}

	public static VendorAddress getByVendorAddressID(VendorAddressID pVendorAddressID)
			throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorAddressID, true);
	}

	@Column(name = VENDORID_COLUMN)
	@Type(type = "com.figstreet.data.vendor.VendorID")
	public VendorID getVendorID()
	{
		return this.fVendorID;
	}

	protected void setVendorID(VendorID pVendorID)
	{
		this.fVendorID = pVendorID;
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

	@Column(name = PRIMARY_COLUMN)
	public boolean getPrimary()
	{
		return this.fPrimary;
	}

	public void setPrimary(boolean pPrimary)
	{
		this.fPrimary = pPrimary;
	}

	@Column(name = TYPE_COLUMN)
	@Type(type = "com.figstreet.data.vendoraddress.VendorAddressType")
	public VendorAddressType getType()
	{
		return this.fType;
	}

	public void setType(VendorAddressType pType)
	{
		this.fType = pType;
	}

	@Column(name = ADDRESS1_COLUMN)
	public String getAddress1()
	{
		return this.fAddress1;
	}

	public void setAddress1(String pAddress1)
	{
		this.fAddress1 = pAddress1;
	}

	@Column(name = ADDRESS2_COLUMN)
	public String getAddress2()
	{
		return this.fAddress2;
	}

	public void setAddress2(String pAddress2)
	{
		this.fAddress2 = pAddress2;
	}

	@Column(name = CITY_COLUMN)
	public String getCity()
	{
		return this.fCity;
	}

	public void setCity(String pCity)
	{
		this.fCity = pCity;
	}

	@Column(name = REGION_COLUMN)
	@Type(type = "com.figstreet.data.common.Region")
	public Region getRegion()
	{
		return this.fRegion;
	}

	public void setRegion(Region pRegion)
	{
		this.fRegion = pRegion;
	}

	@Column(name = ZIP_COLUMN)
	public String getZip()
	{
		return this.fZip;
	}

	public void setZip(String pZip)
	{
		this.fZip = pZip;
	}

	@Column(name = COUNTRY_COLUMN)
	@Type(type = "com.figstreet.data.common.Country")
	public Country getCountry()
	{
		return this.fCountry;
	}

	public void setCountry(Country pCountry)
	{
		this.fCountry = pCountry;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.vendoraddress.VendorAddressID")
	public VendorAddressID getRecordID()
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
	public HibernateDBConnector<VendorAddress, VendorAddressList, VendorAddressID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
