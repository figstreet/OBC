package com.figstreet.data.codes;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.HibernateList;

public class CodesList extends HibernateList<Codes>
{
	private static final long serialVersionUID = -8569715396710836078L;

	public static CodesList loadAll(int pLimit) throws SQLException
	{
		return Codes.DB_CONNECTOR.loadList(null, "ORDER BY " + Codes.TYPE_COLUMN + " ASC", pLimit);
	}

	public static CodesList loadByType(CodesType pCodesType) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(Codes.TYPE_COLUMN);
		query.append(" = :");
		query.append(Codes.TYPE_COLUMN);
		args.put(Codes.TYPE_COLUMN, CodesType.asString(pCodesType));
		return Codes.DB_CONNECTOR.loadList(query.toString(), args, "ORDER BY " + Codes.VALUE_COLUMN + " ASC", -1);
	}

	public static CodesList loadByTypeAndValue(CodesType pCodesType, String pValue) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(2);
		StringBuilder query = new StringBuilder(Codes.TYPE_COLUMN);
		query.append(" = :");
		query.append(Codes.TYPE_COLUMN);
		args.put(Codes.TYPE_COLUMN, CodesType.asString(pCodesType));

		if (pValue != null)
		{
			query.append(" AND ");
			query.append(Codes.VALUE_COLUMN);
			query.append(" = :");
			query.append(Codes.VALUE_COLUMN);
			args.put(Codes.VALUE_COLUMN, pValue);
		}

		return Codes.DB_CONNECTOR.loadList(query.toString(), args, null, -1);
	}

}
