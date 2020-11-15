package com.figstreet.core;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.figstreet.data.users.UsersID;

@MappedSuperclass
public abstract class HibernateDatabaseObject<J extends DataID<?>> implements Serializable, Cloneable
{
	private static final long serialVersionUID = -3665749324531839951L;

	protected J fRecordID;
	protected Timestamp fAdded;
	protected UsersID fAddedBy;
	protected Timestamp fLastUpdated;
	protected UsersID fLastUpdatedBy;

	@Transient
	public abstract HibernateDBConnector getDBConnector();

	@Transient
	public J getRecordID()
	{
		return this.fRecordID;
	}

	public void setRecordID(J pRecordID)
	{
		this.fRecordID = pRecordID;
	}

	@Transient
	public boolean isNewRecord()
	{
		return this.fRecordID == null;
	}

	@Transient
	public Timestamp getAdded()
	{
		return this.fAdded;
	}

	public void setAdded(Timestamp pAdded)
	{
		this.fAdded = pAdded;
	}

	@Transient
	public UsersID getAddedBy()
	{
		return this.fAddedBy;
	}

	public void setAddedBy(UsersID pAddedBy)
	{
		this.fAddedBy = pAddedBy;
	}

	@Transient
	public Timestamp getLastUpdated()
	{
		return this.fLastUpdated;
	}

	public void setLastUpdated(Timestamp pLastUpdated)
	{
		this.fLastUpdated = pLastUpdated;
	}

	@Transient
	public UsersID getLastUpdatedBy()
	{
		return this.fLastUpdatedBy;
	}

	public void setLastUpdatedBy(UsersID pLastUpdatedBy)
	{
		this.fLastUpdatedBy = pLastUpdatedBy;
	}

	public void saveOrUpdate(UsersID pBy) throws SQLException
	{
		if (this.fAdded == null)
		{
			this.fAddedBy = pBy;
			this.fAdded = DateUtil.now();
		}
		this.fLastUpdatedBy = pBy;
		this.fLastUpdated = DateUtil.now();

		this.getDBConnector().saveOrUpdate(this);
	}

	public void delete() throws SQLException
	{
		this.getDBConnector().delete(this);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		if (this.getRecordID() != null)
		{
			sb.append(":");
			sb.append(this.getRecordID());
		}
		return sb.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return ObjectUtil.deepCopy(this);
	}
}
