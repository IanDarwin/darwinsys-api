package com.darwinsys.util;

public interface Observer {
	void notifyChanged(Observable source, Object newData);
}
