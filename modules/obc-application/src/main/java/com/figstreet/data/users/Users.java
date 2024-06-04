package com.figstreet.data.users;

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

@Entity
@Table(name = "users")
public class Users extends HibernateDatabaseObject<UsersID>
{
	private static final long serialVersionUID = -6035950674114383088L;
	public static final String ID_COLUMN = "usid";
	public static final String EMAIL_COLUMN = "us_email";
	public static final String DISPLAYID_COLUMN = "us_display_id";
	public static final String ACTIVE_COLUMN = "us_active";
	public static final String FIRSTNAME_COLUMN = "us_fname";
	public static final String MIDDLENAME_COLUMN = "us_mname";
	public static final String LASTNAME_COLUMN = "us_lname";
	public static final String PASSWORD_COLUMN = "us_password";
	public static final String PASSWORD_SALT_COLUMN = "us_password_salt";
	public static final String PASSWORD_SET_COLUMN = "us_password_set_dt";
	public static final String CONFIRMATION_KEY_COLUMN = "us_confirmation_key";
	public static final String CONFIRMED_COLUMN = "us_confirmed";
	public static final String LAST_LOGIN_COLUMN = "us_login_dt";
	public static final String LOGIN_FAILURE_COUNT_COLUMN = "us_login_failures";
	public static final String INACTIVE_REASON_COLUMN = "us_inactive_reason";
	public static final String ADDED_COLUMN = "us_added_dt";
	public static final String ADDED_BY_COLUMN = "us_added_by";
	public static final String LASTUPDATED_COLUMN = "us_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "us_lastupdated_by";

	private String fEmail;
	private String fDisplayID;
	private boolean fActive;
	private String fFirstName;
	private String fMiddleName;
	private String fLastName;
	private char[] fPassword;
	private String fPasswordSalt;
	private Timestamp fPasswordSet;
	private String fConfirmationKey;
	private Timestamp fConfirmed;
	private Timestamp fLastLogin;
	private Integer fLoginFailureCount;
	private String fInactiveReason;

	@Transient
	public static final HibernateDBConnector<Users, UsersList, UsersID> DB_CONNECTOR = new HibernateDBConnector<Users, UsersList, UsersID>(
			Users.class, "Users", UsersList.class, UsersID.class, ID_COLUMN);

	protected Users()
	{
		// empty ctor;
	}

	public Users(String pEmail, String pDisplayID, String pFirstName, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fEmail = pEmail;
		this.fDisplayID = pDisplayID;
		this.fFirstName = pFirstName;
		this.fAddedBy = pAddedBy;
	}

	public static Users findByUsersID(UsersID pUsersID) throws SQLException, RecordNotExistException
	{
		return DB_CONNECTOR.loadRecord(pUsersID, false);
	}

	public static Users getByUsersID(UsersID pUsersID) throws SQLException, RecordNotExistException
	{
		return DB_CONNECTOR.loadRecord(pUsersID, true);
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

	@Column(name = EMAIL_COLUMN)
	public String getEmail()
	{
		return this.fEmail;
	}

	public void setEmail(String pEmail)
	{
		this.fEmail = pEmail;
	}

	@Column(name = DISPLAYID_COLUMN)
	public String getDisplayID()
	{
		return this.fDisplayID;
	}

	public void setDisplayID(String pDisplayID)
	{
		this.fDisplayID = pDisplayID;
	}

	@Column(name = FIRSTNAME_COLUMN)
	public String getFirstName()
	{
		return this.fFirstName;
	}

	public void setFirstName(String pFirstName)
	{
		this.fFirstName = pFirstName;
	}

	@Column(name = MIDDLENAME_COLUMN)
	public String getMiddleName()
	{
		return this.fMiddleName;
	}

	public void setMiddleName(String pMiddleName)
	{
		this.fFirstName = pMiddleName;
	}

	@Column(name = LASTNAME_COLUMN)
	public String getLastName()
	{
		return this.fLastName;
	}

	public void setLastName(String pLastName)
	{
		this.fLastName = pLastName;
	}

	@Column(name = PASSWORD_COLUMN)
	public char[] getPassword()
	{
		return this.fPassword;
	}

	public void setPassword(char[] pPassword)
	{
		this.fPassword = pPassword;
	}

	@Column(name = PASSWORD_SALT_COLUMN)
	public String getPasswordSalt()
	{
		return this.fPasswordSalt;
	}

	public void setPasswordSalt(String pPasswordSalt)
	{
		this.fPasswordSalt = pPasswordSalt;
	}

	@Column(name = PASSWORD_SET_COLUMN)
	public Timestamp getPasswordSet()
	{
		return this.fPasswordSet;
	}

	public void setPasswordSet(Timestamp pPasswordSet)
	{
		this.fPasswordSet = pPasswordSet;
	}

	@Column(name = CONFIRMATION_KEY_COLUMN)
	public String getConfirmationKey()
	{
		return this.fConfirmationKey;
	}

	public void setConfirmationKey(String pConfirmationKey)
	{
		this.fConfirmationKey = pConfirmationKey;
	}

	@Column(name = CONFIRMED_COLUMN)
	public Timestamp getConfirmed()
	{
		return this.fConfirmed;
	}

	public void setConfirmed(Timestamp pConfirmed)
	{
		this.fConfirmed = pConfirmed;
	}

	@Column(name = LAST_LOGIN_COLUMN)
	public Timestamp getLastLogin()
	{
		return this.fLastLogin;
	}

	public void setLastLogin(Timestamp pLastLogin)
	{
		this.fLastLogin = pLastLogin;
	}

	@Column(name = LOGIN_FAILURE_COUNT_COLUMN)
	public Integer getLoginFailureCount()
	{
		return this.fLoginFailureCount;
	}

	public void setLoginFailureCount(Integer pLoginFailureCount)
	{
		this.fLoginFailureCount = pLoginFailureCount;
	}

	@Column(name = INACTIVE_REASON_COLUMN)
	public String getInactiveReason()
	{
		return this.fInactiveReason;
	}

	public void setInactiveReason(String pInactiveReason)
	{
		this.fInactiveReason = pInactiveReason;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.users.UsersID")
	public UsersID getRecordID()
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
	public HibernateDBConnector<Users, UsersList, UsersID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
