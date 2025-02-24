package com.file_hider;

import java.util.List;
import java.util.Scanner;

import com.file_hider.services.FileHidingService;
import com.file_hider.services.OTPService;
import com.file_hider.models.HiddenFile;
import com.file_hider.models.User;

public class Menu {
    public void viewMenu(User user) {

        FileHidingService fileHidingService = new FileHidingService();
        OTPService otpService = new OTPService();
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Choose an option:");
            System.out.println("1. Hide a file");
            System.out.println("2. Retrieve a file");
            System.out.println("3. Show all hidden files");
            System.out.println("4. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        System.out.println("Enter the file path to hide:");
                        String filePath = scanner.nextLine();
                        fileHidingService.hideFile(user, filePath);
                        System.out.println("File hidden successfully.");
                        break;
                    case 2:
                        System.out.println("Enter the file ID to retrieve:");
                        int fileId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        // Send OTP to user
                        String userOtp = otpService.generateOTP();
                        System.out.println("Otp generated successfully");
                        user.setOtp(userOtp);
                        System.out.println("Otp set to db successfully");
                        otpService.sendOTPEmail(user.getEmail(), userOtp);
                        System.out.println("Otp sent successfully");
                        System.out.println("Enter the OTP:");
                        String otp = scanner.nextLine();
                        fileHidingService.retrieveFile(user, fileId, otp);
                        System.out.println("File retrieved successfully.");
                        break;
                    case 3:
                        List<HiddenFile> hiddenFiles = fileHidingService.getAllHiddenFiles();
                        for (HiddenFile hiddenFile : hiddenFiles) {
                            System.out.println("File ID: " + hiddenFile.getId());
                            System.out.println("File Name: " + hiddenFile.getFileName());
                            System.out.println("File Path: " + hiddenFile.getFilePath());
                            System.out.println("Uploaded At: " + hiddenFile.getUploadedAt());
                            System.out.println("User ID: " + hiddenFile.getUser().getId());
                            System.out.println("Encrypted AES Key: " + hiddenFile.getEncryptedAESKey());
                            System.out.println("--------------------------------------------------");
                        }
                        break;
                    case 4:
                        loggedIn = false;
                        System.out.println("Logged out successfully.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
        scanner.close();
    }
}
