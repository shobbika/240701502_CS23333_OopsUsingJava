package com.BloodVault.hospital;

import com.BloodVault.backend.hospital.HospitalReportsDAO;
import com.BloodVault.session.Session;

import javax.swing.*;
import java.awt.*;

public class HospitalDonorsPanel extends JPanel {
    private final JTable table = new JTable();

    public HospitalDonorsPanel() {
        setLayout(new BorderLayout(8,8));

        JLabel title = new JLabel("Donors for This Hospital (Newest First)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> load());
        add(refresh, BorderLayout.SOUTH);

        load();
    }

    private void load() {
        String hid = Session.currentHospitalId;
        if (hid == null || hid.isEmpty()) {
            table.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Donor ID","Name","Blood Group","Contact","Last Donation"}
            ));
            JOptionPane.showMessageDialog(this, "No Hospital logged in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        table.setModel(new HospitalReportsDAO().getRecentHospitalDonors(hid, 100));
    }
}
