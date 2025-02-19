package com.file_hider.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KeyStorageTest {

    public static void main(String[] args) {
        try {
            // Step 1: Generate Dummy Key Shares (Simulating Shamir's Secret Sharing)
            Map<Integer, byte[]> keyShares = generateDummyShares(3); // Generate 3 dummy shares

            // Step 2: Store Key Shares
            System.out.println("Storing key shares...");
            KeyStorageUtil.storeKeyShares(keyShares);
            System.out.println("Key shares stored successfully.");

            // Step 3: Retrieve Key Shares
            System.out.println("Retrieving key shares...");
            Map<Integer, byte[]> retrievedShares = KeyStorageUtil.retrieveKeyShares();
            System.out.println("Retrieved key shares: " + retrievedShares.size());

            // Step 4: Compare Original and Retrieved Shares
            boolean isSame = compareShares(keyShares, retrievedShares);
            System.out.println("Validation Result: " + (isSame ? "SUCCESS ✅ - Shares Matched" : "FAILED ❌ - Shares Mismatch"));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred during key storage/retrieval!");
        }
    }

    /**
     * Generates dummy key shares (for testing purposes).
     * @param numShares Number of shares to generate.
     * @return A map of share index to random byte[] data.
     */
    private static Map<Integer, byte[]> generateDummyShares(int numShares) {
        Map<Integer, byte[]> shares = new HashMap<>();
        Random random = new Random();

        for (int i = 1; i <= numShares; i++) {
            byte[] data = new byte[16]; // Example: 16-byte key share
            random.nextBytes(data);
            shares.put(i, data);
        }
        return shares;
    }

    /**
     * Compares original and retrieved key shares.
     * @param original Original shares stored.
     * @param retrieved Retrieved shares from storage.
     * @return true if all shares match, false otherwise.
     */
    private static boolean compareShares(Map<Integer, byte[]> original, Map<Integer, byte[]> retrieved) {
        if (original.size() != retrieved.size()) {
            return false;
        }
        for (int key : original.keySet()) {
            if (!java.util.Arrays.equals(original.get(key), retrieved.get(key))) {
                return false;
            }
        }
        return true;
    }
}
