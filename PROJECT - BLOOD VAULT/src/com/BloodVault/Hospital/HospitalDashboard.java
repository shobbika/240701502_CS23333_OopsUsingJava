package com.BloodVault.hospital;

import com.BloodVault.MainApp;
import com.BloodVault.ui.UIHelper;
import com.BloodVault.session.Session;
import com.BloodVault.backend.db.DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;      // added
import java.awt.event.WindowEvent;        // added
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class HospitalDashboard extends JFrame {

    private final Map<String, JButton> groupButtons = new LinkedHashMap<>();

    public HospitalDashboard() {
        setTitle("Hospital Dashboard - Blood Vault");
        setSize(900, 560);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIHelper.BACKGROUND);
        setLayout(new BorderLayout(8,8));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Hospital Dashboard", SwingConstants.LEFT);
        title.setFont(UIHelper.TITLE);
        title.setForeground(UIHelper.PRIMARY);
        JButton logout = UIHelper.createPrimaryButton("Logout");
        logout.addActionListener(e -> {
            Session.currentHospitalId = null;      // clear session on logout
            dispose();
            MainApp.main(new String[0]);
        });
        header.add(title, BorderLayout.LINE_START);
        header.add(logout, BorderLayout.LINE_END);
        add(header, BorderLayout.PAGE_START);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JButton viewDonors = UIHelper.createPrimaryButton("View Donors");
        viewDonors.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewDonors.addActionListener(e -> {
            JFrame f = new JFrame("Donors - This Hospital");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setContentPane(new HospitalDonorsPanel());
            f.pack();
            f.setSize(720, 480);
            f.setLocationRelativeTo(this);
            f.setVisible(true);
        });

        JButton enterData = UIHelper.createAccentButton("Enter Donation Details");
        enterData.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterData.addActionListener(e -> {
            if (Session.currentHospitalId == null || Session.currentHospitalId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please login as a Hospital first.");
                new HospitalLogin();
                return;
            }
            DonorDataEntry form = new DonorDataEntry();
            // Auto refresh stock when the entry window closes
            form.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent e1) { loadStock(); }
                @Override public void windowClosing(WindowEvent e1) { loadStock(); }
            });
        });

        left.add(viewDonors);
        left.add(Box.createVerticalStrut(12));
        left.add(enterData);
        add(left, BorderLayout.LINE_START);

        JPanel center = new JPanel(new GridLayout(2, 4, 12, 12));
        center.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] groups = {"A+","A-","B+","B-","AB+","AB-","O+","O-"};
        for (String g : groups) {
            JButton btn = makeBloodButton(g, 0, 0, 0);
            btn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    g + " Details\n" + btn.getClientProperty("metrics"),
                    "Stock - " + g, JOptionPane.INFORMATION_MESSAGE));
            groupButtons.put(g, btn);
            center.add(btn);
        }
        add(center, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);

        loadStock(); // initial load
    }

    private JButton makeBloodButton(String group, int available, int nearExpiry, int usedMonth) {
        String html = "<html><center><b>" + group + "</b><br/>" +
                "Available: " + available + "<br/>" +
                "Near expiry: " + nearExpiry + "<br/>" +
                "Used this month: " + usedMonth + "</center></html>";
        JButton b = UIHelper.createPrimaryButton(html);
        b.setPreferredSize(new Dimension(160, 100));
        b.putClientProperty("metrics",
                "Available: " + available + "\nNear expiry: " + nearExpiry + "\nUsed this month: " + usedMonth);
        return b;
    }

    private void updateButton(String group, int available, int nearExpiry, int usedMonth) {
        JButton b = groupButtons.get(group);
        if (b == null) return;
        String html = "<html><center><b>" + group + "</b><br/>" +
                "Available: " + available + "<br/>" +
                "Near expiry: " + nearExpiry + "<br/>" +
                "Used this month: " + usedMonth + "</center></html>";
        b.setText(html);
        b.putClientProperty("metrics",
                "Available: " + available + "\nNear expiry: " + nearExpiry + "\nUsed this month: " + usedMonth);
    }

    // Load stock metrics for the logged-in hospital
    private void loadStock() {
        String hid = Session.currentHospitalId;
        if (hid == null || hid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Hospital logged in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] groups = {"A+","A-","B+","B-","AB+","AB-","O+","O-"};
        Map<String,int[]> data = new LinkedHashMap<>();
        for (String g : groups) data.put(g, new int[]{0,0,0});

        String qAvail =
                "SELECT blood_group, COALESCE(SUM(units),0) AS total " +
                        "FROM donations WHERE hospital_id=? AND status='Available' AND expiry_date > CURDATE() " +
                        "GROUP BY blood_group";
        String qNear =
                "SELECT blood_group, COALESCE(SUM(units),0) AS total " +
                        "FROM donations WHERE hospital_id=? AND status='Available' " +
                        "AND DATEDIFF(expiry_date, CURDATE()) BETWEEN 0 AND 7 GROUP BY blood_group";
        String qUsed =
                "SELECT blood_group, COALESCE(SUM(units),0) AS total " +
                        "FROM donations WHERE hospital_id=? AND status='Used' " +
                        "AND DATE_FORMAT(donation_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m') " +
                        "GROUP BY blood_group";

        try (Connection c = DB.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(qAvail)) {
                ps.setString(1, hid);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String bg = rs.getString("blood_group");
                        int val = rs.getInt("total");
                        data.computeIfAbsent(bg, k -> new int[]{0,0,0})[0] = val;
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement(qNear)) {
                ps.setString(1, hid);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String bg = rs.getString("blood_group");
                        int val = rs.getInt("total");
                        data.computeIfAbsent(bg, k -> new int[]{0,0,0})[1] = val;
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement(qUsed)) {
                ps.setString(1, hid);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String bg = rs.getString("blood_group");
                        int val = rs.getInt("total");
                        data.computeIfAbsent(bg, k -> new int[]{0,0,0})[2] = val;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load stock: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        for (Map.Entry<String,int[]> e : data.entrySet()) {
            String g = e.getKey();
            int[] v = e.getValue();
            updateButton(g, v[0], v[1], v[2]);
        }
    }
}
