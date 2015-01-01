package com.darwinsys.io;

/**
 * The callback used by the Crawler.
 */
public abstract class CrawlerCallback implements Thread.UncaughtExceptionHandler {
	public abstract void handleException(Throwable th);
	public void uncaughtException(Thread t, Throwable e){
		handleException(e);
	}
}
