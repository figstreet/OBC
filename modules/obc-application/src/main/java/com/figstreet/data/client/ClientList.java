package com.figstreet.data.client;

import com.figstreet.core.HibernateList;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ClientList extends HibernateList<Client>
{

	public static ClientList loadAll(boolean pActiveOnly) throws SQLException
	{
		if (!pActiveOnly)
			return Client.DB_CONNECTOR.loadList(null, getOrderBy(Client.ID_COLUMN + " ASC"), -1);

		LinkedHashMap<String, Object> args = new LinkedHashMap<>(3);
		StringBuilder query = new StringBuilder(Client.ACTIVE_COLUMN);
		query.append(" = :");
		query.append(Client.ACTIVE_COLUMN);
		args.put(Client.ACTIVE_COLUMN, pActiveOnly);

		return Client.DB_CONNECTOR.loadList(query.toString(), args, getOrderBy(Client.ID_COLUMN + " ASC"), -1);
	}

}
