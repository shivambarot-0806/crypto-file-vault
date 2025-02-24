package com.file_hider.services;

import com.file_hider.dao.FileDAO;
import com.file_hider.dao.FileLogDAO;
import com.file_hider.models.FileLog;
import com.file_hider.models.HiddenFile;
import com.file_hider.models.User;
import com.file_hider.utils.KeyStorageUtil;
import com.file_hider.services.FileEncryptionService.EncryptedFileResult;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.List;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHidingService {
    private final FileDAO fileDAO = new FileDAO();
    private final FileLogDAO fileLogDAO = new FileLogDAO();
    private final CloudStorageService cloudStorageService = new CloudStorageService();
    // private final OTPService otpService = new OTPService();

    public void hideFile(User user, String filePath) throws Exception {
        
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Use the user's existing RSA keys
        byte[] publicKeyBytes = Base64.getDecoder().decode(user.getPublicKey());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Get Encrypted file data and encrypted AES key
        EncryptedFileResult encryptedFileResult = FileEncryptionService.encryptFileToBytes(filePath, publicKey);
        
        String fileName = Paths.get(filePath).getFileName().toString();
        
        // Store the encrypted file data and AES key in the database
        HiddenFile hiddenFile = new HiddenFile(user, fileName, filePath, encryptedFileResult.getEncryptedData(), encryptedFileResult.getEncryptedAESKey());
        fileDAO.save(hiddenFile);

        // delete file from directory
        Files.delete(Paths.get(filePath));

        // Log the file hiding action
        FileLog fileLog = new FileLog(user, fileName, "HIDE");
        fileLogDAO.save(fileLog);

        
    }

    public void retrieveFile(User user, int id, String otp) throws Exception {
        
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Verify OTP
        if (!verifyOTP(user, otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        // Retrieve the hidden file record
        HiddenFile hiddenFile = fileDAO.findById(id);
        if (hiddenFile == null) {
            throw new IllegalArgumentException("File not found");
        }

        // Use the user's existing RSA keys
        Map<Integer, byte[]> privateKeyShares = KeyStorageUtil.retrieveKeyShares();
        PrivateKey privateKey = KeyManagementService.reconstructKey(privateKeyShares);

        String encryptedAESKey = hiddenFile.getEncryptedAESKey();
        byte[] hiddenFileData = hiddenFile.getFileData();
        String filePath = hiddenFile.getFilePath();

        // Get decrytped file data
        byte[] decryptedFileData = FileEncryptionService.decryptFileTobytes(encryptedAESKey, hiddenFileData, filePath, privateKey);

        // Write decrypted file data to the output file path
        Files.write(Paths.get(hiddenFile.getFilePath()), decryptedFileData);

        // Log the file retrieval action
        FileLog fileLog = new FileLog(user, hiddenFile.getFileName(), "RETRIEVE");
        fileLogDAO.save(fileLog);
    }

    public List<HiddenFile> getAllHiddenFiles() {
        return fileDAO.findAll();
    }

    private boolean verifyOTP(User user, String otp) {
        // Implement OTP verification logic
        if (otp.equals(user.getOtp())) {
            return true;
        }
        return false;
    }
}
