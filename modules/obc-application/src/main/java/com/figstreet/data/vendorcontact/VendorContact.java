package com.figstreet.data.vendorcontact;

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
import com.figstreet.data.vendor.VendorID;

@Entity
@Table(name = "vendorcontact")
public class VendorContact extends HibernateDatabaseObject<VendorContactID>
{
	private static final long serialVersionUID = 2014051849796293643L;

	public static final String ID_COLUMN = "vdcid";
	public static final String ACTIVE_COLUMN = "vdc_active";
	public static final String VENDORID_COLUMN = "vdc_vdid";
	public static final String PRIMARY_COLUMN = "vdc_primary";
	public static final String TYPE_COLUMN = "vdc_type";
	public static final String EMAIL_COLUMN = "vdc_email";
	public static final String PHONE_COUNTRY_CODE_COLUMN = "vdc_country_code";
	public static final String PHONE1_COLUMN = "vdc_phone1";
	public static final String PHONE2_COLUMN = "vdc_phone2";
	public static final String PHONE3_COLUMN = "vdc_phone3";
	public static final String PHONE_EXT_COLUMN = "vdc_phone_ext";
	public static final String NAME1_COLUMN = "vdc_contact_name1";
	public static final String NAME2_COLUMN = "vdc_contact_name2";
	public static final String ADDED_COLUMN = "vdc_added_dt";
	public static final String ADDED_BY_COLUMN = "vdc_added_by";
	public static final String LASTUPDATED_COLUMN = "vdc_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "vdc_lastupdated_by";

	private VendorID fVendorID;
	private boolean fActive;
	private boolean fPrimary;
	private VendorContactType fType;
	private String fEmail;
	private String fPhoneCountryCode;
	private String fPhone1;
	private String fPhone2;
	private String fPhone3;
	private String fPhoneExt;
	private String fName1;
	private String fName2;

	@Transient
	public static final HibernateDBConnector<VendorContact, VendorContactList, VendorContactID> DB_CONNECTOR = new HibernateDBConnector<VendorContact, VendorContactList, VendorContactID>(
			VendorContact.class, "VendorContact", VendorContactList.class, VendorContactID.class, ID_COLUMN);

	protected VendorContact()
	{
		// empty ctor;
	}

	public VendorContact(VendorID pVendorID, VendorContactType pType, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fVendorID = pVendorID;
		this.fType = pType;
		this.fAddedBy = pAddedBy;
	}

	public static VendorContact findByVendorContactID(VendorContactID pVendorContactID)
			throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorContactID, false);
	}

	public static VendorContact getByVendorContactID(VendorContactID pVendorContactID)
			throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pVendorContactID, true);
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
	@Type(type = "com.figstreet.data.vendor.VendorID")
	public VendorID getVendorID()
	{
		return this.fVendorID;
	}

	public void setVendorID(VendorID pVendorID)
	{
		this.fVendorID = pVendorID;
	}

	@Column(name = PRIMARY_COLUMN)
	public boolean isPrimary()
	{
		return this.fPrimary;
	}

	public void setPrimary(boolean pPrimary)
	{
		this.fPrimary = pPrimary;
	}

	@Column(name = TYPE_COLUMN)
	@Type(type = "com.figstreet.data.vendorcontact.VendorContactType")
	public VendorContactType getType()
	{
		return this.fType;
	}

	public void setType(VendorContactType pType)
	{
		this.fType = pType;
	}

	@Column(name = EMAIL_COLUMN)
	public String getEmail()
	{
		return this.fEmail;
	}

	public void setEmail(String pEmail)
	{
		this.fEmail = pEmail;
	}

	@Column(name = PHONE_COUNTRY_CODE_COLUMN)
	public String getPhoneCountryCode()
	{
		return this.fPhoneCountryCode;
	}

	public void setPhoneCountryCode(String pPhoneCountryCode)
	{
		this.fPhoneCountryCode = pPhoneCountryCode;
	}

	@Column(name = PHONE1_COLUMN)
	public String getPhone1()
	{
		return this.fPhone1;
	}

	public void setPhone1(String pPhone1)
	{
		this.fPhone1 = pPhone1;
	}

	@Column(name = PHONE2_COLUMN)
	public String getPhone2()
	{
		return this.fPhone2;
	}

	public void setPhone2(String pPhone2)
	{
		this.fPhone2 = pPhone2;
	}

	@Column(name = PHONE3_COLUMN)
	public String getPhone3()
	{
		return this.fPhone3;
	}

	public void setPhone3(String pPhone3)
	{
		this.fPhone3 = pPhone3;
	}

	@Column(name = PHONE_EXT_COLUMN)
	public String getPhoneExt()
	{
		return this.fPhoneExt;
	}

	public void setPhoneExt(String pPhoneExt)
	{
		this.fPhoneExt = pPhoneExt;
	}

	@Column(name = NAME1_COLUMN)
	public String getName1()
	{
		return this.fName1;
	}

	public void setName1(String pName1)
	{
		this.fName1 = pName1;
	}

	@Column(name = NAME2_COLUMN)
	public String getName2()
	{
		return this.fName2;
	}

	public void setName2(String pName2)
	{
		this.fName2 = pName2;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.vendorcontact.VendorContactID")
	public VendorContactID getRecordID()
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
	public HibernateDBConnector<VendorContact, VendorContactList, VendorContactID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
