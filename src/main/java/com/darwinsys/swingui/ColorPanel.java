package com.darwinsys.swingui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ColorPanel extends JPanel {
    protected Consumer<Color> setter;

    public final static Color[] COLORS = {
            Color.BLACK, Color.WHITE, Color.RED, Color.BLUE,
            Color.GREEN, Color.ORANGE, Color.MAGENTA,
    };
    
    ColorPanel(Consumer<Color> setter) {
        this.setter = setter;
        setLayout(new GridLayout(2,4));
        for (Color c : COLORS) {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(16,16));
            b.setBackground(c);
            b.addActionListener(evt->setter.accept(c));
            add(b);
        }
        JButton b = new JButton("...");
        b.addActionListener(chooser);
        add(b);
    }

    ActionListener chooser = e -> {
        Color ch = JColorChooser.showDialog(
                null,             // parent
                "Pick a Drawing Color",
                getBackground()// title
        );
        if (ch != null) {
            setter.accept(ch);
        }
    };

    // Simple demo harness
    public static void main(String[] args) {
        var jf = new JFrame();
        Consumer<Color> handler = c -> System.out.println("c = " + c);
        jf.add(new ColorPanel(handler));
        jf.pack();
        jf.setVisible(true);
    }
}
