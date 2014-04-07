// BEGIN main
package com.darwinsys.swingui;

import java.awt.Frame;
import java.awt.Label;
import com.darwinsys.swingui.WindowCloser;

/* Example of closing a Window. */
public class WindowCloserDemo {

    /* Main method */
    public static void main(String[] argv) {
        Frame f = new Frame("Close Me");
        f.add(new Label("Try Titlebar Close", Label.CENTER));
        f.setSize(100, 100);
        f.setVisible(true);
        f.addWindowListener(new WindowCloser(f, true));
    }
}
// END main
