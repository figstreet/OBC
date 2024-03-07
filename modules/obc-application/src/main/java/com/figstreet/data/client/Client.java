package com.figstreet.data.client;

import com.figstreet.core.ClientID;
import com.figstreet.core.HibernateDBConnector;
import com.figstreet.core.HibernateDatabaseObject;
import com.figstreet.data.users.UsersID;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.SQLException;
import java.sql.Timestamp;

@Entity
@Table(name = "client")
public class Client extends HibernateDatabaseObject<ClientID>
{

	public static final String ID_COLUMN = "clid";
	public static final String ACTIVE_COLUMN = "cl_active";
	public static final String NAME_COLUMN = "cl_name";
	public static final String ADDED_COLUMN = "cl_added_dt";
	public static final String ADDED_BY_COLUMN = "cl_added_by";
	public static final String LASTUPDATED_COLUMN = "cl_lastupdated_dt";
	public static final String LASTUPDATED_BY_COLUMN = "cl_lastupdated_by";

	private boolean fActive;
	private String fName;

	@Transient
	public static final HibernateDBConnector<Client, ClientList, ClientID> DB_CONNECTOR = new HibernateDBConnector<Client, ClientList, ClientID>(
			Client.class, "Client", ClientList.class, ClientID.class, ID_COLUMN);

	protected Client()
	{
		// empty ctor;
	}

	public Client(String pName, UsersID pAddedBy)
	{
		this.fActive = true;
		this.fName = pName;
		this.fAddedBy = pAddedBy;
	}

	public static Client findByClientID(ClientID pClientID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pClientID, false);
	}

	public static Client getByClientID(ClientID pClientID) throws SQLException
	{
		return DB_CONNECTOR.loadRecord(pClientID, true);
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


	@Override
	@Id
	@Column(name = ID_COLUMN, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "com.figstreet.core.ClientID")
	public ClientID getRecordID()
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
	public HibernateDBConnector<Client, ClientList, ClientID> getDBConnector()
	{
		return DB_CONNECTOR;
	}
}
