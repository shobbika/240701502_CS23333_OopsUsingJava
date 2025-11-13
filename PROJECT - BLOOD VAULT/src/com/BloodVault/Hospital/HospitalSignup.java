package com.BloodVault.hospital;

import com.BloodVault.ui.UIHelper;
import com.BloodVault.utils.Utils;
import com.BloodVault.backend.hospital.HospitalDAO;
import com.BloodVault.session.Session; // added

import javax.swing.*;
import java.awt.*;

public class HospitalSignup extends JFrame {
    public HospitalSignup() {
        UIHelper.RoundedPanel root = new UIHelper.RoundedPanel(new GridBagLayout(), 12, UIHelper.CARD);
        setTitle("Hospital Signup - Blood Vault");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 520);
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,10,8,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Hospital Registration");
        title.setFont(UIHelper.TITLE);
        title.setForeground(UIHelper.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        root.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        root.add(new JLabel("Hospital Name*:"), gbc);
        gbc.gridx = 1;
        JTextField name = new JTextField(20);
        root.add(name, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("License No*:"), gbc);
        gbc.gridx = 1;
        JTextField lic = new JTextField(20);
        root.add(lic, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Location*:"), gbc);
        gbc.gridx = 1;
        JTextField loc = new JTextField(20);
        root.add(loc, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Email*:"), gbc);
        gbc.gridx = 1;
        JTextField email = new JTextField(20);
        root.add(email, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Phone*:"), gbc);
        gbc.gridx = 1;
        JTextField phone = new JTextField(14);
        root.add(phone, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Password*:"), gbc);
        gbc.gridx = 1;
        JPasswordField pass = new JPasswordField(18);
        root.add(pass, gbc);
        gbc.gridx = 2;
        JButton genPass = UIHelper.createAccentButton("Create Password");
        root.add(genPass, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Confirm Password*:"), gbc);
        gbc.gridx = 1;
        JPasswordField confirm = new JPasswordField(18);
        root.add(confirm, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER;
        JButton create = UIHelper.createAccentButton("Create Account");
        root.add(create, gbc);

        genPass.addActionListener(e -> {
            String generated = Utils.generateStrongPassword(12);
            pass.setText(generated);
            confirm.setText(generated);
            JOptionPane.showMessageDialog(this, "Generated password:\n" + generated, "Password", JOptionPane.INFORMATION_MESSAGE);
        });

        create.addActionListener(e -> {
            if (name.getText().trim().isEmpty() || lic.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill mandatory fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Utils.isEmailValid(email.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Enter valid email", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Utils.isPhoneValid(phone.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Enter valid phone", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String p1 = new String(pass.getPassword());
            String p2 = new String(confirm.getPassword());
            if (p1.length() < 8 || !p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords must match and be at least 8 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = Utils.generateHospitalID();

            boolean ok = new HospitalDAO().insertHospital(
                    id,
                    name.getText().trim(),
                    lic.getText().trim(),
                    loc.getText().trim(),
                    email.getText().trim(),
                    phone.getText().trim(),
                    p1 // DAO hashes in SQL
            );

            if (ok) {
                // Auto-login so session is ready for DonorDataEntry, avoiding “No Hospital logged in”
                Session.currentHospitalId = id; // FIX
                JOptionPane.showMessageDialog(this, "Hospital registered!\nHospital ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
                new HospitalDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Database error — could not save hospital", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
