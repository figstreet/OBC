package com.figstreet.data.codes;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

public abstract class CodeConstantTypeDescriptor<J extends CodeConstant> extends AbstractTypeDescriptor<J>
{
	private static final long serialVersionUID = 349454605977220998L;

	@SuppressWarnings("unchecked")
	public CodeConstantTypeDescriptor(Class<J> pCodeConstantClass)
	{
		super(pCodeConstantClass, ImmutableMutabilityPlan.INSTANCE);
	}

	public abstract J getValue(String pValue);

	@SuppressWarnings("unchecked")
	public <X> X unwrap(J pCodeConstant, Class<X> pType, WrapperOptions pOptions)
	{
		if (pCodeConstant == null)
			return null;

		if (String.class.isAssignableFrom(pType))
			return (X) pCodeConstant.toString();

		throw this.unknownUnwrap(pType);
	}

	@Override
	public <X> J wrap(X pCodeConstant, WrapperOptions pOptions)
	{
		if (pCodeConstant == null)
			return null;

		String value = null;
		if (String.class.isInstance(pCodeConstant))
			value = (String) pCodeConstant;

		if (value == null)
			throw this.unknownWrap(pCodeConstant.getClass());

		return this.getValue(value);
	}

	@Override
	public J fromString(String pValue)
	{
		return this.getValue(pValue);
	}
}
