package com.darwinsys.io;

public abstract class CrawlerCallback implements Thread.UncaughtExceptionHandler {
	public abstract void handleException(Throwable th);
	public void uncaughtException(Thread t, Throwable e){
		handleException(e);
	}
}
