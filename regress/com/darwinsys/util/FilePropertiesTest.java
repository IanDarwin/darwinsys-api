package regress;

import com.darwinsys.util.FileProperties;

import java.io.*;
import java.util.*;

public class FilePropertiesTest {
    public static void main(String[] av) throws IOException {
		System.out.println("This properties should be empty:");
		Properties p = new FileProperties("no such file");
		p.list(System.out);
		p.setProperty("foo", "bar");
		((FileProperties)p).save();

		p = new FileProperties("no such file");
		System.out.println("This properties should be have foo=bar:");
		p.list(System.out);

		new File("no such file").delete();
	}
}
