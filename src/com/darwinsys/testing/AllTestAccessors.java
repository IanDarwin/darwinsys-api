package com.darwinsys.testing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.darwinsys.testing.TestAccessors;

/**
 * Abstract class to take an array of classes
 * (which MUST be set in a subclass static initializer!)
 * and run the TestAccessors test on each of them.
 */
@RunWith(value=Parameterized.class)
public abstract class AllTestAccessors {

	private Class<?> clazz;
	
	/** The subclass MUST use a static method
	 * to initialize this!
	 */
	static Class<?>[] classes;
	
    /** Constructor */
	public AllTestAccessors(Object clazz) {
		this.clazz = (Class<?>) clazz;
	}

	@Test
	public void testOneClass() throws Exception {
		TestAccessors.process(clazz);
	}

    /** This method provides data to the constructor for use in tests */
    @Parameters
    public static List<Class<?>[]> data() {
    	List<Class<?>[]> results = 
    		new ArrayList<Class<?>[]>(classes.length);
    	for (Class<?> cl : classes) {
    		results.add(new Class<?>[]{cl});
    	}
		return results;
	}
	
}

