import com.darwinsys.util.FileIO;

import java.io.*;

public class FileIOTest {
    public static void main(String[] av) {
		try {
			FileIO.copyFile("FileIO.java", "FileIO.bak");
			FileIO.copyFile("FileIO.class", "FileIO-class.bak");
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
