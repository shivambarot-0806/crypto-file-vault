package com.file_hider.services;

import com.file_hider.utils.AESUtil;
import com.file_hider.utils.RSAUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileEncryptionService {
    public static class EncryptedFileResult {
        private byte[] encryptedData;
        private String encryptedAESKey;
        
        public EncryptedFileResult(byte[] encryptedData, String encryptedAESKey) {
            this.encryptedData = encryptedData;
            this.encryptedAESKey = encryptedAESKey;
        }
        
        public byte[] getEncryptedData() {
            return encryptedData;
        }
        
        public String getEncryptedAESKey() {
            return encryptedAESKey;
        }
    }
    /**
     * Encrypts a file using AES and then encrypts the AES key using RSA.
     *
     * @param filePath  Path to the plaintext file. Path to store the encrypted file.
     * @param rsaPublicKey   RSA public key for encrypting the AES key.
     * @return Encrypted File result instance which contains (Encrypted AES key as a Base64 string + Encrypted File Data).
     * @throws Exception In case of encryption errors.
     */
    public static EncryptedFileResult encryptFileToBytes(String filePath, java.security.PublicKey rsaPublicKey) throws Exception {
        // Read file data
        byte[] fileData = Files.readAllBytes(Path.of(filePath));

        // Generate a random AES key and IV
        SecretKey aesKey = AESUtil.generateAESKey();
        IvParameterSpec iv = AESUtil.generateIV();

        // Encrypt file data using AES
        byte[] encryptedFileData = AESUtil.encryptFile(fileData, aesKey, iv);
        
        // Save the IV and encrypted file data (IV is needed for decryption)
        // One simple method: prepend the IV bytes to the encrypted file data.
        byte[] combinedData = new byte[iv.getIV().length + encryptedFileData.length];
        System.arraycopy(iv.getIV(), 0, combinedData, 0, iv.getIV().length);
        System.arraycopy(encryptedFileData, 0, combinedData, iv.getIV().length, encryptedFileData.length);
        // Files.write(Path.of(filePath), combinedData);

        // Encrypt the AES key using RSA public key (convert AES key to Base64 first if needed)
        String aesKeyStr = AESUtil.encodeKey(aesKey);
        String encryptedAESKey = RSAUtil.encryptAESKey(aesKeyStr, rsaPublicKey);

        // Return the encrypted AES key (this should be stored in your DB with file metadata)
        return new EncryptedFileResult(combinedData, encryptedAESKey);
    }

    /**
     * Decrypts a file given the encrypted AES key and RSA private key.
     *
     * @param encryptedAESKey  Encrypted AES key as Base64 string.
     * @param filePath    Path to the encrypted file. Path to store the decrypted (plaintext) file.
     * @param rsaPrivateKey    RSA private key for decrypting the AES key.
     * @return true if decryption is successful.
     * @throws Exception In case of decryption errors.
     */
    public static boolean decryptFile(String encryptedAESKey, byte[] combinedData, String filePath, java.security.PrivateKey rsaPrivateKey) throws Exception {
        // Decrypt AES key using RSA private key
        String aesKeyStr = RSAUtil.decryptAESKey(encryptedAESKey, rsaPrivateKey);
        SecretKey aesKey = AESUtil.decodeKey(aesKeyStr);

        // Read combined IV and encrypted file data from file
        // byte[] combinedData = Files.readAllBytes(Path.of(filePath));

        // Extract IV (first 16 bytes for AES)
        byte[] ivBytes = new byte[16];
        System.arraycopy(combinedData, 0, ivBytes, 0, 16);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        // Extract encrypted file data
        byte[] encryptedFileData = new byte[combinedData.length - 16];
        System.arraycopy(combinedData, 16, encryptedFileData, 0, encryptedFileData.length);

        // Decrypt file data using AES
        byte[] decryptedFileData = AESUtil.decryptFile(encryptedFileData, aesKey, iv);

        // Write decrypted file
        Files.write(Path.of(filePath), decryptedFileData);

        return true;
    }
}
