package com.figstreet.core;

import java.io.Serializable;

import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public abstract class DataID<I extends Comparable<I>> extends AbstractSingleColumnStandardBasicType<DataID<I>>
		implements Comparable<DataID<I>>, ResultSetIdentifierConsumer, Serializable
{
	private static final long serialVersionUID = 967535560719706337L;
	protected I fValue;

	@SuppressWarnings("rawtypes")
	protected DataID(SqlTypeDescriptor pSqlTypeDescriptor, JavaTypeDescriptor pJavaTypeDescriptor)
	{
		this(null, pSqlTypeDescriptor, pJavaTypeDescriptor);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected DataID(I pValue, SqlTypeDescriptor pSqlTypeDescriptor, JavaTypeDescriptor pJavaTypeDescriptor)
	{
		super(pSqlTypeDescriptor, pJavaTypeDescriptor);
		this.fValue = pValue;
	}

	public I getValue()
	{
		return this.fValue;
	}

	@Override
	public String toString()
	{
		if (this.fValue == null)
			return null;
		return this.fValue.toString();
	}

	public static <I extends Comparable<I>> String asString(DataID<I> pDataID)
	{
		if (pDataID != null)
			return pDataID.toString();
		return null;
	}

	@Override
	public int hashCode()
	{
		return this.getValue().hashCode();
	}

	@Override
	public boolean equals(Object pObj)
	{
		if (pObj == null)
			return false;

		if (pObj instanceof DataID)
		{
			DataID<?> other = (DataID<?>) pObj;
			if (this.getValue() == null)
			{
				if (other.getValue() == null)
					return true;
			}
			return this.getValue().equals(other.getValue());
		}
		return false;
	}

	@Override
	public int compareTo(DataID<I> pObj)
	{
		if (this.getValue() == null && pObj.getValue() == null)
			return 0;
		else if (pObj.getValue() == null)
			return Integer.MAX_VALUE;
		else if (this.getValue() == null)
			return Integer.MIN_VALUE;

		return this.getValue().compareTo(pObj.getValue());
	}

	@Override
	public String getName()
	{
		return this.getClass().getName();
	}

}
