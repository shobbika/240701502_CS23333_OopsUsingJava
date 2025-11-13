package com.BloodVault.donor;

import com.BloodVault.ui.UIHelper;
import com.BloodVault.utils.Utils;
import com.BloodVault.backend.donor.DonorDAO; // FIX: correct package import

import javax.swing.*;
import java.awt.*;

public class DonorSignup extends JFrame {
    public DonorSignup() {
        UIHelper.RoundedPanel root = new UIHelper.RoundedPanel(new GridBagLayout(), 12, UIHelper.CARD);
        setTitle("Donor Signup - Blood Vault");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 640);
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,10,8,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Donor Registration");
        title.setFont(UIHelper.TITLE);
        title.setForeground(UIHelper.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        root.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        root.add(new JLabel("First Name*:"), gbc);
        gbc.gridx = 1;
        JTextField fn = new JTextField(18);
        root.add(fn, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Last Name*:"), gbc);
        gbc.gridx = 1;
        JTextField ln = new JTextField(18);
        root.add(ln, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Age*:"), gbc);
        gbc.gridx = 1;
        JTextField age = new JTextField(6);
        root.add(age, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Blood Group*:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> bg = new JComboBox<>(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});
        root.add(bg, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Height (cm):"), gbc);
        gbc.gridx = 1;
        JTextField height = new JTextField(8);
        root.add(height, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Weight (kg):"), gbc);
        gbc.gridx = 1;
        JTextField weight = new JTextField(8);
        root.add(weight, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Email*:"), gbc);
        gbc.gridx = 1;
        JTextField email = new JTextField(18);
        root.add(email, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Mobile*:"), gbc);
        gbc.gridx = 1;
        JTextField mobile = new JTextField(14);
        root.add(mobile, gbc);

        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Location*:"), gbc);
        gbc.gridx = 1;
        JTextField location = new JTextField(18);
        root.add(location, gbc);

        // Password row
        gbc.gridy++; gbc.gridx = 0;
        root.add(new JLabel("Password*:"), gbc);
        gbc.gridx = 1;
        JPasswordField pass = new JPasswordField(18);
        root.add(pass, gbc);
        gbc.gridx = 2;
        JButton genPass = UIHelper.createAccentButton("Create Password");
        root.add(genPass, gbc);

        // Confirm Password row
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
            if (fn.getText().trim().isEmpty() || ln.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter first and last name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Utils.isAgeValid(age.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Enter valid age (16+)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Utils.isEmailValid(email.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Enter valid email", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Utils.isPhoneValid(mobile.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Enter valid mobile", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String p1 = new String(pass.getPassword());
            String p2 = new String(confirm.getPassword());
            if (p1.length() < 8 || !p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords must match and be at least 8 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = Utils.generateDonorID();

            // FIX: convert optional fields to Integer or null
            Integer h = height.getText().trim().isEmpty() ? null : Integer.valueOf(height.getText().trim());
            Integer w = weight.getText().trim().isEmpty() ? null : Integer.valueOf(weight.getText().trim());

            // FIX: call DAO instance in the correct package; DAO hashes with SHA2 in SQL
            boolean ok = new DonorDAO().insertDonor(
                    id,
                    fn.getText().trim(),
                    ln.getText().trim(),
                    Integer.parseInt(age.getText().trim()),
                    bg.getSelectedItem().toString(),
                    h,
                    w,
                    email.getText().trim(),
                    mobile.getText().trim(),
                    location.getText().trim(),
                    p1
            );

            if (ok) {
                JOptionPane.showMessageDialog(this, "Account created!\nYour Donor ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
                new com.BloodVault.donor.DonorDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Database error — could not save donor", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
