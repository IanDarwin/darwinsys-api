# Makefile for com.darwinsys.util package
# $Id$

#JAVAC=		javac
JAVAC=		jikes +E

FMT_SRC=	QuickTimeFormat.java StringFormat.java
GUI_SRC=	ArrayListTableModel.java ArrayListTablePanel.java \
			I18N.java SimpleHelp.java UtilGUI.java WindowCloser.java 
IO_SRC=		FileIO.java FileProperties.java Tag.java ExecAndPrint.java
STR_SRC=	StrSubs.java 
MISC_SRC=	Debug.java GetOpt.java Mailer.java MutableInteger.java
SRC=		$(FMT_SRC) $(GUI_SRC) $(IO_SRC) $(STR_SRC) $(MISC_SRC)

JAR=		com-darwinsys-util.jar

all build:	$(JAR)

# The explicit dependancy upon "docs" is commented out so I don't have
# to wait for javadoc whenever I tweak a file. You should probably
# remove the '#' on the following line:
$(JAR):	$(SRC) # docs
		$(JAVAC) -d . $(SRC)
		jar -cfm $(JAR) manifest.stub com docs

doc docs:	docs/index.html
docs/index.html: $(SRC)
		-@mkdir docs 2>/dev/null	# ensure dir exists, ok if already there
		javadoc -d docs *.java

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
		-rm -rf $(JAR) *.class com docs
