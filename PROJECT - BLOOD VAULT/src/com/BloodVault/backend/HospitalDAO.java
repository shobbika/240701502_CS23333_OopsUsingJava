package com.BloodVault.backend.hospital;

import com.BloodVault.backend.db.DB;
import java.sql.*;

public class HospitalDAO {

    public boolean insertHospital(String hospitalId, String name, String licenseNo, String location,
                                  String email, String phone, String plainPassword) {
        String sql = "INSERT INTO hospitals(hospital_id, name, license_no, location, email, phone, password_hash) " +
                "VALUES (?, ?, ?, ?, ?, ?, SHA2(?,256))";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hospitalId);
            ps.setString(2, name);
            ps.setString(3, licenseNo);
            ps.setString(4, location);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setString(7, plainPassword);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticate(String hospitalId, String plainPassword) {
        String sql = "SELECT 1 FROM hospitals WHERE hospital_id = ? AND password_hash = SHA2(?,256)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hospitalId);
            ps.setString(2, plainPassword);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
