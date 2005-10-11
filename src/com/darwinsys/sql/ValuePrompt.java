package com.darwinsys.sql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/** Prompt for a value in a database.
 * Usage: ValuePrompt connection table column "expression"
 * e.g.
 * <pre>
 * java ValuePrompt mydb products location "location is null or location = 0"
 * </pre>
 * @author ian
 */
public class ValuePrompt {

    public static void main(String args[]) {
    	String connection = args[0];
    	String table = args[1];
    	String column = args[2];
    	String expression = args[3];
        
        try {
        	Connection con = ConnectionUtil.getConnection(connection);         

        	BufferedReader is = 
        		new BufferedReader(new InputStreamReader(System.in));
			Statement stmt = con.createStatement(
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = "SELECT * FROM " + table +
					" where " + expression;
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Starting processing: query = " + query);
			while (rs.next()) {
				System.out.println(rs.getObject(1));
				String resp = is.readLine();
				if (resp == null || "".equals(resp))
					continue;
				rs.updateString(column, resp);
				rs.updateRow();
			}
			System.out.println("All done.");
			rs.close();
			stmt.close();
            con.close();
        } catch(Exception ex) {
            System.err.println("Exception: " + ex);
        }
	}
}
