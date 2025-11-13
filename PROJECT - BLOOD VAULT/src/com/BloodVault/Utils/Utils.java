package com.BloodVault.utils;

import java.security.SecureRandom;
import java.util.UUID;

public class Utils {
    private static final SecureRandom RNG = new SecureRandom();

    private static final String LOWER  = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT  = "0123456789";
    private static final String SYMBOL = "!@#$%^&*()-_=+[]{};:,.?";
    private static final String ALL    = LOWER + UPPER + DIGIT + SYMBOL;

    // Unique donor ID like D8f3a1c2 (8 hex chars from UUID)
    public static String generateDonorID() {
        return "D" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    // Unique hospital ID like H9b62e7fd (8 hex chars from UUID)
    public static String generateHospitalID() {
        return "H" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static boolean isEmailValid(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isPhoneValid(String phone) {
        return phone != null && phone.matches("\\d{7,15}");
    }

    public static boolean isAgeValid(String ageStr) {
        try {
            int a = Integer.parseInt(ageStr);
            return a >= 16 && a <= 120;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String generateStrongPassword(int length) {
        if (length < 8) length = 12;
        StringBuilder sb = new StringBuilder(length);
        // ensure at least one of each category
        sb.append(LOWER.charAt(RNG.nextInt(LOWER.length())));
        sb.append(UPPER.charAt(RNG.nextInt(UPPER.length())));
        sb.append(DIGIT.charAt(RNG.nextInt(DIGIT.length())));
        sb.append(SYMBOL.charAt(RNG.nextInt(SYMBOL.length())));
        for (int i = sb.length(); i < length; i++) {
            sb.append(ALL.charAt(RNG.nextInt(ALL.length())));
        }
        // Fisher–Yates shuffle
        char[] arr = sb.toString().toCharArray();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RNG.nextInt(i + 1);
            char t = arr[i]; arr[i] = arr[j]; arr[j] = t;
        }
        return new String(arr);
    }
}
