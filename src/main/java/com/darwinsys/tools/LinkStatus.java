package com.darwinsys.tools;

// record LinkStatus(boolean ok, String message) {}

class LinkStatus {
	boolean ok;
	String message;
	
	public LinkStatus(boolean status, String message) {
		this.ok = status;
		this.message = message;
	}
	
	@Override
	public String toString() {
		return ok + " " + message;
	}
}
