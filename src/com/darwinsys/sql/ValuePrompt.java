package com.darwinsys.sql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/** Prompt for a value in a database.
 * Usage: ValuePrompt connection "select expression"
 * e.g.
 * <pre>
 * java ValuePrompt mydb "select title, product, location from products  where location is null or location = 0"
 * </pre>
 * @author ian
 */
public class ValuePrompt {

	public static final String USAGE =
		"ValuePrompt connection-name \"select query...\"";

	public static void main(String args[]) {
    	if (args.length == 1 && args[0].startsWith("-h")) {
    		System.err.println(USAGE);
    		System.exit(0);
    	}
    	if (args.length != 2) {
    		System.err.println(USAGE);
    		System.exit(1);
    	}
    	
    	String connection = args[0];
    	String query = args[1];
        
        try {
        	Connection con = ConnectionUtil.getConnection(connection);         

        	BufferedReader is = 
        		new BufferedReader(new InputStreamReader(System.in));
			Statement stmt = con.createStatement(
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Starting processing: query = " + query);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int colCount = rsMetaData.getColumnCount();
			while (rs.next()) {
				boolean changed = false;
				for (int i = 1; i <= colCount; i++) {
					System.out.printf("%s=%s: ", rsMetaData.getColumnName(i), rs.getObject(i));
					String resp = is.readLine();
					if (resp == null || "".equals(resp))
						continue;
					rs.updateString(i, resp);
					changed = true;
				}
				if (changed) {
					rs.updateRow();
				}
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
