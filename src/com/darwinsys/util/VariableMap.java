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
		final String stringVal = get(key);
		if (stringVal == null) {
			throw new IllegalArgumentException("Key " + key + " not found");
		}
		return Integer.parseInt(stringVal);
	}
	
	/** The pattern for matching variables in the command: ${letters}.
	 * But we have to capture the name part (the letters) for re-use.
	 */
	private final Pattern varsPatt = Pattern.compile("\\$\\{(\\w+)\\}");
	
	/** Substitute all variables in a given string; note that the
	 * input string MUST NOT contain \ or $ characters (except of course
	 * in the variable substitution context) unless you
	 * really understand what you're doing.
	 * <p>
	 * Example input: <kbd>lookFor(${USER})</kbd>
	 * If USER variable in map contains PIE, the result will be
	 * <kbd>lookFor(PIE)</kbd>.
	 * @param inString
	 * @return The string after substitution
	 */
	public String substVars(String inString) {
		StringBuffer sb = new StringBuffer();
		// System.out.printf("VariableMap.substVars(%s)%n", inString);
		Matcher m = varsPatt.matcher(inString);
		
		while (m.find()) {
			String varName = m.group(1);
			String replText = get(varName);
			// System.err.printf("VariableMap.substVars: %s->%s%n", varName, replText);
			if (replText != null) {
				try {
				m.appendReplacement(sb, replText);
				} catch (IllegalArgumentException e) {
					System.err.printf("VariableMap.SubstVars: BLOWN BY %s%nInputString %s", replText, inString);
					sb.append("???");
				}
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
