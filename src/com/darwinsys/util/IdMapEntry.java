package jabacart;

/**
 * An IdMapping is one int-String pair, for example, a primary key in a
 * database and a name or description of the item in the named row.
 * IdMapping objects are immutable.
 * <p>Rather like a java.util.Map.Entry but without needing to convert.
 * @version $Id$
 */
public class IdMapping {
	int id;
	String name;
	public IdMapping(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
