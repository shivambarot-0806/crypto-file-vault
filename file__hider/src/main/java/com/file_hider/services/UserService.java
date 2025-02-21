package com.file_hider.services;

import com.file_hider.dao.UserDAO;
import com.file_hider.models.User;
import com.file_hider.utils.KeyStorageUtil;
import com.file_hider.utils.RSAUtil;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Map;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public void registerUser(String email, String password) throws NoSuchAlgorithmException {
        String passwordHash = hashPassword(password);

        // Generate RSA key pair
        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        PrivateKey privateKey = keyPair.getPrivate();

        User user = new User(email, passwordHash, null, null);
        user.setPublicKey(publicKey);
        // user.setPrivateKey(privateKey);
        
        // split rsa private key 
        Map<Integer, byte[]> keyShares = KeyManagementService.splitKey(privateKey);
        
        // store key shares 
        try {
            KeyStorageUtil.storeKeyShares(keyShares);
        } catch (Exception e) {
            System.out.println("!!!! Error while storing key shares !!!!");
            e.printStackTrace();
        }
        userDAO.save(user);
        System.out.println("User Registered Successfully");
    }

    public User authenticateUser(String email, String password) throws NoSuchAlgorithmException {
        User user = userDAO.findByEmail(email);
        if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            return user;
        }
        return null;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
