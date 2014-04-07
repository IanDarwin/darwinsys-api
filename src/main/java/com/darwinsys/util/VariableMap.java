package com.darwinsys.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A Map with Variable substitution, using ${key} syntax.
 * VariableMap both holds variables and performs substitution
 * on a string using all held variables matched by ${varName}.
 * Supports java.beans PropertyChange notification so other parts
 * of an application can be notified of any variable values that change.
 */
public class VariableMap extends HashMap<String, String> {

	private static final long serialVersionUID = 7830637441984501436L;

	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
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

	public void setIntVar(String key, int value) {
		put(key, Integer.toString(value));
	}
	public String getVar(String key, String defaultValue) {
		return get(key) != null ? get(key) : defaultValue;
	}
	
	public int getIntVar(String key) {
		final String stringVal = get(key);
		if (stringVal == null) {
			throw new IllegalArgumentException("Key " + key + " not found");
		}
		return Integer.parseInt(stringVal);
	}
	
	public int getIntVar(String key, int defaultValue) {
		final String stringVal = get(key);
		if (stringVal == null) {
			return defaultValue;
		} else {
			return Integer.parseInt(stringVal);
		}
	}
	
	/** The pattern for matching variables in the command: ${letters}.
	 * But we have to capture the name part (the letters) for re-use.
	 */
	private final Pattern varsPatt = Pattern.compile("\\$\\{(\\w+)\\}");
	
	/** Substitute all variables in a given string; does not change
	 * the contents of this Map in any way.
	 * Note that the input string MUST NOT contain \ or $ characters (except
	 * in the variable substitution context) unless you
	 * really understand what you're doing.
	 * <p>
	 * Example input: <pre>lookFor(${USER})</pre>
	 * If USER variable in map contains PIE, the result will be
	 * <pre>lookFor(PIE)</pre>.
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
	
	// PROPERTY CHANGE SUPPORT

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener liszt) {
		listeners.remove(liszt);
	}

	/** Minimal property change notification
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public String put(String key, String value) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this, key, get(key), value);
		for (PropertyChangeListener list : listeners) {
			list.propertyChange(evt);
		}
		return super.put(key, value);
	}
}
