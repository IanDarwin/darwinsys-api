package regress;

import com.darwinsys.io.FileIO;
import java.io.*;

public class FileIOTest {
    public static void main(String[] av) {
		if (av.length < 0)
			throw new IllegalArgumentException("Usage: FileIOTest filename");
		String fileName = av[0];
		String targetFileName = av[0] + ".bak";
		try {
			FileIO.copyFile(fileName, targetFileName);
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
