/* Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 2004-2006.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.darwinsys.sql;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.darwinsys.util.Verbosity;

/**
 * Print an SQL ResultSet in SQL-import format.
 */
public class ResultsDecoratorSQL extends ResultsDecorator {

	public ResultsDecoratorSQL(PrintWriter out, Verbosity v) {
		super(out, v);
	}

	/** Write a "normal" (data-holding) ResultSet
	 */
	@Override
	public int write(ResultSet rs) throws IOException, SQLException {
		ResultSetMetaData metadata = rs.getMetaData();
		// This assumes you're not using a Join!!
		String tableName = metadata.getTableName(1);
		if (tableName == null) {
			tableName = "XXXTABLENAMEXXX";
			System.err.println("Warning: at least one tablename null");
		}
		int colCount = metadata.getColumnCount();
		StringBuffer sb = new StringBuffer("insert into ").append(tableName).append("(");
		for (int i = 1; i <= colCount; i++) {
			sb.append(metadata.getColumnName(i));
			if (i != colCount) {
				sb.append(", ");
			}
		}
		sb.append(") values (");
		String insertCommand = sb.toString();

		int rowCount = 0;
		while (rs.next()) {
			++rowCount;
			println(insertCommand);

			for (int i = 1; i <= colCount; i++) {
				String tmp = rs.getString(i);
				if (rs.wasNull()) {
					print("null");
				} else {
					// Numbers go unchanged; Strings get squote doubling
					// and wrap in dquotes; dates/times/etc get wrapped
					// in dquotes; default case goes unchanged.
					switch (metadata.getColumnType(i)) {
						case Types.BIGINT:
						case Types.DECIMAL:
						case Types.DOUBLE:
						case Types.FLOAT:
						case Types.INTEGER:
							// Do nothing
							break;
						case Types.CHAR:
						case Types.CLOB:
						case Types.VARCHAR:
						case Types.LONGVARCHAR:
							tmp = duplicateSingleQuotes(tmp);
							tmp = wrapInDoubleQuotes(tmp);
							break;
						case Types.DATE:
						case Types.TIME:
						case Types.TIMESTAMP:
							tmp = wrapInDoubleQuotes(tmp);
							break;
						default:
							// Do Nothing
						break;
					}
					print(tmp);
				}
				if (i != colCount) {
					print( ", ");
				}
			}
			println(");");
		}
		return rowCount;
	}

	/** Display this resultset assuming it is a
	 * Table description.
	 */
	@Override
	public void displayTable(String tableName, ResultSet rs) throws IOException, SQLException {
		
		println("create table " + tableName + " (");
		while (rs.next()) {
			print("\t" + rs.getString(4) + ' ' + rs.getString(6));
			int nullable = rs.getInt(11);
			if (nullable == ResultSetMetaData.columnNoNulls) {
				print(" not null");
			}
			String defaultValue = rs.getString(13);
			if (defaultValue != null) {
				print(" default('" + defaultValue + "')");
			}
			println(",");
		}
		println(");");
	}


	/**
	 * Double the quotes in a string
	 * @param input
	 * @return
	 */
	private String duplicateSingleQuotes(String input) {
		return input.replaceAll("'", "''");
	}

	/**
	 * Wrap a string in double quotes
	 * @param input
	 * @return
	 */
	private String wrapInDoubleQuotes(String input) {
		return String.format("'%s'", input);
	}

	@Override
	public void printRowCount(int rowCount) throws IOException {
		println("-- RowCount: " + rowCount);

	}
	/* (non-Javadoc)
	 * @see ResultsDecorator#getName()
	 */
	@Override
	public String getName() {
		return "SQL";
	}
}
