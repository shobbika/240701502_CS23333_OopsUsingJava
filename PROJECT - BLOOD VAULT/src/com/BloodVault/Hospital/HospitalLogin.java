package com.BloodVault.hospital;

import com.BloodVault.ui.UIHelper;
import com.BloodVault.backend.hospital.HospitalDAO; // FIX: add DAO import
import com.BloodVault.session.Session;              // FIX: hold logged-in hospital

import javax.swing.*;
import java.awt.*;

public class HospitalLogin extends JFrame {
    public HospitalLogin() {
        UIHelper.RoundedPanel root = new UIHelper.RoundedPanel(new GridBagLayout(), 12, UIHelper.CARD);
        setTitle("Hospital Login - Blood Vault");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(460, 300);
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Hospital Sign In");
        title.setFont(UIHelper.TITLE);
        title.setForeground(UIHelper.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        root.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        root.add(new JLabel("Hospital ID:"), gbc);
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

        login.addActionListener(e -> {
            String hid = id.getText().trim();
            String pw  = new String(pf.getPassword());
            boolean ok = new HospitalDAO().authenticate(hid, pw); // SQL uses SHA2(?,256)

            if (ok) {
                Session.currentHospitalId = hid; // set context for data entry
                new HospitalDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Hospital ID or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signup.addActionListener(e -> {
            new HospitalSignup();
            dispose();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
