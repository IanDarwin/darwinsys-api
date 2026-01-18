package com.darwinsys.swingui;

import javax.swing.*;
import java.awt.Color;
import java.util.function.Consumer;

public class ColorPanelDemo {
    // Simple demo harness
    public static void main(String[] args) {
        var jf = new JFrame();
        Consumer<Color> handler = c -> System.out.println("c = " + c);
        jf.add(new ColorPanel(handler));
        jf.pack();
        jf.setVisible(true);
    }
}
