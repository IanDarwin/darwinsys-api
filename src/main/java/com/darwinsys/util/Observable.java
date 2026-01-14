package com.darwinsys.util;

import java.util.ArrayList;
import java.util.List;

public class Observable {
	private List<Observer> watchers = new ArrayList<>();

	void addObserver(Observer obs) {
		watchers.add(obs);
	}

	void removeObserver(Observer obs) {
		watchers.remove(obs);
	}

	void notifyChanged(Object changedData) {
		watchers.forEach(w -> w.notifyChanged(this, changedData));
	}
}
