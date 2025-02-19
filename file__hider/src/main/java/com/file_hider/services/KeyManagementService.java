package com.file_hider.services;

import com.codahale.shamir.*;
import com.file_hider.utils.RSAUtil;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

public class KeyManagementService {
    private static final int TOTAL_SHARES = 5;
    private static final int REQUIRED_SHARES = 3;

    /**
     * Splits the RSA private key using Shamir's Secret Sharing.
     */
    public static Map<Integer, byte[]> splitKey(PrivateKey privateKey) {
        // Create Shamir's Secret Sharing scheme
        Scheme scheme = new Scheme(new SecureRandom(), TOTAL_SHARES, REQUIRED_SHARES);

        // Convert private key to byte array
        byte[] privateKeyBytes = privateKey.getEncoded();

        // Split the private key into shares
        Map<Integer, byte[]> shares = scheme.split(privateKeyBytes);
        return shares;
    }

    /**
     * Reconstructs the RSA private key from shares.
     */
    public static byte[] reconstructKey(Map<Integer, byte[]> shares) {
        Scheme scheme = new Scheme(new SecureRandom(), TOTAL_SHARES, REQUIRED_SHARES);
        return scheme.join(shares);
    }

    /**
     * Converts key shares to Base64 (For storage in files).
     */
    public static Map<Integer, String> encodeShares(Map<Integer, byte[]> shares) {
        Map<Integer, String> encodedShares = new TreeMap<>();
        for (Map.Entry<Integer, byte[]> entry : shares.entrySet()) {
            encodedShares.put(entry.getKey(), Base64.getEncoder().encodeToString(entry.getValue()));
        }
        return encodedShares;
    }

    /**
     * Decodes Base64 shares back to byte array.
     */
    public static Map<Integer, byte[]> decodeShares(Map<Integer, String> encodedShares) {
        Map<Integer, byte[]> shares = new TreeMap<>();
        for (Map.Entry<Integer, String> entry : encodedShares.entrySet()) {
            shares.put(entry.getKey(), Base64.getDecoder().decode(entry.getValue()));
        }
        return shares;
    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair = RSAUtil.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            
            
            Map<Integer, byte[]> shares = splitKey(privateKey);
            
            Map<Integer, String> encodedShares = encodeShares(shares);
            System.out.println("Generated Key Shares........");
            encodedShares.forEach((key, value) -> System.out.println("Shares " + key + " : " + value));
            
            Map<Integer, String> subsetShares = Map.of(
                1, encodedShares.get(1),
                2, encodedShares.get(2),
                4, encodedShares.get(4));
                
                // Decode shares and reconstruct the private key
                Map<Integer, byte[]> decodedShares = KeyManagementService.decodeShares(subsetShares);
                byte[] reconstructedKeyBytes = KeyManagementService.reconstructKey(decodedShares);
                
                // Verify if reconstruction is correct
                System.out.println("\nOriginal Private Key Length: " + privateKey.getEncoded().length);
                System.out.println("Reconstructed Private Key Length: " + reconstructedKeyBytes.length);
                System.out.println("Match: " + (privateKey.getEncoded().length == reconstructedKeyBytes.length));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
