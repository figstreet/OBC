package com.figstreet.core;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.TypedQuery;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

public class HibernateDBConnector<C extends HibernateDatabaseObject<J>, L extends HibernateList<C>, J extends DataID<?>>
{
	private static final String LOGGER_NAME = HibernateDBConnector.class.getPackage().getName()
			+ ".HibernateDBConnector";

	private Class<C> fRecordClass;
	private Class<L> fListClass;
	private Class<J> fIDClass;
	private String fHibernateMappedName;
	private Constructor<L> fListConstructor;
	private Constructor<J> fIDConstructor;
	private String fPrimaryKey;

	public HibernateDBConnector(Class<C> pRecordClass, String pHibernateMappedName, Class<L> pListClass,
			Class<J> pIDClass, String pPrimaryKey)
	{
		this.fRecordClass = pRecordClass;
		this.fHibernateMappedName = pHibernateMappedName;
		this.fListClass = pListClass;
		this.fIDClass = pIDClass;
		this.fPrimaryKey = pPrimaryKey;
	}

	public Class<C> getRecordClass()
	{
		return this.fRecordClass;
	}

	public String getHibernateMappedName()
	{
		return this.fHibernateMappedName;
	}

	public String getPrimaryKey()
	{
		return this.fPrimaryKey;
	}

	private Constructor<L> getListConstructor() throws NoSuchMethodException
	{
		if (this.fListConstructor == null)
			this.fListConstructor = this.fListClass.getConstructor();
		return this.fListConstructor;
	}

	private Constructor<J> getIDConstructor() throws NoSuchMethodException
	{
		if (this.fIDConstructor == null)
			this.fIDConstructor = this.fIDClass.getConstructor(String.class);
		return this.fIDConstructor;
	}

	public boolean deleteByID(J pRecordID) throws SQLException
	{
		boolean deleted = false;
		if (this.getPrimaryKey() != null)
		{
			HibernateTransaction trans = HibernateTransaction.open();
			Session session = trans.getSession();
			try
			{
				StringBuilder delQuery = new StringBuilder("DELETE FROM ");
				delQuery.append(this.getHibernateMappedName());
				delQuery.append(" WHERE ");
				delQuery.append(this.getPrimaryKey());
				delQuery.append(" = ");
				delQuery.append(pRecordID);
				Logging.debug(LOGGER_NAME, "deleteByID", "Query: " + delQuery.toString());
				TypedQuery<C> query = session.createQuery(delQuery.toString());
				int row = query.executeUpdate();
				deleted = row != 0;
				trans.commit();
			}
			catch (HibernateException he)
			{
				throw new SQLException(he);
			}
			finally
			{
				trans.close();
			}
		}
		return deleted;
	}

	public void saveOrUpdate(C pToStore) throws SQLException
	{
		HibernateTransaction trans = HibernateTransaction.open();
		Session session = trans.getSession();
		try
		{
			session.saveOrUpdate(pToStore);
			trans.commit();
		}
		catch (HibernateException he)
		{
			throw new SQLException(he);
		}
		finally
		{
			trans.close();
		}
	}

	public void delete(C toDelete) throws SQLException
	{
		HibernateTransaction trans = HibernateTransaction.open();
		Session session = trans.getSession();
		try
		{
			session.delete(toDelete);
			trans.commit();
		}
		catch (HibernateException he)
		{
			throw new SQLException(he);
		}
		finally
		{
			trans.close();
		}
	}

	public C loadRecord(J pRecordID, boolean pMustExist) throws SQLException
	{
		return this.loadRecord(pRecordID, pMustExist, LockMode.NONE);
	}

	@SuppressWarnings("unchecked")
	public C loadRecord(J pRecordID, boolean pMustExist, LockMode pLockMode) throws SQLException
	{
		C toReturn = null;
		if (pRecordID == null)
		{
			if (pMustExist)
				throw new IllegalArgumentException("RecordID cannot be null");
			else
				return toReturn;
		}

		HibernateTransaction trans = HibernateTransaction.open();
		Session session = trans.getSession();
		try
		{
			Object fromDB = session.get(this.getRecordClass(), pRecordID, pLockMode);
			if (fromDB == null && pMustExist)
			{
				String msg = String.format("No record found for class %s with ID %s", this.getRecordClass(),
						pRecordID.toString());
				throw new SQLException(msg);
			}
			if (fromDB != null)
				toReturn = (C) fromDB;
			trans.commit();
		}
		catch (HibernateException he)
		{
			throw new SQLException(he);
		}
		finally
		{
			trans.close();
		}
		return toReturn;
	}

