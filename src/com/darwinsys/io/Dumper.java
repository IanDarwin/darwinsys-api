import java.io.*;

/**
 * Read a file and print, using BufferedReader and System.out
 */
public class DumpFile {

    public static void main(String av[]) {
        DumpFile c = new DumpFile();
        switch(av.length) {
            case 0: c.process(new DataInputStream(System.in));
				break;
            default:
				for (int i=0; i<av.length; i++)
                    try {
                        c.process(new DataInputStream(new FileInputStream(av[i])));
                    } catch (FileNotFoundException e) {
                        System.err.println(e);
                    }
        }
    }

    /** print one file, given an open BufferedReader */
    public void process(DataInputStream is) {
        try {
            int b = 0;
			int bnum = 0;

            while ((b=is.read()) != -1) {
                System.out.print(b + " ");
				if (++bnum%10 == 0)
					System.out.print("\n");
            }
			System.out.print("\n");
            is.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
