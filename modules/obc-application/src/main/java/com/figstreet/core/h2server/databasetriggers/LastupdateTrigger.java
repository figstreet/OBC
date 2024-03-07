package com.figstreet.core.h2server.databasetriggers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.figstreet.core.FormatUtil;
import org.h2.api.Trigger;

public class LastupdateTrigger implements Trigger
{
	private int fLastupdateColumn = -1;

	/**
	 * This method is called for each triggered action.
	 *
	 * @param pDbCon a connection to the database
	 * @param pOldRow the old row, or null if no old row is available (for
	 *            INSERT)
	 * @param pNewRow the new row, or null if no new row is available (for
	 *            DELETE)
	 * @throws SQLException if the operation must be undone
	 */
	@Override
	public void fire(Connection pDbCon, Object[] pOldRow, Object[] pNewRow) throws SQLException
	{
		if (this.fLastupdateColumn > 0 && pNewRow != null)
		{
			pNewRow[this.fLastupdateColumn - 1] = new Timestamp((new java.util.Date()).getTime());
		}
	}

	/**
	 * Initializes the trigger.
	 *
	 * @param pDbCon a connection to the database
	 * @param pSchemaName the name of the schema
	 * @param pTriggerName the name of the trigger used in the CREATE TRIGGER
	 *            statement
	 * @param pTableName the name of the table
	 * @param pBefore whether the fire method is called before or after the
	 *            operation is performed
	 * @param pType the operation type: INSERT, UPDATE, or DELETE
	 */
	@Override
	public void init(Connection pDbCon, String pSchemaName, String pTriggerName, String pTableName, boolean pBefore,
		int pType) throws SQLException
	{
		if (this.fLastupdateColumn < 0)
		{
			String sql = "select ordinal_position from INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='"
				+ FormatUtil.escapeSQLServerObject(pTableName) + "' and COLUMN_NAME like '%_LASTUPDATE_DT'";
			Statement stmt = null;
			ResultSet rs = null;
			try
			{
				stmt = pDbCon.createStatement();
				rs = stmt.executeQuery(sql);
				if (rs.next())
					this.fLastupdateColumn = rs.getInt(1);
			}
			finally
			{
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}

		}

	}

	@Override
	public void remove() throws SQLException
	{
		//empty
	}

	@Override
	public void close() throws SQLException
	{
		//empty
	}

}
