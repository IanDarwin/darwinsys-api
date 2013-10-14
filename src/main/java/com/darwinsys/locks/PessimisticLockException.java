package com.darwinsys.locks;

public class PessimisticLockException extends RuntimeException {

	private static final long serialVersionUID = -6060487116987872592L;

	public PessimisticLockException(String string) {
		super(string);
	}

}
