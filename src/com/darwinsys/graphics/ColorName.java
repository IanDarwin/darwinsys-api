package com.darwinsys.graphics;

import java.awt.Color;

/** A simple class for looking up Java AWT Color Names; I got tired
 * of including this code in every program that needed it!
 * (yes, this IS a hint to JavaSoft.).
 * @author	Ian Darwin, delinted by Bill Heinze
 */
public class ColorName {
	
	/** A class to map from color names to java.awt.Color */
	static class ColorNameMap {
		ColorNameMap(String c, Color jc) {
			colorName = c;
			jColor = jc;
		}
		String colorName;
		Color  jColor;
	}
	
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
	public static Color getColor(String name) {
		if (name == null)
			return null;
		if (name.charAt(0) == '#')			// hex encoding
			return Color.decode(name);
		for (ColorNameMap col : map) {
			if (name.equalsIgnoreCase(col.colorName))
				return col.jColor;
		}
		return null;
	}


}
