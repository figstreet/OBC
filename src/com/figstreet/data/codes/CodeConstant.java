package com.figstreet.data.codes;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import com.figstreet.core.DisplayValue;
import com.figstreet.core.Logging;
import com.figstreet.data.users.UsersID;

public abstract class CodeConstant extends AbstractSingleColumnStandardBasicType<CodeConstant>
		implements Serializable, Comparable<CodeConstant>, DisplayValue
{
	private static final long serialVersionUID = 693731630748689718L;

	public static abstract class CodeConstantFactory<T extends CodeConstant>
	{
		private final Class<T> fClass;
		private final CodesType fCodesType;
		private final boolean fValueIsCaseSensitive;
		private Map<String, T> fAllValues = new LinkedHashMap<>();

		protected CodeConstantFactory(Class<T> pClass, CodesType pCodesType, boolean pValueIsCaseSensitive)
		{
			this.fClass = pClass;
			this.fCodesType = pCodesType;
			this.fValueIsCaseSensitive = pValueIsCaseSensitive;
			CodeCache.getThe().addCodeConstantFactory(this);
		}

		protected CodeConstantFactory(Class<T> pClass, CodesType pCodesType)
		{
			this(pClass, pCodesType, false);
		}

		protected abstract T newInstance(String pValue, String pDisplay);

		public abstract String getLoggerName();

		public CodesType getCodesType()
		{
			return this.fCodesType;
		}

		@SuppressWarnings({ "unchecked" })
		protected void populateAllValues()
		{
			CodesList codeList = null;
			try
			{
				codeList = CodesList.loadByType(this.fCodesType);
			}
			catch (Exception e)
			{
				Logging.error(this.getLoggerName(), "populateAllValues",
						"Error loading codes, this will need to be refreshed", e);
			}

			synchronized (this.fAllValues)
			{
				HashMap<String, T> allValues = new HashMap<String, T>();
				Field[] fields = this.fClass.getDeclaredFields();
				for (Field field : fields)
				{
					if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())
							&& field.getType().equals(this.fClass))
					{
						try
						{
							T type = (T) field.get(null);
							String key = type.fValue;
							if (!this.fValueIsCaseSensitive)
								key = key.toUpperCase();
							allValues.put(key, type);
							type.fIsInCodes = false;
							type.fActive = false;
						}
						catch (Exception e)
						{
							Logging.error(this.getLoggerName(), "populateAllValues", "Error accessing static field "
									+ field.getName() + ", will not be present in cache", e);
						}
					}
				}

				if (codeList != null)
				{
					for (Codes code : codeList)
					{
						String value = code.getValue();
						if (!this.fValueIsCaseSensitive)
							value = value.toUpperCase();
						T type = allValues.get(value);
						if (type == null)
						{
							type = this.newInstance(value, code.getDescription());
							allValues.put(value, type);
						}

						type.setDisplay(code.getDescription());
						type.fIsInCodes = true;
						type.fActive = code.isActive();
					}
				}
				this.fAllValues = allValues;
			}
		}

		public T getValue(String pValue, boolean pCheckOnly)
		{
			return this.getValue(pValue, pCheckOnly, false);
		}

		public T getValue(String pValue, boolean pCheckOnly, boolean pAddToDatabase)
		{
			if (pValue == null)
				return null;
			String value = this.fValueIsCaseSensitive ? pValue.trim() : pValue.trim().toUpperCase();
			if (value.length() == 0)
				return null;

			T item = this.fAllValues.get(value);
			if (item != null)
				return item;

			if (pCheckOnly)
				return null;

			synchronized (this.fAllValues)
			{
				// try again in case hit during populateAllValues()
				item = this.fAllValues.get(value);
				if (item != null)
					return item;

				item = this.newInstance(value, value);
				if (pAddToDatabase)
				{
					try
					{
						item.setActive(true);
						CodesList databaseList = CodesList.loadByTypeAndValue(this.fCodesType, value);
						if (databaseList.isEmpty())
						{
							UsersID by = UsersID.ADMIN;
							Codes newCode = new Codes(this.fCodesType, value, value, by);
							newCode.saveOrUpdate(by);
						}
					}
					catch (Exception e)
					{
						Logging.error(this.getLoggerName(), "getValue", "Error storing " + value + " as new codes record in database", e);
					}
				}
				else
				{
					Logging.info(this.getLoggerName(), "getValue", "bad value(" + value + ")");
					item.setActive(false);
				}
				this.fAllValues.put(item.toString(), item);

				return item;
			}
		}

		public T getValue(String pValue)
		{
			return this.getValue(pValue, false);
		}

		public boolean contains(String pValue, boolean pIncludeNonCodes, boolean pActiveOnly)
		{
			T item = this.getValue(pValue, true);
			if (item == null)
				return false;
			return (item.isInCodes() || pIncludeNonCodes) && (item.isActive() || !pActiveOnly);
		}

		public boolean contains(String pValue)
		{
			return this.contains(pValue, false, true);
		}

		public boolean contains(T pValue, boolean pIncludeNonCodes, boolean pActiveOnly)
		{
			if (pValue == null)
				return false;
			return this.contains(pValue.toString(), pIncludeNonCodes, pActiveOnly);
		}

		public boolean contains(T pValue)
		{
			return this.contains(pValue, false, true);
		}

		private Collection<T> getAllValues(boolean pIncludeNonCodes)
		{
			ArrayList<T> allValues = new ArrayList<>();

			for (T t : this.fAllValues.values())
				if (t.isInCodes() || (pIncludeNonCodes && !t.isInCodes()))
					allValues.add(t);

			return allValues;
		}

		protected Collection<T> getAllValues()
		{
			return this.fAllValues.values();
		}

		public Collection<T> getNativeLinkValues(boolean pIncludeNonCodes)
		{
			return this.getAllValues(pIncludeNonCodes);
		}

		public Collection<T> getNativeLinkValues()
		{
			return this.getAllValues(false);
		}

		public Collection<T> getActiveValues(boolean pIncludeNonCodes)
		{
			ArrayList<T> allValues = new ArrayList<>();

			for (T t : this.fAllValues.values())
				if (t.isActive() && (t.isInCodes() || pIncludeNonCodes))
					allValues.add(t);

			return allValues;
		}

		public Collection<T> getActiveValues()
		{
			return this.getActiveValues(false);
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

		// Returns a map of CodeConstant.toString() - CodeConstant
		public HashMap<String, T> getMapOfCodeConstants(boolean pIncludeNonCodes, boolean pIncludeNull,
				boolean pActiveOnly)
		{
			HashMap<String, T> toReturn = new HashMap<String, T>();
			Collection<T> allValues;
			if (pActiveOnly)
				allValues = this.getActiveValues(pIncludeNonCodes);
			else
				allValues = this.getAllValues(pIncludeNonCodes);
			if (allValues != null)
			{
				Iterator<T> it = allValues.iterator();
				if (it != null)
				{
					while (it.hasNext())
					{
						T value = it.next();
						if (pIncludeNull || value != null)
							toReturn.put(CodeConstant.asString(value), value);
					}
				}
			}
			return toReturn;
		}

		public HashSet<String> getSetOfCodeValues(boolean pIncludeNonCodes, boolean pIncludeNull, boolean pActiveOnly)
		{
			HashSet<String> toReturn = new HashSet<String>();
			Collection<T> allValues;
			if (pActiveOnly)
				allValues = this.getActiveValues(pIncludeNonCodes);
			else
				allValues = this.getAllValues(pIncludeNonCodes);
			if (allValues != null)
			{
				Iterator<T> it = allValues.iterator();
				if (it != null)
				{
					while (it.hasNext())
					{
						T value = it.next();
						if (pIncludeNull)
							toReturn.add(CodeConstant.asString(value));
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
	protected boolean fIsInCodes;
	protected boolean fActive;

	@SuppressWarnings("rawtypes")
	protected CodeConstant(JavaTypeDescriptor pJavaTypeDescriptor)
	{
		this(null, null, false, pJavaTypeDescriptor);
	}

	@SuppressWarnings("rawtypes")
	protected CodeConstant(String pValue, String pDisplay, JavaTypeDescriptor pJavaTypeDescriptor)
	{
		this(pValue, pDisplay, true, pJavaTypeDescriptor);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected CodeConstant(String pValue, String pDisplay, boolean pActive, JavaTypeDescriptor pJavaTypeDescriptor)
	{
		super(VarcharTypeDescriptor.INSTANCE, pJavaTypeDescriptor);
		this.fValue = pValue;
		this.fDisplay = pDisplay;
		this.fActive = pActive;
	}

	public static String asString(CodeConstant pValue)
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

	public boolean isInCodes()
	{
		return this.fIsInCodes;
	}

	public boolean isActive()
	{
		return this.fActive;
	}

	public void setActive(boolean pActive)
	{
		this.fActive = pActive;
	}

	@Override
	public boolean equals(Object pObject)
	{
		if (pObject != null && (pObject instanceof CodeConstant))
			return ((CodeConstant) pObject).fValue.equals(this.fValue);
		return false;
	}

	@Override
	public int hashCode()
	{
		return this.fValue.hashCode();
	}

	@Override
	public int compareTo(CodeConstant pCodeConstant)
	{
		return this.fValue.compareTo(pCodeConstant.fValue);
	}

	@Override
	public String getName()
	{
		return this.getClass().getName();
	}
}
