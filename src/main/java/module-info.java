// tag::main[]
module com.darwinsys.api {

	requires transitive java.desktop;
	requires java.net.http;
	requires transitive java.prefs;
	requires transitive java.sql;
	requires java.sql.rowset;
	requires jakarta.mail;
	requires java.xml;

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
	opens   com.darwinsys.io;
	exports com.darwinsys.lang;
	exports com.darwinsys.locks;
	provides com.darwinsys.locks.PessimisticLockManager
		with com.darwinsys.locks.PessimisticLockManagerImpl;
	exports com.darwinsys.mail;
	exports com.darwinsys.net;
	exports com.darwinsys.notepad;
	exports com.darwinsys.numbers;
	opens   com.darwinsys.numbers;
	exports com.darwinsys.preso;
	exports com.darwinsys.reflection;
	exports com.darwinsys.regex;
	exports com.darwinsys.security;
	exports com.darwinsys.sql;
	exports com.darwinsys.swingui;
	opens   com.darwinsys.swingui;
	exports com.darwinsys.tel;
	exports com.darwinsys.testdata;
	exports com.darwinsys.tools;
	exports com.darwinsys.unix;
	exports com.darwinsys.util;
	exports com.darwinsys.xml;
}
// end::main[]
