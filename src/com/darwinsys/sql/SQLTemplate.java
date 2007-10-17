package com.darwinsys.sql;

public class SQLTemplate {
	private String alias;
	private String template;

	private final static SQLTemplate[] DEFAULTS = {
		new SQLTemplate("Display tables", "\\dt"),
		new SQLTemplate("Select", "SELECT * from TABLE"),
		new SQLTemplate("Select w/where", "SELECT * from TABLE where x = y"),
		new SQLTemplate("Insert", "INSERT into TABLE(col,col) VALUES(val,val)"),
		new SQLTemplate("Update", "UPDATE TABLE set x = y where x = y"),
	};

	public SQLTemplate(String alias, String template) {
		super();
		this.alias = alias;
		this.template = template;
	}

	public static SQLTemplate[] getList() {
		return DEFAULTS;
	}

	@Override
	public String toString() {
		return alias;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
}
