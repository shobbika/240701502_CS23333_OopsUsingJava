package com.BloodVault;

import com.BloodVault.ui.UIHelper;
import com.BloodVault.donor.DonorLogin;
import com.BloodVault.donor.DonorSignup;
import com.BloodVault.hospital.HospitalLogin;
import com.BloodVault.hospital.HospitalSignup;

import javax.swing.*;
import java.awt.*;

/**
 * Entry point. Modern two-card home screen for Donor / Hospital.
 */
public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blood Vault");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(UIHelper.BACKGROUND);
            frame.setSize(760, 460);
            frame.setLayout(new GridBagLayout());
            frame.setResizable(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(16, 16, 16, 16);

            // Title
            JLabel title = new JLabel("Blood Vault");
            title.setFont(UIHelper.TITLE);
            title.setForeground(UIHelper.PRIMARY);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            frame.add(title, gbc);

            // Donor card
            UIHelper.RoundedPanel donorCard = new UIHelper.RoundedPanel(new GridBagLayout(), 14, UIHelper.CARD);
            donorCard.setPreferredSize(new Dimension(300, 180));
            GridBagConstraints d = new GridBagConstraints();
            d.insets = new Insets(10, 10, 10, 10);
            d.gridx = 0; d.gridy = 0; d.gridwidth = 2;
            JLabel dTitle = new JLabel("Donor");
            dTitle.setFont(UIHelper.SUBTITLE);
            donorCard.add(dTitle, d);

            d.gridy = 1; d.gridwidth = 1;
            JButton dLogin = UIHelper.createPrimaryButton("Login");
            dLogin.addActionListener(e -> {
                new DonorLogin();
                frame.dispose();
            });
            donorCard.add(dLogin, d);

            d.gridx = 1;
            JButton dSignup = UIHelper.createAccentButton("Sign Up");
            dSignup.addActionListener(e -> {
                new DonorSignup();
                frame.dispose();
            });
            donorCard.add(dSignup, d);

            // Hospital card
            UIHelper.RoundedPanel hospCard = new UIHelper.RoundedPanel(new GridBagLayout(), 14, UIHelper.CARD);
            hospCard.setPreferredSize(new Dimension(300, 180));
            GridBagConstraints h = new GridBagConstraints();
            h.insets = new Insets(10, 10, 10, 10);
            h.gridx = 0; h.gridy = 0; h.gridwidth = 2;
            JLabel hTitle = new JLabel("Hospital");
            hTitle.setFont(UIHelper.SUBTITLE);
            hospCard.add(hTitle, h);

            h.gridy = 1; h.gridwidth = 1;
            JButton hLogin = UIHelper.createPrimaryButton("Login");
            hLogin.addActionListener(e -> {
                new com.BloodVault.hospital.HospitalLogin();
                frame.dispose();
            });
            hospCard.add(hLogin, h);

            h.gridx = 1;
            JButton hSignup = UIHelper.createAccentButton("Sign Up");
            hSignup.addActionListener(e -> {
                new HospitalSignup();
                frame.dispose();
            });
            hospCard.add(hSignup, h);

            // Place cards side-by-side
            gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridx = 0;
            frame.add(donorCard, gbc);
            gbc.gridx = 1;
            frame.add(hospCard, gbc);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
