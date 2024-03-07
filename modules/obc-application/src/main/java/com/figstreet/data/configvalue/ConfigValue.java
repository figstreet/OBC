package com.figstreet.data.configvalue;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.figstreet.core.ClientID;
import org.hibernate.annotations.Type;

import com.figstreet.core.HibernateDBConnector;
import com.figstreet.core.HibernateDatabaseObject;
import com.figstreet.data.users.UsersID;

@Entity
@Table(name = "configvalue")
public class ConfigValue extends HibernateDatabaseObject<ConfigValueID>
{

	private static final long serialVersionUID = -5580281699868867640L;

	public static final String ID_COLUMN = "cvid";
	public static final String CLIENTID_COLUMN = "cv_clid";
	public static final String ACTIVE_COLUMN = "cv_active";
	public static final String CONFIG_TYPE_COLUMN = "cv_config_type";
	public static final String PROPERTY_NAME_COLUMN = "cv_property_name";
	public static final String PROPERTY_VALUE_COLUMN = "cv_property_value";
	public static final String ADDED_COLUMN = "cv_added_dt";
	public static final String ADDED_BY_COLUMN = "cv_added_by";
	public static final String LASTUPDATED_COLUMN = "cv_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "cv_lastupdated_by";

	private boolean fActive;
	private ClientID fClientID;
	private String fConfigValueType;
	private String fPropertyName;
	private String fPropertyValue;

	@Transient
	public static final HibernateDBConnector<ConfigValue, ConfigValueList, ConfigValueID> DB_CONNECTOR = new HibernateDBConnector<ConfigValue, ConfigValueList, ConfigValueID>(
			ConfigValue.class, "ConfigValue", ConfigValueList.class, ConfigValueID.class, ID_COLUMN);

	protected ConfigValue()
	{
		// empty ctor;
	}

	public ConfigValue(String pConfigValueType, String pPropertyName, String pPropertyValue, UsersID pAddedBy) {
		this(ClientID.GENERAL, pConfigValueType, pPropertyName, pPropertyValue, pAddedBy);
	}

	public ConfigValue(ClientID pClientID, String pConfigValueType, String pPropertyName, String pPropertyValue, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fClientID = pClientID;
		this.fConfigValueType = pConfigValueType;
		this.fPropertyName = pPropertyName;
		this.fPropertyValue = pPropertyValue;
		this.fAddedBy = pAddedBy;
	}

	public static ConfigValue findByConfigValueID(ConfigValueID pConfigValueID)
			throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pConfigValueID, false);
	}

	public static ConfigValue getByConfigValueID(ConfigValueID pConfigValueID)
			throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pConfigValueID, true);
	}

	public static ConfigValue findByNameForClient(ClientID pClientID, String pConfigType, String pPropertyName)
			throws SQLException
	{
		ConfigValueList list = ConfigValueList.loadByTypeAndNameForClient(pClientID, pConfigType, pPropertyName);
		if (list.size() == 0)
			return null;
		if (list.size() == 1)
			return list.get(0);

		throw new SQLException(
				"Found too many ConfigValues for type " + pConfigType + " and property " + pPropertyName);
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

	@Column(name = CLIENTID_COLUMN)
	public ClientID getClientID() {
		return this.fClientID;
	}

	@Column(name = CONFIG_TYPE_COLUMN)
	public String getConfigValueType()
	{
		return this.fConfigValueType;
	}

	public void setConfigValueType(String pConfigValueType)
	{
		this.fConfigValueType = pConfigValueType;
	}

	@Column(name = PROPERTY_NAME_COLUMN)
	public String getPropertyName()
	{
		return this.fPropertyName;
	}

	public void setPropertyName(String pPropertyName)
	{
		this.fPropertyName = pPropertyName;
	}

	@Column(name = PROPERTY_VALUE_COLUMN)
	public String getPropertyValue()
	{
		return this.fPropertyValue;
	}

	public void setPropertyValue(String pPropertyValue)
	{
		this.fPropertyValue = pPropertyValue;
	}

	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.data.configvalue.ConfigValueID")
	public ConfigValueID getRecordID()
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
	public HibernateDBConnector<ConfigValue, ConfigValueList, ConfigValueID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
