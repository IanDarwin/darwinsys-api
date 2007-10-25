package com.darwinsys.sql;

public class SQLTemplate {
	private String alias;
	private String template;

	private final static SQLTemplate[] DEFAULTS = {
		new SQLTemplate("Display table(s)", "\\dt"),
		new SQLTemplate("Select", "Select * from TABLE"),
		new SQLTemplate("Select w/where", "Select * from TABLE where x = y"),
		new SQLTemplate("Insert", "Insert into TABLE(col,col) Values(val,val)"),
		new SQLTemplate("Update", "Update TABLE set x = y where x = y"),
		new SQLTemplate("Drop Table", "Drop Table TABLENAME"),
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
