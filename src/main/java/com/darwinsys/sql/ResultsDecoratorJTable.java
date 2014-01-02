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

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.darwinsys.util.Verbosity;

/** Print ResultSet in HTML
 */
public class ResultsDecoratorJTable extends ResultsDecorator {
	JTable table;

	public ResultsDecoratorJTable(JTable table, PrintWriter out, Verbosity v) {
		super(out, v);
		this.table = table;
	}

	public int write(final ResultSet rs) throws IOException, SQLException {

		final ResultSetMetaData md = rs.getMetaData();
		final int colCount = md.getColumnCount();

		// Create a JTableModel to hold the data; map 1-origin SQL row/col to/from 0-origin JTable row/col
		TableModel dataModel = new TableModel() {

			public Class<?> getColumnClass(int columnIndex) {
				try {
					return Class.forName(md.getColumnClassName(1+columnIndex));
				} catch (Exception e) {
					return null;
				}
			}

			public int getColumnCount() {
				return colCount;
			}

			public String getColumnName(int columnIndex) {
				try {
					return md.getColumnLabel(1+columnIndex);
				} catch (SQLException e) {
					e.printStackTrace();
					return "unknown";
				}
			}

			public int getRowCount() {
				try {
					rs.last();
					return rs.getRow();
				} catch (SQLException e) {
					e.printStackTrace();
					return -1;
				}
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				try {
					rs.absolute(1+rowIndex);
					return rs.getObject(1+columnIndex);
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}

			/** All read-only for now */
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}

			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				throw new IllegalArgumentException("Model is read-only");
			}

			// LISTENER SUPPORT
			public void addTableModelListener(TableModelListener l) {
                // ??
			}

			public void removeTableModelListener(TableModelListener l) {
                // ??
			}
		};

		// populate it
		table.setModel(dataModel);
		
		// Something the UI interface design forgot
		for (int i = 0; i < colCount; i++) {
			// XXX put column name as tooltip
		}

		return dataModel.getRowCount();
	}

	@Override
	public void displayTable(String table, ResultSet rs) throws IOException, SQLException {
		write(rs);
	}

	/** Return a printable name for this decorator
	 * @see ResultsDecorator#getName()
	 */
	public String getName() {
		return "JTable";
	}
}
