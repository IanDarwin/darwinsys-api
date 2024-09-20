package com.darwinsys.swingui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.function.Consumer;

public class ColorPanel extends JPanel {
    protected Consumer<Color> setter;

    public final static Color[] COLORS = {
            Color.BLACK, Color.WHITE, Color.RED, Color.BLUE,
            Color.GREEN, Color.ORANGE, Color.MAGENTA,
    };

    public ColorPanel(Consumer<Color> setter) {
        this.setter = setter;
        setLayout(new GridLayout(2,4));
        for (Color c : COLORS) {
            var b = new JButton(){
                @Override
                protected void paintComponent(Graphics g) {
                    var d = getSize();
                    g.fillRect(0, 0, d.width, d.height);
                }
            };
            b.addActionListener(evt -> setter.accept(c));
            b.setSize(new Dimension(16,16));
            b.setForeground(c);
            b.setBackground(c);
            b.setOpaque(true);
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

    public static void main(String[] args) {
        JFrame jf = new JFrame("Dummy");
        jf.add(new ColorPanel(System.out::println));
        jf.pack();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
