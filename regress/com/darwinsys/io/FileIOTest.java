import com.darwinsys.util.*;
import java.io.*;

public class FileIOTest {
    public static void main(String[] av) {
        FileIO f = new FileIO();
		try {
			f.copyFile("FileIO.java", "FileIO.bak");
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
    }
}
