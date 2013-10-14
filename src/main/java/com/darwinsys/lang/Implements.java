package com.darwinsys.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the same as the Override annotation but requires
 * that the method it applies to implements a method in one
 * of the class' interfaces. The standard compilers do not
 * recognize this but we hope to implement it in FindBugs
 * (which is why the RetentionPolicy is set to CLASS).
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Implements {

}
