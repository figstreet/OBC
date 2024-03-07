package com.figstreet.data.configvalue;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.ClientID;
import com.figstreet.core.HibernateList;

public class ConfigValueList extends HibernateList<ConfigValue>
{
	private static final long serialVersionUID = -9155723160441168188L;

	public static ConfigValueList loadAll(int pLimit) throws SQLException
	{
		return ConfigValue.DB_CONNECTOR.loadList(null, "ORDER BY " + ConfigValue.CONFIG_TYPE_COLUMN + " ASC", pLimit);
	}

	public static ConfigValueList loadByTypeAndNameForClient(ClientID pClientID, String pConfigType, String pPropertyName) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(3);
		StringBuilder query = new StringBuilder(ConfigValue.CLIENTID_COLUMN);
		query.append(" = :");
		query.append(ConfigValue.CLIENTID_COLUMN);
		args.put(ConfigValue.CLIENTID_COLUMN, pClientID);
		query.append(" AND ");
		query.append(ConfigValue.CONFIG_TYPE_COLUMN);
		query.append(" = :");
		query.append(ConfigValue.CONFIG_TYPE_COLUMN);
		args.put(ConfigValue.CONFIG_TYPE_COLUMN, pConfigType);
		if (pPropertyName != null)
		{
			query.append(" AND ");
			query.append(ConfigValue.PROPERTY_NAME_COLUMN);
			query.append(" = :");
			query.append(ConfigValue.PROPERTY_NAME_COLUMN);
			args.put(ConfigValue.PROPERTY_NAME_COLUMN, pConfigType);
		}
		return ConfigValue.DB_CONNECTOR.loadList(query.toString(), args, null, -1);
	}

}
