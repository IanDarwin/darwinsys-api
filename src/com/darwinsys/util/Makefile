# Makefile for com.darwinsys.util package
# $Id$

#JAVAC=		javac
JAVAC=		jikes +E

SRC=		Debug.java FileIO.java FileProperties.java \
			GetOpt.java MutableInteger.java StrSubs.java \
			UtilGUI.java WindowCloser.java 

JAR=		darwinsys-util.jar

all:		$(JAR)

$(JAR):	$(SRC)
		$(JAVAC) -d . $(SRC)
		jar cvf $(JAR) com

src-jar:
		jar cvf darwinsys-util-src.jar $(SRC) Makefile

install:	$(JAR)
		cp $(JAR) $$HOME/classes/

clean:
		-rm -rf $(JAR) com
