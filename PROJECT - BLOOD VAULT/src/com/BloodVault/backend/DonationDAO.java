package com.BloodVault.backend.hospital;

import com.BloodVault.backend.db.DB;
import java.sql.*;

public class DonationDAO {

    public boolean insertDonation(String donorId, String hospitalId, String donationDateYmd,
                                  String bloodGroup, int units, String bp, Integer heartRate) {
        String sql = "INSERT INTO donations (donor_id, hospital_id, donation_date, blood_group, units, status, bp, heart_rate, expiry_date) " +
                "VALUES (?, ?, ?, ?, ?, 'Available', ?, ?, DATE_ADD(?, INTERVAL 42 DAY))";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, donorId);
            ps.setString(2, hospitalId);
            ps.setString(3, donationDateYmd); // 'YYYY-MM-DD'
            ps.setString(4, bloodGroup);
            ps.setInt(5, units);
            ps.setString(6, bp);
            if (heartRate == null) ps.setNull(7, Types.INTEGER); else ps.setInt(7, heartRate);
            ps.setString(8, donationDateYmd); // for expiry calc
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markUsed(long donationId) {
        String sql = "UPDATE donations SET status='Used' WHERE donation_id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, donationId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
