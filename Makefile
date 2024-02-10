# Makefile to make a "docset" for the program known as:
#	Dash (https://kapeli.com/dash) on macOS;
#	Zeal (https://zealdocs.org/) everywhere else.
# Kapeli.com hosts the 200+ public doc sets; thanks Kapeli!

LOCAL_DOCSETS_DIR=$$HOME/.local/share/Zeal/Zeal/docsets/

install-zealdocs:	zealdocs
	rm -r ${LOCAL_DOCSETS_DIR}
	cp -r DarwinSys-API.docset/ ${LOCAL_DOCSETS_DIR}

zealdocs:
	javadocset DarwinSys-API target/site/apidocs
	cp src/main/resources/Face32x32.png DarwinSys-API.docset/icon.png

javadoc:
	mvn javadoc:javadoc

install:
	mvn clean package
	sh -x ./scripts/inst
