// BEGIN main
package com.darwinsys.lang;

import java.io.File;

/** Some things that are System Dependent.
 * All methods are static.
 * @author Ian Darwin
 */
public class SysDep {

    final static String UNIX_NULL_DEV = "/dev/null";
    final static String WINDOWS_NULL_DEV = "NUL:";
    final static String FAKE_NULL_DEV = "jnk";
    
    /** Return the name of the "Null Device" on platforms which support it,
     * or "jnk" (to create an obviously well-named temp file) otherwise.
     */
    public static String getDevNull() {

        if (new File(UNIX_NULL_DEV).exists()) {     // <1>
            return UNIX_NULL_DEV;
        }

        String sys = System.getProperty("os.name"); // <2>
        if (sys==null) {                            // <3>
            return FAKE_NULL_DEV;
        }
        if (sys.startsWith("Windows")) {            // <4>
            return WINDOWS_NULL_DEV;
        }
        return FAKE_NULL_DEV;                       // <5>
    }
}
// END main
