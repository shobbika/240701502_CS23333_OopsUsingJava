package com.BloodVault.backend.donor;

import com.BloodVault.backend.db.DB;
import java.sql.*;

public class DonorDAO {

    public boolean insertDonor(String donorId, String firstName, String lastName, int age, String bloodGroup,
                               Integer height, Integer weight, String email, String mobile, String location, String plainPassword) {
        String sql = "INSERT INTO donors(donor_id, first_name, last_name, age, blood_group, height_cm, weight_kg, email, mobile, location, password_hash) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SHA2(?,256))";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, donorId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setInt(4, age);
            ps.setString(5, bloodGroup);
            if (height == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, height);
            if (weight == null) ps.setNull(7, Types.INTEGER); else ps.setInt(7, weight);
            ps.setString(8, email);
            ps.setString(9, mobile);
            ps.setString(10, location);
            ps.setString(11, plainPassword);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticate(String donorId, String plainPassword) {
        String sql = "SELECT 1 FROM donors WHERE donor_id = ? AND password_hash = SHA2(?,256)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, donorId);
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
