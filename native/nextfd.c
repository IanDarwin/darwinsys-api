#include <stdio.h>
#include <fcntl.h>

#include "jni.h"
#include "com_darwinsys_io_NextFD.h"

/**
 * Native method to return the next available file descriptor.
 * Implementation is pretty trivial, but it's a very useful method.
 */
JNIEXPORT jint JNICALL
Java_com_darwinsys_io_NextFD_nextfd(JNIEnv *env, jclass clazz) {
	int fd = open(".", O_RDONLY);
	(void) close(fd);
	return (jint)fd;
}
