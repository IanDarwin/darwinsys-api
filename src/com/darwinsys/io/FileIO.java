import java.io.*;

/**
 * Some simple file I-O primitives reimplemented in Java
 */
public class FileIO {

    public static void main(String av[]) {
        FileIO f = new FileIO();
		try {
			f.copyFile("FileIO.java", "FileIO.bak");
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
    }

    /** Copy a file from one filename to another */
    public void copyFile(String inName, String outName) throws
			FileNotFoundException, IOException {
		BufferedInputStream is = 
			new BufferedInputStream(new FileInputStream(inName));
		BufferedOutputStream os = 
			new BufferedOutputStream(new FileOutputStream(outName));
		long count = 0;		// the byte count
		int b;				// the byte read from the file
		while ((b = is.read()) != -1) {
			os.write(b);
		}
		is.close();
		os.close();
    }
}
