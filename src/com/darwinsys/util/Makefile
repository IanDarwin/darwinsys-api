# Makefile for com.darwinsys.util package
# $Id$

#JAVAC=		javac
JAVAC=		jikes +E

SRC=		Debug.java FileProperties.java GetOpt.java

all:		jar

jar:	$(SRC)
		$(JAVAC) -d . $(SRC)
		jar cvf darwinsys-util.jar com $(SRC) Makefile

src-jar:
		jar cvf darwinsys-util-src.jar $(SRC) Makefile
