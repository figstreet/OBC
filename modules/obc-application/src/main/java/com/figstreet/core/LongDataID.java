package com.figstreet.core;

import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public abstract class LongDataID extends DataID<Long>
{
	private static final long serialVersionUID = 5576802228311361351L;

	protected LongDataID(JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		super(BigIntTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	protected LongDataID(Long pValue, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		this(pValue, BigIntTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	protected LongDataID(String pValue, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		this(new Long(pValue), BigIntTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	public LongDataID(Long pValue, SqlTypeDescriptor pSqlTypeDescriptor, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		super(pValue, pSqlTypeDescriptor, pJavaTypeDescriptor);
	}

}
