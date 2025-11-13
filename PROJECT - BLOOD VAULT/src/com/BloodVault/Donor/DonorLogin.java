package com.BloodVault.donor;

import com.BloodVault.ui.UIHelper;
import com.BloodVault.backend.donor.DonorDAO; // added

import javax.swing.*;
import java.awt.*;

/**
 * Simple donor login screen with proper password validation.
 */
public class DonorLogin extends JFrame {
    public DonorLogin() {
        UIHelper.RoundedPanel root = new UIHelper.RoundedPanel(new GridBagLayout(), 12, UIHelper.CARD);
        setTitle("Donor Login - Blood Vault");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(460, 300);
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Donor Sign In");
        title.setFont(UIHelper.TITLE);
        title.setForeground(UIHelper.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        root.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        root.add(new JLabel("Donor ID:"), gbc);
        gbc.gridx = 1;
        JTextField id = new JTextField(16);
        root.add(id, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        root.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField pf = new JPasswordField(16);
        root.add(pf, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton login = UIHelper.createPrimaryButton("Login");
        root.add(login, gbc);

        gbc.gridx = 1;
        JButton signup = UIHelper.createAccentButton("Sign Up");
        root.add(signup, gbc);

        // Fixed: validate against DB before navigating
        login.addActionListener(e -> {
            String did = id.getText().trim();
            String pw  = new String(pf.getPassword());

            if (did.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Donor ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean ok = new DonorDAO().authenticate(did, pw); // compares SHA2(?,256) in SQL
            if (ok) {
                new DonorDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Donor ID or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signup.addActionListener(e -> {
            new DonorSignup();
            dispose();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
