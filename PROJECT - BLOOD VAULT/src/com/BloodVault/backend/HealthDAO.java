package com.BloodVault.backend.health;

import com.BloodVault.backend.db.DB;
import java.sql.*;

public class HealthDAO {

    public boolean upsertLatestVitals(String donorId, String lastDonationDateYmd, Double bloodCount,
                                      String bp, Integer heightCm, Integer weightKg, Integer heartRate) {
        String sql = "INSERT INTO donor_health_latest (donor_id, last_donation_date, blood_count_g_dl, bp, height_cm, weight_kg, heart_rate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE last_donation_date=VALUES(last_donation_date), " +
                "blood_count_g_dl=VALUES(blood_count_g_dl), bp=VALUES(bp), height_cm=VALUES(height_cm), " +
                "weight_kg=VALUES(weight_kg), heart_rate=VALUES(heart_rate)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, donorId);
            ps.setString(2, lastDonationDateYmd);
            if (bloodCount == null) ps.setNull(3, Types.DECIMAL); else ps.setDouble(3, bloodCount);
            ps.setString(4, bp);
            if (heightCm == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, heightCm);
            if (weightKg == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, weightKg);
            if (heartRate == null) ps.setNull(7, Types.INTEGER); else ps.setInt(7, heartRate);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
