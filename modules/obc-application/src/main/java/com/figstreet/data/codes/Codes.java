package com.figstreet.data.codes;

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
import com.figstreet.data.users.UsersID;

@Entity
@Table(name = "codes")
public class Codes extends HibernateDatabaseObject<CodesID>
{
	private static final long serialVersionUID = -3585308197793059854L;

	public static final String ID_COLUMN = "coid";
	public static final String ACTIVE_COLUMN = "co_active";
	public static final String TYPE_COLUMN = "co_type";
	public static final String VALUE_COLUMN = "co_value";
	public static final String DESCRIPTION_COLUMN = "co_desc";
	public static final String ADDED_COLUMN = "co_added_dt";
	public static final String ADDED_BY_COLUMN = "co_added_by";
	public static final String LASTUPDATED_COLUMN = "co_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "co_lastupdated_by";

	private boolean fActive;
	private CodesType fCodesType;
	private String fValue;
	private String fDescription;

	@Transient
	public static final HibernateDBConnector<Codes, CodesList, CodesID> DB_CONNECTOR = new HibernateDBConnector<Codes, CodesList, CodesID>(
			Codes.class, "Codes", CodesList.class, CodesID.class, ID_COLUMN);

	protected Codes()
	{
		//empty ctor;
	}

	public Codes(CodesType pCodesType, String pValue, String pDescription, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fCodesType = pCodesType;
		this.fValue = pValue;
		this.fDescription = pDescription;
		this.fAddedBy = pAddedBy;
	}

	public static Codes findByCodesID(CodesID pCodesID) throws SQLException, RecordNotExistException
	{
		return DB_CONNECTOR.loadRecord(pCodesID, false);
	}

	public static Codes getByCodesID(CodesID pCodesID) throws SQLException, RecordNotExistException
	{
		return DB_CONNECTOR.loadRecord(pCodesID, true);
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

	@Column(name = TYPE_COLUMN)
	@Type(type = "com.figstreet.data.codes.CodesType")
	public CodesType getCodesType()
	{
		return this.fCodesType;
	}

	public void setCodesType(CodesType pCodesType)
	{
		this.fCodesType = pCodesType;
	}

	@Column(name = VALUE_COLUMN)
	public String getValue()
	{
		return this.fValue;
	}

	public void setValue(String pCodeValue)
	{
		this.fValue = pCodeValue;
	}

	@Column(name = DESCRIPTION_COLUMN)
	public String getDescription()
	{
		return this.fDescription;
	}

	public void setDescription(String pCodeDescription)
	{
		this.fDescription = pCodeDescription;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.codes.CodesID")
	public CodesID getRecordID()
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
	public HibernateDBConnector<Codes, CodesList, CodesID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
