import java.io.IOException;
import java.io.PrintWriter;

/**
 * Callback so that ResultsDecorator can call invoker to handle redirections etc.
 * @version $Id$
 */
public interface ResultsDecoratorPrinter {
	
	void print(String line) throws IOException;
	
	void println(String line) throws IOException;
	
	void println() throws IOException;
	
	PrintWriter getPrintWriter();
}
