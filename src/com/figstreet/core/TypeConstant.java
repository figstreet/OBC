package com.figstreet.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public abstract class TypeConstant extends AbstractSingleColumnStandardBasicType<TypeConstant>
		implements Serializable, Comparable<TypeConstant>, DisplayValue
{
	private static final long serialVersionUID = 4639643423379246156L;

	public static abstract class TypeConstantFactory<T extends TypeConstant>
	{
		private final Class<T> fClass;
		private Map<String, T> fAllValues;

		protected TypeConstantFactory(Class<T> pClass)
		{
			this.fClass = pClass;
			this.populateAllValues();
		}

		protected abstract T newInstance(String pValue, String pDisplay);

		protected abstract String getLoggerName();

		@SuppressWarnings("unchecked")
		private void populateAllValues()
		{
			try
			{
				synchronized (this)
				{
					Field[] fields = this.fClass.getDeclaredFields();
					Map<String, T> allValues = new LinkedHashMap<>(fields.length);
					for (Field field : fields)
					{
						if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())
								&& field.getType().equals(this.fClass))
						{
							T type = (T) field.get(null);
							allValues.put(type.fValue, type);
						}
					}

					this.fAllValues = allValues;
				}
			}
			catch (Exception e)
			{
				Logging.error(this.getLoggerName(), "populateAllValues", "Error loading values", e);
			}
		}

		public void refresh()
		{
			this.populateAllValues();
		}

		public T getValue(String pValue, boolean pCheckOnly)
		{
			if (pValue == null)
				return null;
			String value = pValue.trim();
			if (value.length() == 0)
				return null;

			T item = this.fAllValues.get(value);
			if (item != null)
				return item;

			if (pCheckOnly)
				return null;

			synchronized (this)
			{
				// try again in case hit during populateAllValues()
				item = this.fAllValues.get(value);
				if (item != null)
					return item;

				Logging.info(this.getLoggerName(), "getValue", "bad value(" + value + "), adding to internal map");
				item = this.newInstance(value, value);
				this.fAllValues.put(item.toString(), item);

				return item;
			}
		}

		public T getValue(String pValue)
		{
			return this.getValue(pValue, false);
		}

		public boolean contains(String pValue)
		{
			T item = this.getValue(pValue, true);
			return item != null;
		}

		public boolean contains(T pValue)
		{
			if (pValue == null)
				return false;
			return this.contains(pValue.toString());
		}

		public List<T> getAllValues()
		{
			ArrayList<T> allValues = new ArrayList<T>();

			for (T t : this.fAllValues.values())
				allValues.add(t);
			return allValues;
		}

		public String getAllValuesAsStringList(boolean pQuoted)
		{
			StringBuilder sb = new StringBuilder();

			for (T t : this.getAllValues())
			{
				if (sb.length() > 0)
					sb.append(",");
				if (pQuoted)
					sb.append(String.format("'%s'", t.toString()));
				else
					sb.append(String.format("%s", t.toString()));
			}

			return sb.toString();
		}

		// Returns a map of TypeConstant.toString() - TypeConstant
		public HashMap<String, T> getMapOfTypeConstants(boolean pIncludeNull)
		{
			HashMap<String, T> toReturn = new HashMap<String, T>();
			Collection<T> allValues = this.getAllValues();
			if (allValues != null)
			{
				Iterator<T> it = allValues.iterator();
				if (it != null)
				{
					while (it.hasNext())
					{
						T value = it.next();
						if (pIncludeNull || value != null)
							toReturn.put(TypeConstant.asString(value), value);
					}
				}
			}
			return toReturn;
		}

		public HashSet<String> getSetOfTypeValues(boolean pIncludeNull)
		{
			HashSet<String> toReturn = new HashSet<String>();
			Collection<T> allValues = this.getAllValues();
			if (allValues != null)
			{
				Iterator<T> it = allValues.iterator();
				if (it != null)
				{
					while (it.hasNext())
					{
						T value = it.next();
						if (pIncludeNull)
							toReturn.add(TypeConstant.asString(value));
						else if (value != null && value.toString() != null)
							toReturn.add(value.toString());

					}
				}
			}
			return toReturn;
		}

	}

	protected String fValue;
	protected String fDisplay;

	@SuppressWarnings("rawtypes")
	protected TypeConstant(JavaTypeDescriptor pJavaTypeDescriptor)
	{
		this(null, null, pJavaTypeDescriptor);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected TypeConstant(String pValue, String pDisplay, JavaTypeDescriptor pJavaTypeDescriptor)
	{
		super(VarcharTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
		this.fValue = pValue;
		this.fDisplay = pDisplay;
	}

	public static String asString(TypeConstant pValue)
	{
		if (pValue == null)
			return null;
		return pValue.toString();
	}

	@Override
	public String toString()
	{
		return this.fValue;
	}

	public String getValue()
	{
		return this.fValue;
	}

	protected void setValue(String pValue)
	{
		this.fValue = pValue;
	}

	@Override
	public String getDisplay()
	{
		return this.fDisplay;
	}

	public String getDisplay(boolean pAppendValue)
	{
		if (pAppendValue)
			return String.format("%s (%s)", this.fDisplay, this.fValue);
		return this.fDisplay;
	}

	public void setDisplay(String pDisplay)
	{
		this.fDisplay = pDisplay;
	}

	@Override
	public boolean equals(Object pObj)
	{
		if ((pObj != null) && (pObj instanceof TypeConstant))
			return ((TypeConstant) pObj).fValue.equals(this.fValue);
		return false;
	}

	@Override
	public int hashCode()
	{
		return this.fValue.hashCode();
	}

	@Override
	public int compareTo(TypeConstant pO)
	{
		return this.fValue.compareTo(pO.fValue);
	}

}
