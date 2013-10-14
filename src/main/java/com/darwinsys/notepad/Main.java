package com.darwinsys.notepad;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length == 0)
			new Notepad();
		else {
			for (String arg : args) {
				new Notepad().doLoad(arg);
			}
		}
	}
}
