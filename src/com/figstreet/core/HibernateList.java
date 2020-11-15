package com.figstreet.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public abstract class HibernateList<C extends HibernateDatabaseObject<?>> extends ArrayList<C> implements Serializable
{
	public static final String ORDER_BY = " ORDER BY ";

	public C getDatabaseObject(int pIndex)
	{
		return (C) super.get(pIndex);
	}

	public Map<DataID<?>, C> getListAsMap()
	{
		LinkedHashMap<DataID<?>, C> map = new LinkedHashMap<>(this.size());
		for (C record : this)
		{
			map.put(record.getRecordID(), record);
		}
		return map;
	}

	public static String getOrderBy(String... pColumns)
	{
		if (pColumns == null)
			return null;
		StringBuilder toReturn = new StringBuilder(ORDER_BY);
		for (int i = 0; i < pColumns.length; i++)
		{
			toReturn.append(pColumns[i]);
			if ((i + 1) < pColumns.length)
				toReturn.append(", ");
		}
		return toReturn.toString();
	}
}
