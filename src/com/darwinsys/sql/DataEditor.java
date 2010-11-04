package com.darwinsys.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;

public class DataEditor {
	Connection conn;
	String tableName;
	JFrame parent;
	
	public DataEditor(JFrame parent, Connection conn, String tableName) {
		super();
		this.parent = parent;
		this.conn = conn;
		this.tableName = tableName;
		
		try {
			final DatabaseMetaData dbMetaData = conn.getMetaData();
			ResultSet rs = dbMetaData.getTables("", "%", tableName, null);
			rs.last();
			System.out.println("Number of tables = " + rs.getRow());
			getColumnInfo(conn, tableName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getColumnInfo(Connection conn, String tableName) throws SQLException {
		PreparedStatement pc = conn.prepareStatement(
				"select * from " + tableName, 
				ResultSet.TYPE_SCROLL_SENSITIVE, 
				ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = pc.executeQuery();
		final ResultSetMetaData metaData = rs.getMetaData();
		System.out.println(metaData.getColumnCount());
	}
	
	public static void main(String[] args) {
		Connection conn = ConnectionUtil.getConnection("pvtschool");
		DataEditor de = new DataEditor((JFrame)null, conn, "school");
	}
}
