package regress;

import com.darwinsys.swingui.SimpleHelp;

public class SimpleHelpDemo {
	/** Main */
	public static void main(String argv[]) {
		if (argv.length == 0)
			throw new IllegalArgumentException(
			"Usage: SimpleHelpDemo helpFile");
		new SimpleHelp("TESTING", argv[0]).setVisible(true);
	}
}
