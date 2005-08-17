# Makefile for the OPTIONAL Java Native Methods parts of the darwinsys API
# Ian Darwin, http://www.darwinsys.com

# $Id$

BUILD=	../build

#CFLAGS_FOR_SO = -G # Solaris
CFLAGS_FOR_SO = -shared
CSRCS		= nextfd.c

# JAVA_HOME should be set in the environment
INCLUDES	= -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/`uname|tr A-Z a-z`

all:		test

test:		libdarwinsys.so $(BUILD)/com/darwinsys/io/NextFD.class
		@echo
		@echo "Here we test the Java code that calls C code."
		@echo
		LD_LIBRARY_PATH=`pwd`:. java -classpath $(BUILD) com/darwinsys/io/NextFD

com_darwinsys_io_nextfd.h:	$(BUILD)/com/darwinsys/io/NextFD.class
		javah -classpath $(BUILD) -jni com.darwinsys.io.NextFD

nextfd.o::	com_darwinsys_io_nextfd.h

libdarwinsys.so:	$(CSRCS) com_darwinsys_io_nextfd.h
	$(CC) $(INCLUDES) $(CFLAGS_FOR_SO) $(CSRCS) -o libdarwinsys.so
	@echo Remember to install `pwd`/libdarwinsys.so* on your LD_LIBRARY_PATH

clean:
	rm -f core *.o *.so *.h
