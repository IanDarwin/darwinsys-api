package jabacart;

/** Checked exception for failed insertions */
public class InsertException extends DataBaseException {
	public InsertException() {
		super();
	}
	public InsertException(String msg) {
		super(msg);
	}
}
