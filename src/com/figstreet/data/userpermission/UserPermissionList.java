package com.figstreet.data.userpermission;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.figstreet.core.HibernateList;
import com.figstreet.data.users.UsersID;

public class UserPermissionList extends HibernateList<UserPermission>
{
	private static final long serialVersionUID = -5564113339112926109L;

	public static UserPermissionList loadByUsersID(UsersID pUsersID) throws SQLException
	{
		LinkedHashMap<String, Object> args = new LinkedHashMap<>(1);
		StringBuilder query = new StringBuilder(UserPermission.USERSID_COLUMN);
		query.append(" = :");
		query.append(UserPermission.USERSID_COLUMN);
		args.put(UserPermission.USERSID_COLUMN, pUsersID);
		return UserPermission.DB_CONNECTOR.loadList(query.toString(), args,
				"ORDER BY " + UserPermission.ID_COLUMN + " ASC", -1);
	}

}
