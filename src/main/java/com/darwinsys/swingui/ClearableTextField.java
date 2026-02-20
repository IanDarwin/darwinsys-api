package com.darwinsys.swingui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

/**
 * A JTextField with a clear (X) button that appears when the field has content,
 * and optional placeholder text when the field does not have content.
 * Usage:
 *   ClearableTextField field = new ClearableTextField("Search...");
 *   panel.add(field);
 */
public class ClearableTextField extends JPanel {

	private static final int DEFAULT_COLUMNS = 20;
    private final JTextField textField;
    private final ClearButton clearButton;

	/** Construct a ClearableTextField with all default options */
    public ClearableTextField() {
        this("", DEFAULT_COLUMNS);
    }

	/** Construct a ClearableTextField with a given placeholder text */
    public ClearableTextField(String placeHolder) {
        this(placeHolder, DEFAULT_COLUMNS);
    }

	/** Construct a ClearableTextField with a given placeholder text  and number of text columns */
    public ClearableTextField(String placeholder, int columns) {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1, true),
            new EmptyBorder(0, 2, 0, 2)
        ));

        // The actual Text field 
        textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw placeholder text when empty
                if (getText().isEmpty() && !placeholder.isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(180, 180, 190));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, 0, y);
                    g2.dispose();
                }
            }
        };
        if (columns != 0) {
            textField.setColumns(columns);
        }
        this.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { textField.requestFocusInWindow(); }
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { textField.repaint(); }
            public void focusLost(FocusEvent e)   { textField.repaint(); }
        });

        // The Clear button, which only shows if there's text
        clearButton = new ClearButton();
        clearButton.setVisible(false);

        // Show/hide the clear button as the user types
        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { syncButton(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { syncButton(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { syncButton(); }
            private void syncButton() {
                clearButton.setVisible(!textField.getText().isEmpty());
                revalidate();
                repaint();
            }
        });

        // Clear action
        clearButton.addActionListener(e -> {
            textField.setText("");
            textField.requestFocusInWindow();
        });

        add(textField, BorderLayout.CENTER);
        add(clearButton, BorderLayout.EAST);
    }

    // Delegate helpers

    /** Returns the underlying JTextField for advanced configuration. */
    public JTextField getTextField() { return textField; }
    // Delegation methods to underlying JTextField
    public String getText() { return textField.getText(); }
    public void   setText(String t) { textField.setText(t); }
    public int getColumns() { return textField.getColumns(); }
    public void setColumns(int n) { textField.setColumns(n);}
    public void addActionListener(ActionListener l) { textField.addActionListener(l); }

    @Override public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        textField.setPreferredSize(null); // let layout handle it
    }

    // Rounded-panel override

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 210));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.dispose();
    }

    // Inner clear button

    private static class ClearButton extends JButton {

        private boolean hovered = false;

        ClearButton() {
            setPreferredSize(new Dimension(28, 28));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int size   = Math.min(getWidth(), getHeight());
            int circle = 16;
            int cx     = (size - circle) / 2;
            int cy     = (getHeight() - circle) / 2;

            // Background circle (visible on hover)
            if (hovered) {
                g2.setColor(new Color(220, 220, 228));
            } else {
                g2.setColor(new Color(200, 200, 210));
            }
            g2.fill(new Ellipse2D.Float(cx, cy, circle, circle));

            // X mark
            int pad = 5;
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(cx + pad, cy + pad, cx + circle - pad, cy + circle - pad);
            g2.drawLine(cx + circle - pad, cy + pad, cx + pad, cy + circle - pad);

            g2.dispose();
        }
    }
}
