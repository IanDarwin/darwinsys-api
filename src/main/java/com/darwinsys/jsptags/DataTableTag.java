package com.darwinsys.jsptags;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.sql.DataSource;

import com.darwinsys.sql.SQLUtils;

/** A simple DataTable or DataGrip JSP tag.
 * Sample JSP usage:
 * <pre>
 * &lt;h3&gt;Products Apparently In Stock but Location = 0&lt;/h3&gt;
 * &lt;darwin:datatable dataSource="${applicationScope.myproject_DATASOURCE}"
 * 	style1="odd" style2="even"
 * 	pkey='sku' link='/productdetails.do?sku='&gt;
 * 	select sku, stockCount, title from products
 *    where stockCount &gt; 0 and location = 0
 * &lt;/darwin:datatable&gt;
 * </pre>
 * @author Ian Darwin
 */
public class DataTableTag extends BodyTagSupport {

	private static final long serialVersionUID = -378231759603927261L;
	public static final String J2EE_ENC_ROOT = "java:comp/env/";
	/** A JNDI name to look up a JDBC DataSource */
	private String dsName;
	/** The DataSource that is looked up there */
	private DataSource dataSource;
	/** Non-datasource JDBC Parameters */
	private String dbURL, dbDriver, dbUsername, dbPassword;
	/** A ResultSet, either computed or passed in */
	private ResultSet resultSet;
	/** The Query String */
	private String query;
	/** The CSS style for the title row */
	private String titleStyle = "odd";
	/** The CSS Style for rows 2, 4, 6 ... */
	private String style1 = "odd";
	/** The CSS Style for the data rows 1, 3, 5 ... */
	private String style2 = "even";
	/** The name of the primary key column, which must appear in query */
	private String pkey;
	/** The link text to make a link from the pkey column to the detail page */
	private String link;

	@Override
	public int doEndTag() throws JspException {
		if (query == null && bodyContent != null) {
			query = bodyContent.getString();
		}
		if (query == null || "".equals(query)) {
			throw new JspException(
			"Query must be provided, as an attribute or as BodyContent.");
		}

		final JspWriter out = pageContext.getOut();
		Connection conn = null;
		Statement statement = null;
		JspException exceptionToThrow = null;
		try {
			conn = getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
			SQLUtils.resultSetToHTML(resultSet, new PrintWriter(out),
				style1, style1, style2, pkey, link);

		} catch (SQLException e) {
			exceptionToThrow = new JspException("Database error", e);
		} finally {
			SQLUtils.cleanup(resultSet, statement, conn);
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
		}
		resetFields();
		return EVAL_PAGE;
	}

	/**
	 * reset to avoid accidental reuse
	 */
	private void resetFields() {
		query = null;
		dataSource = null;
	}

	/** Get the connection from the saved DataSource.
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection() throws SQLException, JspException {
		if (dataSource == null) {
			throw new JspException(
				"Error: dataSource was null when I needed it.");
		}
		return dataSource.getConnection();
	}

	public String getDataSourceName() {
		return dsName;
	}

	/** Treat the provided name as a JNDI lookup in the J2EE ENC;
	 * if it is a relative path, prepend the ENC path.
	 * In either case, set both the dsName field and the ds field.
	 * @param dsn The name to look up the DataSource in JNDI.
	 */
	public void setDataSourceName(String dsn) {
		this.dsName = dsn.startsWith(J2EE_ENC_ROOT) ? dsn : J2EE_ENC_ROOT+dsn;
		try {
			setDataSource((DataSource)new InitialContext().lookup(dsn));
		} catch (NamingException e) {
			String message = "StrutsDataTableTag.setDataSourceName(): error " + e;
			System.err.println(message);
			throw new IllegalArgumentException(message);
		}
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}


	public String getStyle1() {
		return style1;
	}

	public void setStyle1(String style1) {
		this.style1 = style1;
	}

	public String getStyle2() {
		return style2;
	}

	public void setStyle2(String style2) {
		this.style2 = style2;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public String getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(String titleStyle) {
		this.titleStyle = titleStyle;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
}
