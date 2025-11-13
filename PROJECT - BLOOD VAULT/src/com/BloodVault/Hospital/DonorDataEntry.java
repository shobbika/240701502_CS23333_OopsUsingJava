package com.BloodVault.hospital;

import com.BloodVault.ui.UIHelper;
import com.BloodVault.backend.hospital.DonationDAO;
import com.BloodVault.backend.health.HealthDAO;
import com.BloodVault.session.Session;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DonorDataEntry extends JFrame {

    // Allowed blood groups for FK constraint
    private static final Set<String> ALLOWED_BG = new HashSet<>(
            Arrays.asList("A+","A-","B+","B-","AB+","AB-","O+","O-")
    );

    public DonorDataEntry() {
        UIHelper.RoundedPanel root = new UIHelper.RoundedPanel(new GridBagLayout(), 12, UIHelper.CARD);
        setTitle("Donor Data Entry - Blood Vault");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 460);
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Enter Donation Details");
        title.setFont(UIHelper.TITLE);
        title.setForeground(UIHelper.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        root.add(title, gbc);

        gbc.gridwidth =1;
        gbc.gridy++;
        root.add(new JLabel("Donor ID:"), gbc);
        gbc.gridx = 1;
        JTextField did = new JTextField(14);
        root.add(did, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        JTextField date = new JTextField(12);
        root.add(date, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Blood Group:"), gbc);
        gbc.gridx = 1;
        JTextField bg = new JTextField(6);
        root.add(bg, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Units:"), gbc);
        gbc.gridx = 1;
        JTextField units = new JTextField(4);
        root.add(units, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("BP:"), gbc);
        gbc.gridx = 1;
        JTextField bp = new JTextField(10);
        root.add(bp, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Heart Rate:"), gbc);
        gbc.gridx = 1;
        JTextField hr = new JTextField(6);
        root.add(hr, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton submit = UIHelper.createAccentButton("Submit");
        root.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                // Basic validations
                String donorId = did.getText().trim();
                if (donorId.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter Donor ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String hospitalId = Session.currentHospitalId; // set at hospital login
                if (hospitalId == null || hospitalId.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No Hospital logged in. Please login again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String ymd = date.getText().trim();
                if (!isValidYMD(ymd)) {
                    JOptionPane.showMessageDialog(this, "Enter a valid date in YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String bloodGroup = bg.getText().trim().toUpperCase();
                if (!ALLOWED_BG.contains(bloodGroup)) {
                    JOptionPane.showMessageDialog(this, "Blood Group must be one of: " + ALLOWED_BG, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int unitsVal;
                try {
                    unitsVal = Integer.parseInt(units.getText().trim());
                    if (unitsVal <= 0) {
                        JOptionPane.showMessageDialog(this, "Units must be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Units must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String bpVal = bp.getText().trim();
                Integer hrVal = hr.getText().trim().isEmpty() ? null : Integer.valueOf(hr.getText().trim());

                // Save donation
                boolean a = new DonationDAO().insertDonation(donorId, hospitalId, ymd, bloodGroup, unitsVal, bpVal, hrVal);

                // Update latest health snapshot (no blood_count/height/weight fields on this form)
                boolean b = new HealthDAO().upsertLatestVitals(donorId, ymd, null, bpVal, null, null, hrVal);

                if (a && b) {
                    JOptionPane.showMessageDialog(this, "Donation + health update saved for " + donorId);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Database error — could not save donation/health", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Strict YYYY-MM-DD validation
    private boolean isValidYMD(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
