package com.BloodVault.donor;

import com.BloodVault.MainApp;
import com.BloodVault.ui.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Donor dashboard with headings, bold table header, compact health panel,
 * and a manual click-through awareness carousel.
 */
public class DonorDashboard extends JFrame {

    private JPanel awarenessCards;
    private CardLayout awarenessLayout;

    public DonorDashboard() {
        setTitle("Donor Dashboard - Blood Vault");
        setSize(880, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIHelper.BACKGROUND);
        setLayout(new BorderLayout(8,8));

        // Header with Logout (top-right)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Donor Dashboard", SwingConstants.LEFT);
        title.setFont(UIHelper.TITLE);
        title.setForeground(UIHelper.PRIMARY);
        JButton logout = UIHelper.createPrimaryButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            MainApp.main(new String[0]);
        });
        header.add(title, BorderLayout.LINE_START);
        header.add(logout, BorderLayout.LINE_END);
        add(header, BorderLayout.PAGE_START);

        // LEFT: Donation History (label + table)
        JPanel left = new JPanel(new BorderLayout(6,6));
        JLabel histLabel = new JLabel("Donation History");
        histLabel.setFont(UIHelper.TITLE.deriveFont(Font.BOLD, 16f));
        histLabel.setForeground(UIHelper.PRIMARY);
        left.add(histLabel, BorderLayout.PAGE_START);

        String[] cols = {"Date", "Hospital", "Blood Group", "Units", "Status"};
        Object[][] data = {
                {"2025-01-12", "City Hospital", "A+", 1, "Used"},
                {"2025-03-20", "Camp East", "A+", 1, "Available"}
        };
        JTable table = new JTable(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        table.setRowHeight(24);
        // Bold, centered header
        JTableHeader th = table.getTableHeader();
        th.setFont(UIHelper.BTN.deriveFont(Font.BOLD, 14f));
        DefaultTableCellRenderer hdr = (DefaultTableCellRenderer) th.getDefaultRenderer();
        hdr.setHorizontalAlignment(SwingConstants.CENTER);
        // Optional: emphasize primary color in header foreground/background
        th.setForeground(Color.WHITE);
        th.setBackground(UIHelper.PRIMARY);
        left.add(new JScrollPane(table), BorderLayout.CENTER);

        // RIGHT: Health Details (label + compact grid)
        JPanel right = new JPanel(new BorderLayout(6,6));
        JLabel healthLabel = new JLabel("Health Details");
        healthLabel.setFont(UIHelper.TITLE.deriveFont(Font.BOLD, 16f));
        healthLabel.setForeground(UIHelper.PRIMARY);
        right.add(healthLabel, BorderLayout.PAGE_START);

        JPanel healthGrid = new JPanel(new GridLayout(5, 2, 6, 6));
        healthGrid.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        healthGrid.add(new JLabel("Last Donation:"));
        healthGrid.add(new JLabel("2025-03-20"));
        healthGrid.add(new JLabel("Blood Count:"));
        healthGrid.add(new JLabel("13.2 g/dL"));
        healthGrid.add(new JLabel("BP:"));
        healthGrid.add(new JLabel("120/80"));
        healthGrid.add(new JLabel("Height / Weight:"));
        healthGrid.add(new JLabel("170 cm / 65 kg"));
        healthGrid.add(new JLabel("Heart Rate:"));
        healthGrid.add(new JLabel("74 bpm"));
        right.add(healthGrid, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.62);
        split.setDividerLocation(540);
        add(split, BorderLayout.CENTER);

        // BOTTOM: Awareness (manual carousel with Prev/Next)
        JPanel awareness = new JPanel(new BorderLayout(6,6));
        awareness.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JLabel awTitle = new JLabel("Awareness");
        awTitle.setFont(UIHelper.TITLE.deriveFont(Font.BOLD, 16f));
        awTitle.setForeground(UIHelper.PRIMARY);
        awareness.add(awTitle, BorderLayout.PAGE_START);

        awarenessLayout = new CardLayout();
        awarenessCards = new JPanel(awarenessLayout);
        awarenessCards.setOpaque(false);

        // Keep each slide short; font sized up and bold
        awarenessCards.add(makeAwarenessLabel("Before donation: Hydrate, eat iron-rich meals, sleep well."), "1");
        awarenessCards.add(makeAwarenessLabel("After donation: Drink fluids, rest, avoid heavy exercise for 24h."), "2");
        awarenessCards.add(makeAwarenessLabel("Prevent fainting: Deep breathing, applied muscle tension, alert staff early."), "3");
        awarenessCards.add(makeAwarenessLabel("Donation interval: Whole blood every 56 days (up to 6 times/year)."), "4");
        awarenessCards.add(makeAwarenessLabel("Boost blood count: Iron-rich intake with vitamin C for absorption."), "5");
        awarenessCards.add(makeAwarenessLabel("Do not donate if unwell or under deferral conditions; check eligibility."), "6");

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton prev = UIHelper.createPrimaryButton("Previous");
        JButton next = UIHelper.createAccentButton("Next");
        prev.addActionListener(e -> awarenessLayout.previous(awarenessCards));
        next.addActionListener(e -> awarenessLayout.next(awarenessCards));
        nav.add(prev);
        nav.add(next);

        awareness.add(awarenessCards, BorderLayout.CENTER);
        awareness.add(nav, BorderLayout.PAGE_END);
        awareness.setPreferredSize(new Dimension(10, 110)); // slightly taller to feel “more upper”
        add(awareness, BorderLayout.PAGE_END);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JComponent makeAwarenessLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return lbl;
    }
}
