import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Base class for a series of ResultSet printers.
 * @version $Id$
 */
public abstract class ResultsDecorator {
	ResultSet rs;
	PrintWriter out;
	ResultsDecorator(PrintWriter out){
		this.out = out;
	}
	abstract void write(ResultSet rs) throws SQLException;
	abstract void write(int rowCount) throws SQLException;
}