package com.figstreet.data.userpermission;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.figstreet.data.users.UsersID;

public class UserPermissionMap implements Serializable
{
	private static final long serialVersionUID = 7017948731639595825L;

	private UsersID fUsersID;
	private Map<UserPermissionType, UserPermission> fUserPermissionMap;

	public UserPermissionMap()
	{
		this.fUserPermissionMap = new HashMap<UserPermissionType, UserPermission>(1);
	}

	public UserPermissionMap(UsersID pUsersID) throws SQLException
	{
		this(pUsersID, UserPermissionList.loadByUsersID(pUsersID));
	}

	public UserPermissionMap(UsersID pUsersID, UserPermissionList pUserPermissionList)
	{
		if (pUsersID == null || pUserPermissionList == null)
			throw new IllegalArgumentException("pUsersID and pUserPermissionList are required");

		this.fUsersID = pUsersID;
		this.fUserPermissionMap = new HashMap<UserPermissionType, UserPermission>(pUserPermissionList.size());
		for (UserPermission userPermission : pUserPermissionList)
		{
			this.fUserPermissionMap.put(userPermission.getType(), userPermission);
		}
	}

	public UsersID getUsersID()
	{
		return this.fUsersID;
	}

	public boolean getHasPermission(UserPermissionType pType)
	{
		UserPermission userPermission = this.fUserPermissionMap.get(pType);
		if (userPermission == null)
			return false;
		return userPermission.isActive();
	}

	public UserPermission findPermission(UserPermissionType pType)
	{
		return this.fUserPermissionMap.get(pType);
	}

}
