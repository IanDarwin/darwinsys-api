package com.darwinsys.swingui;

import javax.swing.table.*;
import java.util.*;

/** JTable model for ArrayList of heterogeneous objects.
 * Subclasses must set String columnNames[] and
 * Class columnClasses[], which MUST be in the same order.
 * Subclasses need only implement these AbstractTableModel methods:
 * <pre>
 * public int getColumnCount() {
 * public Object getValueAt(int row, int col)  {
 * public void setValueAt(Object val, int row, int col)  {
 * </pre>
 */
public abstract class ArrayListTableModel extends AbstractTableModel {

	/** List of column names, must be provided by subclass. */
	protected String columnNames[];
	/** List of column names, must be provided by subclass. */
	protected Class columnClasses[];

	/** The list of Method object */
	protected ArrayList methods = null;

	/** for caching. */
	private int ROW_INVALID = -1;
	/** for caching, shared by get/set ValueAt */
	private int prevRow = ROW_INVALID;
	/** for caching, shared by get/set ValueAt */
	private Object current  = null;

	/** Constructor requires the list of objects */
	public ArrayListTableModel(ArrayList m) {
		methods = m;
	}

	/** Allow the model to load the ArrayList all at once, as when
	 * loading a file.
	 */
	void setListData(ArrayList v) {
		methods = v;
		invalidateCache();
	}

	/** Get the name of a given column, from the list provided by subclass */
	public String getColumnName(int n) {
		if (columnNames == null)
			throw new IllegalStateException("columnNames not set");
		int max = columnNames.length;
		if (n>=max)
			throw new ArrayIndexOutOfBoundsException(
			"columnNames has " + max + " elements; you asked for " + max);
		return columnNames[n];
	}

	/** Get the class of a given column, from the list provided by subclass */
	public Class getColumnClass(int n) {
		if (columnClasses == null)
			throw new IllegalStateException("columnClasses not set");
		int max = columnClasses.length;
		if (n>=max)
			throw new ArrayIndexOutOfBoundsException(
			"columnClasses has " + max + " elements; you asked for " + max);
		return columnClasses[n];
	}

	/** Returns the number of objects in the list. */
	public int getRowCount()  {
		return methods.size();
	}

	/** All cells are editable.
	 * Subclasses can override this if only some cells should be editable.
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	/** Cache one most-recently-used item. This is a convenience
	 * routine that subclasses are invited but not required to use.
	 * Normal use would be, in get/setValueAt():
	 * <pre>
	 *		public void setValueAt(int row, ...) {
	 *			MyDataType current = (MyDataType) getCached(row);
	 *			...
	 *		}
	 * </pre>
	 */
	public Object getCached(int row) {
		if (row != prevRow) {
			current = methods.get(row);
			prevRow = row;
		}
		return current;
	}

	/** Invalidate the cache. Called automatically by setListData();
	 * must be called if you otherwise change the ArrayList.
	 */
	public void invalidateCache() {
		prevRow = ROW_INVALID;
	}
}
