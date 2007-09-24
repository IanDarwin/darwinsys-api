package com.darwinsys.swingui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage the "enabled" property on a group of Components,
 * such that only one will be enabled.
 * May malfunction if you directly invoke setEnabled() on any component.
 * NOT THREADSAFE; use single-threadedly.
 */
public class ExclusiveEnableGroup implements java.io.Serializable {

	private List<Component> groupComps = new ArrayList<Component>();

	/** Add the Component to the list of managed Components */
	public void add(Component c) {
		c.setEnabled(false);
		groupComps.add(c);
	}

	/** Enable the given Component; this is the only way you
	 * should control enabled properties on the components.
	 */
	public void enable(Component targetComp) {
		if (!groupComps.contains(targetComp)) {
			throw new IllegalArgumentException(targetComp + " not in list");
		}
		// Walk the list once, disabling all but the target,
		// and enabling the target.
		for (Component c : groupComps) {
			targetComp.setEnabled(c == targetComp);
		}
	}
}
