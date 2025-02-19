package com.file_hider.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * A utility class to securely store key shares in multiple, hidden system locations.
 *
 * In this example, we store each key share (provided as a byte array) in one of three locations:
 *   1. A hidden folder in the user's home directory.
 *   2. A system-wide location (using common paths based on the operating system).
 *   3. An external (e.g., USB) location (example path).
 *
 * The location for each share is chosen based on the share's index (cycling through the locations).
 */
public class KeyStorageUtil {

    // Define storage paths for different locations.
    private static final String LOCAL_PATH = System.getProperty("user.home") 
            + File.separator + ".filehider_keys" + File.separator;

    private static final String SYSTEM_PATH;
    private static final String USB_PATH;
    
    // For simplicity, we can also define an ADMIN_PATH if needed.
    // Here we simply reuse SYSTEM_PATH.
    private static final String ADMIN_PATH; 

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            SYSTEM_PATH = "C:" + File.separator + "ProgramData" + File.separator 
                    + "FileHider" + File.separator + "keys" + File.separator;
            USB_PATH = "D:" + File.separator + "FileHider" + File.separator + "keys" + File.separator;
        } else if (osName.contains("mac")) {
            SYSTEM_PATH = "/Library/Application Support/FileHider/keys/";
            USB_PATH = "/Volumes/USB/FileHider/keys/";  // Adjust based on your USB mount point.
        } else { // Assume Linux/Unix
            SYSTEM_PATH = "/var/lib/filehider/keys/";
            USB_PATH = "/media/usb/FileHider/keys/";  // Adjust according to your system.
        }
        // For this example, we use SYSTEM_PATH as the admin location.
        ADMIN_PATH = SYSTEM_PATH;
    }

    /**
     * Stores key shares into different system locations.
     *
     * The method accepts a map where each entry has:
     *   Key: an Integer representing the share number.
     *   Value: a byte[] containing the share data.
     *
     * Each share is encoded in Base64 before writing to file.
     *
     * @param shares A map of share number to share data.
     * @throws IOException If an I/O error occurs.
     */
    public static void storeKeyShares(Map<Integer, byte[]> shares) throws IOException {
        for (Map.Entry<Integer, byte[]> entry : shares.entrySet()) {
            int shareNumber = entry.getKey();
            byte[] shareData = entry.getValue();
            
            // Encode share data as a Base64 string.
            String shareStr = Base64.getEncoder().encodeToString(shareData);
            // Choose a target storage location based on share number.
            String targetPath = selectStorageLocation(shareNumber);
            // Ensure the target directory exists.
            File dir = new File(targetPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Define the file name (e.g., "share_1.key").
            File shareFile = new File(dir, "share_" + shareNumber + ".key");
            // Write the Base64 encoded share to the file.
            Files.write(shareFile.toPath(), shareStr.getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    /**
     * Retrieves all key shares from the defined storage locations.
     *
     * It searches each known location (local, system, USB) for files matching the pattern "share_*.key"
     * and returns a map where the key is the share number and the value is the decoded byte[] data.
     *
     * @return A map of share number to share data.
     * @throws IOException If an I/O error occurs.
     */
    public static Map<Integer, byte[]> retrieveKeyShares() throws IOException {
        Map<Integer, byte[]> shares = new TreeMap<>();
        // List of storage locations to search.
        String[] locations = { LOCAL_PATH, SYSTEM_PATH, USB_PATH };
        
        for (String pathStr : locations) {
            File dir = new File(pathStr);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles((d, name) -> name.startsWith("share_") && name.endsWith(".key"));
                if (files != null) {
                    for (File file : files) {
                        // Expecting file name in format "share_X.key"
                        String name = file.getName();
                        int underscoreIndex = name.indexOf("_");
                        int dotIndex = name.indexOf(".");
                        if (underscoreIndex != -1 && dotIndex != -1) {
                            String numStr = name.substring(underscoreIndex + 1, dotIndex);
                            int shareNumber = Integer.parseInt(numStr);
                            // Read file content and decode Base64 data.
                            String content = new String(Files.readAllBytes(file.toPath()));
                            byte[] shareData = Base64.getDecoder().decode(content);
                            shares.put(shareNumber, shareData);
                        }
                    }
                }
            }
        }
        return shares;
    }

    /**
     * Selects a storage location based on the share number.
     *
     * This example uses a simple modulo-based method:
     *   - If shareNumber mod 3 == 0: store in LOCAL_PATH.
     *   - If shareNumber mod 3 == 1: store in SYSTEM_PATH.
     *   - If shareNumber mod 3 == 2: store in USB_PATH.
     *
     * You can customize this method as needed.
     *
     * @param shareNumber The number of the key share.
     * @return The chosen storage path.
     */
    private static String selectStorageLocation(int shareNumber) {
        switch (shareNumber % 3) {
            case 0:
                return LOCAL_PATH;
            case 1:
                return SYSTEM_PATH;
            case 2:
                return USB_PATH;
            default:
                return LOCAL_PATH;
        }
    }
}
