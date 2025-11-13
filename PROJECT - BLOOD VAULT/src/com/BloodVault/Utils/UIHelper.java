package com.BloodVault.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Small UI helper: colors, fonts and a RoundedPanel (card style).
 */
public class UIHelper {
    public static final Color PRIMARY = new Color(178, 34, 34);       // main red
    public static final Color ACCENT = new Color(80, 180, 80);        // green accent
    public static final Color BACKGROUND = new Color(250, 250, 250);  // page bg
    public static final Color CARD = Color.white;
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BTN = new Font("Segoe UI", Font.BOLD, 14);

    public static JButton createPrimaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFont(BTN);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return b;
    }

    public static JButton createAccentButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFont(BTN);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return b;
    }

    // Rounded card panel: use by passing a layout and corner radius
    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;

        public RoundedPanel(LayoutManager layout, int radius, Color bg) {
            super(layout);
            this.radius = radius;
            this.backgroundColor = bg == null ? CARD : bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
