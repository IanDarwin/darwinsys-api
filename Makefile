install:	zealdocs
	cp -r DarwinSys-API.docset/ $$HOME/.local/share/Zeal/Zeal/docsets/

zealdocs:
	javadocset DarwinSys-API target/site/apidocs

javadoc:
	mvn javadoc:javadoc
