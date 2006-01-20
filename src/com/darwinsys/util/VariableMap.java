package com.darwinsys.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A HashMap with Variable substitution, using ${key} syntax.
 * VariableMap both holds variables and performs substitution
 * on a string using all held variables matched by ${varName}.
 */
public class VariableMap extends HashMap<String, String> {

	public VariableMap() {
		super();
	}
	
	public VariableMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public VariableMap(int initialCapacity) {
		super(initialCapacity);
	}

	public VariableMap(Map<String, String> m) {
		super(m);
	}
	
	public void setVar(String key, String value) {
		put(key, value);
	}

	public String getVar(String key) {
		return get(key);
	}

	public int getIntVar(String key) {
		return Integer.parseInt(get(key));
	}
	
	/** The pattern for matching variables in the command: ${letters}.
	 * But we have to capture the name part (the letters) for re-use.
	 */
	private final Pattern varsPatt = Pattern.compile("\\$\\{(\\w+)\\}");
	
	/** Substitute all variables in a given string
	 * @param inString
	 * @return
	 */
	public String substVars(String inString) {
		StringBuffer sb = new StringBuffer();
		// System.out.printf("VariableMap.substVars(): patt %s%n", varsPatt);
		Matcher m = varsPatt.matcher(inString);
		
		while (m.find()) {
			String varName = m.group(1);
			System.out.println("varName = " + varName);
			String replText = get(varName);
			if (replText != null) {
				m.appendReplacement(sb, replText);
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
