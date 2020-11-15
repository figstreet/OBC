package com.figstreet.data.userpermission;

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
@Table(name = "userpermission")
public class UserPermission extends HibernateDatabaseObject<UserPermissionID>
{
	private static final long serialVersionUID = 3037846589286697436L;

	public static final String ID_COLUMN = "upid";
	public static final String USERSID_COLUMN = "up_usid";
	public static final String ACTIVE_COLUMN = "up_active";
	public static final String TYPE_COLUMN = "up_type";
	public static final String ADDED_COLUMN = "up_added_dt";
	public static final String ADDED_BY_COLUMN = "up_added_by";
	public static final String LASTUPDATED_COLUMN = "up_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "up_lastupdated_by";


	private UsersID fUsersID;
	private UserPermissionType fType;
	private boolean fActive;

	@Transient
	public static final HibernateDBConnector<UserPermission, UserPermissionList, UserPermissionID> DB_CONNECTOR = new HibernateDBConnector<UserPermission, UserPermissionList, UserPermissionID>(
			UserPermission.class, "UserPermission", UserPermissionList.class, UserPermissionID.class, ID_COLUMN);

	protected UserPermission()
	{
		//empty ctor;
	}

	public UserPermission(UsersID pUsersID, UserPermissionType pType, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fUsersID = pUsersID;
		this.fType = pType;
		this.fAddedBy = pAddedBy;
	}

	public static UserPermission findByUserPermissionID(UserPermissionID pUserPermissionID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pUserPermissionID, false);
	}

	public static UserPermission getByUserPermissionID(UserPermissionID pUserPermissionID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pUserPermissionID, true);
	}

	@Column(name = USERSID_COLUMN)
	public UsersID getUsersID()
	{
		return this.fUsersID;
	}

	public void setUsersID(UsersID pUsersID)
	{
		this.fUsersID = pUsersID;
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
	@Type(type = "com.figstreet.data.userpermission.UserPermissionType")
	public UserPermissionType getType()
	{
		return this.fType;
	}

	public void setType(UserPermissionType pType)
	{
		this.fType = pType;
	}


	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.userpermission.UserPermissionID")
	public UserPermissionID getRecordID()
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
	public HibernateDBConnector<UserPermission, UserPermissionList, UserPermissionID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