	public L loadList(String pWhereClause, String pOrderBy, int pLimit) throws SQLException
	{
		return this.loadList(pWhereClause, null, pOrderBy, pLimit);
	}

	@SuppressWarnings("unchecked")
	public L loadList(String pWhereClause, LinkedHashMap<String, Object> pArgs, String pOrderBy, int pLimit)
			throws SQLException
	{
		L toReturn;
		try
		{
			toReturn = this.getListConstructor().newInstance();
		}
		catch (Exception e)
		{
			throw new SQLException(e);
		}

		HibernateTransaction trans = HibernateTransaction.open();
		Session session = trans.getSession();
		try
		{
			StringBuilder sb = new StringBuilder(" FROM ");
			sb.append(this.getHibernateMappedName());
			if (pWhereClause != null && pWhereClause.length() > 0)
			{
				sb.append(" WHERE ");
				sb.append(pWhereClause);
			}
			if (pOrderBy != null && pOrderBy.length() > 0)
			{
				sb.append(" ");
				sb.append(pOrderBy);
			}
			Logging.debug(LOGGER_NAME, "loadList", "Query: " + sb.toString());
			TypedQuery<C> query = session.createQuery(sb.toString());
			if (pLimit >= 0)
			{
				query.setFirstResult(0);
				query.setMaxResults(pLimit);
			}
			if (pArgs != null && !pArgs.isEmpty())
			{
				for (Entry<String, Object> arg : pArgs.entrySet())
					query.setParameter(arg.getKey(), arg.getValue());
			}

			toReturn.addAll(query.getResultList());
			trans.commit();
		}
		catch (HibernateException he)
		{
			throw new SQLException(he);
		}
		finally
		{
			trans.close();
		}
		return toReturn;
	}

	public List<J> loadPkList(String pWhereClause, LinkedHashMap<String, Object> pArgs, String pOrderBy, int pLimit)
			throws SQLException
	{
		List<J> pkList = null;
		List<?> dbList = null;
		HibernateTransaction trans = HibernateTransaction.open();
		Session session = trans.getSession();
		try
		{
			StringBuilder sb = new StringBuilder("SELECT ");
			sb.append(this.fPrimaryKey);
			sb.append(" FROM ");
			sb.append(this.getHibernateMappedName());
			if (pWhereClause != null && pWhereClause.length() > 0)
			{
				sb.append(" WHERE ");
				sb.append(pWhereClause);
			}
			if (pOrderBy != null && pOrderBy.length() > 0)
			{
				sb.append(" ");
				sb.append(pOrderBy);
			}
			Logging.debug(LOGGER_NAME, "loadPkList", "Query: " + sb.toString());
			NativeQuery<?> query = session.createSQLQuery(sb.toString());
			if (pLimit >= 0)
			{
				query.setFirstResult(0);
				query.setMaxResults(pLimit);
			}
			if (pArgs != null && !pArgs.isEmpty())
			{
				for (Entry<String, Object> arg : pArgs.entrySet())
					query.setParameter(arg.getKey(), arg.getValue());
			}

			dbList = query.list();
			trans.commit();
		}
		catch (HibernateException he)
		{
			throw new SQLException(he);
		}
		finally
		{
			trans.close();
		}

		if (dbList != null)
		{
			//If values were found, convert to the ID type
			try
			{
				pkList = new ArrayList<J>(dbList.size());

				Constructor<J> idConstructor = this.getIDConstructor();
				for (Object idValue : dbList)
				{
					if (idValue != null)
						pkList.add(idConstructor.newInstance(idValue.toString()));
				}
			}
			catch (Exception e)
			{
				throw new SQLException(e);
			}
		}

		return pkList;
	}

}
