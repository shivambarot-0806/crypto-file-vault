package com.file_hider;

import com.file_hider.services.UserService;
import com.file_hider.models.User;

public class CLI {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar file_hider.jar <command> [options]");
            System.out.println("Commands:");
            System.out.println("  register <email> <password>");
            System.out.println("  login <email> <password>");
            return;
        }

        String command = args[0];
        UserService userService = new UserService();

        try {
            switch (command) {
                case "register":
                    if (args.length != 3) {
                        System.out.println("Usage: java -jar file_hider.jar register <email> <password>");
                        return;
                    }
                    String email = args[1];
                    String password = args[2];
                    userService.registerUser(email, password);
                    System.out.println("User registered successfully.");
                    break;
                case "login":
                    if (args.length != 3) {
                        System.out.println("Usage: java -jar file_hider.jar login <email> <password>");
                        return;
                    }
                    email = args[1];
                    password = args[2];
                    User user = userService.authenticateUser(email, password);
                    if (user != null) {
                        System.out.println("Login successful.\n");
                        Menu menu = new Menu();
                        menu.viewMenu(user);
                    } else {
                        System.out.println("Invalid email or password.");
                    }
                    break;   
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
