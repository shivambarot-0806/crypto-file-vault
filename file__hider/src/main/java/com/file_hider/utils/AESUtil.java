package com.file_hider.utils;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";

    // Generate a secrect key and returns it 
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    // encrypt file and return encrypted file data
    public static byte[] encryptFile(byte[] fileData, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(fileData);
    }

    // decrypt file and return the decrypted file data
    public static byte[] decryptFile(byte[] encryptedFile, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encryptedFile);
    }

    // Generate IV (Intialization Vector)
    public static IvParameterSpec generateIV() {
        byte[] iv = new byte[16];       // generate 16 bit array with 0s
        new SecureRandom().nextBytes(iv);       // generate random number generator which apply the random values to the iv array
        return new IvParameterSpec(iv);     // convert this iv into ivparameterspec's pgject return it
    }

    // Convert SecretKey to Base64 Stirng
    public static String encodeKey(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        // base64 is a class by using getEncoder we get instance of that class to encoding the string call encodeToString(); pass secretkey as a encoded format
    }

    // Convert base64 string back to secret key
    public static SecretKey decodeKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

}
