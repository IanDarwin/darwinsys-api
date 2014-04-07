package com.darwinsys.graphics;

/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2006.
 * All rights reserved. Software written by Ian F. Darwin and others.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the author nor the names of any
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java
 * language and environment is gratefully acknowledged.
 *
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.awt.Color;

/** A simple class for looking up Java AWT Color Names; I got tired
 * of including this code in every program that needed it!
 * (yes, this IS a hint to JavaSoft.).
 * <br>
 * TODO: Rewrite XColor from javasrc as a subclass of this.
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

	public static final Color FALLBACK_COLOR = Color.GRAY;
	
	/** The list of known color names and their corresponding colors */
	final static ColorNameMap map[] = {
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
	 * @return	The java.awt.Color corresponding, or null.
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
