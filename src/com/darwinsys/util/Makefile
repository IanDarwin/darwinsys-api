# Makefile for com.darwinsys.util package
# $Id$

#JAVAC=		javac
JAVAC=		jikes +E

FMT_SRC=	QuickTimeFormat.java StringFormat.java
GUI_SRC=	ArrayListTableModel.java ArrayListTablePanel.java \
			UtilGUI.java WindowCloser.java 
IO_SRC=		FileIO.java FileProperties.java
STR_SRC=	StrSubs.java 
SRC=		$(FMT_SRC) $(GUI_SRC) $(IO_SRC) $(STR_SRC) \
			Debug.java GetOpt.java MutableInteger.java

JAR=		com-darwinsys-util.jar

all:		$(JAR)

$(JAR):	$(SRC)
		$(JAVAC) -d . $(SRC)
		jar -cfm $(JAR) manifest.stub com

src-jar:
		jar cvf darwinsys-util-src.jar $(SRC) Makefile

install:	$(JAR)
		cp $(JAR) $$HOME/classes/

# Check to make sure that everything but tests is in package
check:
		@for f in $(SRC); do grep '^package.com.darwinsys.util;$$' $$f >/dev/null || echo $$f NOT IN PACKAGE; done
		@ls $(SRC) > /tmp/t1
		@ls *.java | grep -v Test.java > /tmp/t2
		@diff /tmp/t[12] || echo "Above files not being built!"

clean:
		-rm -rf $(JAR) com *.class
