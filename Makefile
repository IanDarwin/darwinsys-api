# Makefile to make a "docset" for the program known as:
#	Dash on macOS;
#	Zeal everywhere else.

install-zealdocs:	zealdocs
	cp -r DarwinSys-API.docset/ $$HOME/.local/share/Zeal/Zeal/docsets/

zealdocs:
	javadocset DarwinSys-API target/site/apidocs
	cp src/main/resources/Face32x32.png DarwinSys-API.docset/icon.png

javadoc:
	mvn javadoc:javadoc
