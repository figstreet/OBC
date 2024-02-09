package com.figstreet.core;

import java.lang.reflect.Constructor;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

public class LongDataIDTypeDescriptor<J extends LongDataID> extends AbstractTypeDescriptor<J>
{
	private static final long serialVersionUID = 1799921935165739918L;
	private static final String LOGGER_NAME = LongDataIDTypeDescriptor.class.getPackage().getName()
			+ ".LongDataIDTypeDescriptor";

	private Class<J> fClass;
	private transient Constructor<J> fCtor;

	@SuppressWarnings("unchecked")
	public LongDataIDTypeDescriptor(Class<J> pDataIDClass)
	{
		super(pDataIDClass, ImmutableMutabilityPlan.INSTANCE);
		this.fClass = pDataIDClass;
	}

	private Constructor<J> getConstructor()
	{
		if (this.fCtor == null)
			this.fCtor = CtorUtil.getCtorLong(this.fClass);
		return this.fCtor;
	}

	@Override
	public J fromString(String pValue)
	{
		try
		{
			return this.getConstructor().newInstance(new Long(pValue));
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "fromString",
					"Error executing " + this.fClass.getName() + " ctor with Long parameter; returning null", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <X/* Integer */> X unwrap(J pDataID, Class<X> pType, WrapperOptions pOptions)
	{
		if (pDataID == null)
			return null;

		if (Long.class.isAssignableFrom(pType))
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

		Long value = null;
		if (Long.class.isInstance(pDataID))
			value = (Long) pDataID;
		else if (String.class.isInstance(pDataID))
			value = new Long((String) pDataID);

		if (value == null)
			throw this.unknownWrap(pDataID.getClass());

		try
		{
			return this.getConstructor().newInstance(value);
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "wrap",
					"Error executing " + this.fClass.getName() + " ctor with Long parameter; returning null", e);
		}
		return null;
	}

}
