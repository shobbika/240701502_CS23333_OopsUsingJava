package com.BloodVault.backend.hospital;

import com.BloodVault.backend.db.DB;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class HospitalReportsDAO {

    // Returns a table model with donors who donated at this hospital, newest first
    public DefaultTableModel getRecentHospitalDonors(String hospitalId, int limit) {
        String sql =
                "SELECT d.donor_id, " +
                        "       dn.first_name, dn.last_name, dn.blood_group, dn.email, dn.mobile, " +
                        "       MAX(d.donation_date) AS last_donation " +
                        "FROM donations d " +
                        "JOIN donors dn ON dn.donor_id = d.donor_id " +
                        "WHERE d.hospital_id = ? " +
                        "GROUP BY d.donor_id, dn.first_name, dn.last_name, dn.blood_group, dn.email, dn.mobile " +
                        "ORDER BY last_donation DESC " +
                        "LIMIT ?";
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Donor ID","Name","Blood Group","Contact","Last Donation"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hospitalId);
            ps.setInt(2, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("donor_id");
                    String name = rs.getString("first_name") + " " + rs.getString("last_name");
                    String bg = rs.getString("blood_group");
                    String contact = rs.getString("mobile") + " | " + rs.getString("email");
                    Date last = rs.getDate("last_donation");
                    model.addRow(new Object[]{id, name, bg, contact, last});
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return model;
    }
}
