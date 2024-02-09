package com.figstreet.core;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

public abstract class TypeConstantTypeDescriptor<J extends TypeConstant> extends AbstractTypeDescriptor<J>
{
	private static final long serialVersionUID = 349454615616520998L;

	@SuppressWarnings("unchecked")
	public TypeConstantTypeDescriptor(Class<J> pTypeConstantClass)
	{
		super(pTypeConstantClass, ImmutableMutabilityPlan.INSTANCE);
	}

	public abstract J getValue(String pValue);

	@Override
	public J fromString(String pValue)
	{
		return this.getValue(pValue);
	}

	@SuppressWarnings("unchecked")
	public <X> X unwrap(J pTypeConstant, Class<X> pType, WrapperOptions pOptions)
	{
		if (pTypeConstant == null)
			return null;

		if (String.class.isAssignableFrom(pType))
			return (X) pTypeConstant.toString();

		throw this.unknownUnwrap(pType);
	}

	@Override
	public <X> J wrap(X pTypeConstant, WrapperOptions pOptions)
	{
		if (pTypeConstant == null)
			return null;

		String value = null;
		if (String.class.isInstance(pTypeConstant))
			value = (String) pTypeConstant;

		if (value == null)
			throw this.unknownWrap(pTypeConstant.getClass());

		return this.getValue(value);
	}

}
