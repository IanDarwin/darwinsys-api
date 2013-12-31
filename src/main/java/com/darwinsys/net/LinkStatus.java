package com.darwinsys.net;

class LinkStatus {
	boolean ok;
	String message;
	
	public LinkStatus(boolean status, String message) {
		this.ok = status;
		this.message = message;
	}
}