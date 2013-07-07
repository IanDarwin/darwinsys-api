package com.darwinsys.notepad;

import java.io.IOException;
import java.util.prefs.Preferences;

/** A trivial TODO editor (not a real TODO PIM!) showing use of
 * NotePad with a provided Preferences node.
 */
public class ToDo {

	/** Pass Preferences node so we dont get same location as Notepad */
	static Preferences prefs = Preferences.userNodeForPackage(ToDo.class);

	public static void main(String[] args) throws IOException {
		Thread.currentThread().setName("ToDo");
		String fileName = System.getProperty("user.home") + "/" + "TODO.txt";

		Notepad n = new Notepad(prefs);
		n.doLoad(fileName);
	}
}
