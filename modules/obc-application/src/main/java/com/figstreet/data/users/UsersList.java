package com.figstreet.data.users;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.HibernateList;

public class UsersList extends HibernateList<Users>
{

	private static final long serialVersionUID = -3127441775201214317L;

	public static UsersList loadAll(int pLimit) throws SQLException
	{
		return Users.DB_CONNECTOR.loadList(null, "ORDER BY " + Users.ID_COLUMN + " ASC", pLimit);
	}

	public static UsersList loadByEmail(String pEmail) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(Users.EMAIL_COLUMN);
		query.append(" = :");
		query.append(Users.EMAIL_COLUMN);
		args.put(Users.EMAIL_COLUMN, pEmail);
		return Users.DB_CONNECTOR.loadList(query.toString(), args, "ORDER BY " + Users.ID_COLUMN + " ASC", -1);
	}

	public static UsersList loadByDisplayID(String pDisplayId) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(Users.DISPLAYID_COLUMN);
		query.append(" = :");
		query.append(Users.DISPLAYID_COLUMN);
		args.put(Users.DISPLAYID_COLUMN, pDisplayId);
		return Users.DB_CONNECTOR.loadList(query.toString(), args, "ORDER BY " + Users.ID_COLUMN + " ASC", -1);
	}

}
