package com.darwinsys.testdata;

public interface Generator<T> {
	public T nextValue();
	public T[] nextValues(int howMany);
}
