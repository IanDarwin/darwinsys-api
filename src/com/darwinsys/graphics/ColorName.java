import java.awt.Color;

/** A simple class for looking up Java AWT Color Names; I got tired
 * of including this code in every Applet that needed it!
 * (yes, this IS a hint to JavaSoft.).
 * @author	Ian Darwin, delinted by Bill Heinze
 */
public class ColorName {
	/** The list of known color names and their corresponding colors */
	protected static ColorNameMap map[] = {
		new ColorNameMap("white", Color.white),
		new ColorNameMap("yellow", Color.yellow),
		new ColorNameMap("orange", Color.orange),
		new ColorNameMap("red", Color.red),
		new ColorNameMap("blue", Color.blue),
		new ColorNameMap("green", Color.green),
		new ColorNameMap("pink", Color.pink),
		new ColorNameMap("cyan", Color.cyan),
		new ColorNameMap("magenta", Color.magenta),
		new ColorNameMap("gray", Color.gray),
		new ColorNameMap("lightGray", Color.lightGray),
		new ColorNameMap("darkGray", Color.darkGray),
		new ColorNameMap("black", Color.black),
	};

	/** Lookup a given string
	 * @returns	The java.awt.Color corresponding, or null.
	 */
	public static Color lookup(String c) {
		if (c == null)
			return null;
		if (c.charAt(0) == '#')			// hex encoding
			return Color.decode(c);
		for (int i=0; i<map.length; i++)
			if (c.equalsIgnoreCase(map[i].color))
				return map[i].jColor;
		return null;
	}

	/** Just a test */
	public static void main(String[] a) {
		test("WhItE");
		test("Ucky Purple Pink Spots");
		test(null);
		test("#c0d0e0");
	}
	public static void test(String s) {
		System.out.println('"' + s + '"' + " returns " + ColorName.lookup(s));
	}
}

/** A class to map from color names to java.awt.Color */
class ColorNameMap {
	ColorNameMap(String c, Color jc) {
		color = c;
		jColor = jc;
	}
	String color;
	Color  jColor;
}
