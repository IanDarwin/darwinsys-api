package com.darwinsys.testdata;

public interface Generator {
	public Object nextValue();
	public Object[] nextValues(int howMany);
}
