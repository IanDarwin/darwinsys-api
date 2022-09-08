import java.io.*;

// Rearrange Pictures to strict date-order filing. 
// USE WITH CAUTION! USE AT OWN RISK! Failures may lose pictures - take a backup first
// Looks for several timestamp formats in exiv2 output
// Image timestamp : 2020:08:06 09:30:08
public class StorePixByDate {
	public static void main(String[] args) {
	for (String f : args) {
		System.out.println("Processing: " + f);
		File file = new File(f);
		if (!file.isFile() || !file.canRead()) {
			System.out.println("Missing or unreadable: " + file);
			continue;
		}
		BufferedReader is = null;
		Process p = null;
		try {
			p = new ProcessBuilder("exiv2", "-g", "Date", f)
				.directory(new File("/home/ian/Pictures"))
				.start();
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		boolean found=false;
		while ((line = is.readLine()) != null) {
		if (line.startsWith("Image timestamp") ||
			line.startsWith("Exif.Image.DateTime") ||
			line.startsWith("Exif.GPSInfo.GPSDateStamp")) {
				if (!found) {
				var tokens = line.split(" +");
				String stamp = tokens[3].replaceAll(":","/");
				new File(stamp).mkdirs();
				var base = f.replaceAll(".*/", "");
				File dest = new File(stamp, base);
				if (dest.exists()) {
					System.out.println("Duplicate, not moving " + f + " to " + dest);
					break;	// out of while readline loop
				}
				file.renameTo(dest);
				System.out.println("File " + file + " moved to " + dest);
				found = true;
				break; // out of while readLine loop
			}
			}
		}
		if (!found) {
			System.out.println("storepixbydate: " + f + ": no image timestamp found, not moving.");
		}
	} catch (IOException ex) {
	System.out.println("I/O error processing " + f);
	} finally {
		if (is != null) {
			try {
				is.close();
			} catch (IOException ex) {
				// don't care
			}
		}
		if (p != null && p.isAlive()) {
			p.destroy();	// so we don't run out of process table entries!
		}
	}
	}
	}
}
