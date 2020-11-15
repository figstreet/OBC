package com.figstreet.core;

import java.lang.reflect.Constructor;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

public class IntDataIDTypeDescriptor<J extends IntDataID> extends AbstractTypeDescriptor<J>
{
	private static final long serialVersionUID = -6653833996315601222L;
	private static final String LOGGER_NAME = IntDataIDTypeDescriptor.class.getPackage().getName()
			+ ".IntDataIDTypeDescriptor";

	private Class<J> fClass;
	private transient Constructor<J> fCtor;

	@SuppressWarnings("unchecked")
	public IntDataIDTypeDescriptor(Class<J> pDataIDClass)
	{
		super(pDataIDClass, ImmutableMutabilityPlan.INSTANCE);
		this.fClass = pDataIDClass;
	}

	private Constructor<J> getConstructor()
	{
		if (this.fCtor == null)
			this.fCtor = CtorUtil.getCtorInteger(this.fClass);
		return this.fCtor;
	}

	@Override
	public J fromString(String pValue)
	{
		try
		{
			return this.getConstructor().newInstance(new Integer(pValue));
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "fromString",
					"Error executing " + this.fClass.getName() + " ctor with Integer parameter; returning null", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X/* Integer */> X unwrap(J pDataID, Class<X> pType, WrapperOptions pOptions)
	{
		if (pDataID == null)
			return null;

		if (Integer.class.isAssignableFrom(pType))
			return (X) pDataID.getValue();
		if (String.class.isAssignableFrom(pType))
			return (X) pDataID.getValue();

		throw this.unknownUnwrap(pType);
	}

	@Override
	public <X/* String */> J wrap(X pDataID, WrapperOptions pOptions)
	{
		if (pDataID == null)
			return null;

		Integer value = null;
		if (Integer.class.isInstance(pDataID))
			value = (Integer) pDataID;
		else if (String.class.isInstance(pDataID))
			value = new Integer((String) pDataID);

		if (value == null)
			throw this.unknownWrap(pDataID.getClass());

		try
		{
			return this.getConstructor().newInstance(value);
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "wrap",
					"Error executing " + this.fClass.getName() + " ctor with Integer parameter; returning null", e);
		}
		return null;
	}

}
