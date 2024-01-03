package com.crossuniversity.securityservice.utils;

public class ConstantMessage {
    public static String PASSWORD_CHANGE_SUBJECT = "CrossUniversityLibrary: Password Reset Request";
    public static String RANDOM_PASSWORD_SUBJECT = "CrossUniversityLibrary: Account Registration";

    public static String passwordChangeMessage(String username, String secretCode){
        return String.format("Dear %s,%n%n" +
                "We received a request to reset your password for CrossUniversityLibrary. To proceed with the password reset, please follow the instructions below:%n%n" +
                "1. Copy the secret code provided below:%n" +
                "   Secret Code: %s%n%n" +
                "2. Paste the secret code into the following form:%n" +
                "   %s%n%n" +
                "If you did not initiate this password reset request, please ignore this email, and your password will remain unchanged.%n%n" +
                "Thank you for using CrossUniversityLibrary.%n%n" +
                "Best regards!", username, secretCode, "http://localhost:8080/user/change-password");
    }

    public static String randomPasswordMessage(String email, String randomPassword, String role) {
        return String.format("Dear User,%n%n" +
                "You have been registered as '%s'! Your account has been created successfully.%n" +
                "To log in, please use the following credentials:%n%n" +
                "Email: %s%n" +
                "Password: %s%n%n" +
                "We recommend changing your password after the first login for security reasons.%n%n" +
                "Thank you for choosing our service!%n" +
                "Best regards,%n" +
                "CrossUniversityLibrary", role, email, randomPassword);
    }

}
