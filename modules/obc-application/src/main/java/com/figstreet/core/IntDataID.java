package com.figstreet.core;

import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.IntegerTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public abstract class IntDataID extends DataID<Integer>
{
	private static final long serialVersionUID = -268720336190760032L;

	protected IntDataID(JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		super(IntegerTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	protected IntDataID(Integer pValue, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		this(pValue, IntegerTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	protected IntDataID(String pValue, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		this(new Integer(pValue), IntegerTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
	}

	public IntDataID(Integer pValue, SqlTypeDescriptor pSqlTypeDescriptor, JavaTypeDescriptor<?> pJavaTypeDescriptor)
	{
		super(pValue, pSqlTypeDescriptor, pJavaTypeDescriptor);
	}

}
