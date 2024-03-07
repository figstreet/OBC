package com.figstreet.core;

import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public abstract class ShortDataID extends DataID<Short>
{
	protected ShortDataID(JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		super(BigIntTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	protected ShortDataID(Short pValue, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		this(pValue, BigIntTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	protected ShortDataID(String pValue, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		this(new Short(pValue), BigIntTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	public ShortDataID(Short pValue, SqlTypeDescriptor pSqlTypeDescriptor, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		super(pValue, pSqlTypeDescriptor, pJavaTypeDescriptor);
	}

}