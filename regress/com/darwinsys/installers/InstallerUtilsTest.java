package com.darwinsys.installers;

import java.io.IOException;

import javax.swing.JFrame;

/**
 * Manually test out the InstallerUtils method(s).
 */
public class InstallerUtilsTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		JFrame jf = new JFrame("Just a test");
		try {
			InstallerUtils.acceptLicense(jf, "/fooble/grunch");
		} catch (IOException e) {
			System.out.println("Caught expected IOException");
		}
		boolean accepted = InstallerUtils.acceptLicense(jf,
				"regress/com/darwinsys/installers/InstallerUtilsTest.java");
		System.out.printf("You %s accept the license%n", accepted ? "did" : "did not");

		// Now test with HTML
		InstallerUtils.acceptLicense(jf, "src/com/darwinsys/installers/package.html");
	}
}
