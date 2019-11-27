// This copy of module-info is ONLY for testing - DO NOT DEPLOY
// It must be IDentical to the one in src/main/java but for the
// addition of JUnit
module com.darwinsys.api {

	requires junit;
	
	requires transitive java.desktop;
	requires java.net.http;
	requires java.persistence;
	requires java.prefs;
	requires java.sql;
	requires java.sql.rowset;
	requires jakarta.mail;

	exports com.darwinsys.calendar;
	exports com.darwinsys.csv;
	exports com.darwinsys.database;
	exports com.darwinsys.diff;
	exports com.darwinsys.formatting;
	exports com.darwinsys.genericui;
	exports com.darwinsys.geo;
	exports com.darwinsys.graphics;
	exports com.darwinsys.html;
	exports com.darwinsys.io;
	exports com.darwinsys.lang;
	exports com.darwinsys.locks;
	provides com.darwinsys.locks.PessimisticLockManager
		with com.darwinsys.locks.PessimisticLockManagerImpl;
	exports com.darwinsys.mail;
	exports com.darwinsys.model;
	opens com.darwinsys.model;
	exports com.darwinsys.net;
	exports com.darwinsys.notepad;
	exports com.darwinsys.preso;
	exports com.darwinsys.reflection;
	exports com.darwinsys.regex;
	exports com.darwinsys.security;
	exports com.darwinsys.sql;
	exports com.darwinsys.swingui;
	exports com.darwinsys.tel;
	exports com.darwinsys.testdata;
	exports com.darwinsys.tools;
	exports com.darwinsys.unix;
	exports com.darwinsys.util;
	exports com.darwinsys.xml;
}
