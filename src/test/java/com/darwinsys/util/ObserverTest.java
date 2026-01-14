package com.darwinsys.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObserverTest {

	boolean seen = false;
	final String changedData = "In principio, erant nox";
	String seenData = null;

	@Test
	public void testOne() {
		Observer x = new Observer() {
			public void notifyChanged(Observable obs,Object newData) {
				seen = true;
				seenData = (String)newData;
			}
		};
		Observable obs = new Observable();
		obs.addObserver(x);
		obs.notifyChanged(changedData);
		assertTrue(seen);
		assertSame(changedData, seenData);
	}
}
