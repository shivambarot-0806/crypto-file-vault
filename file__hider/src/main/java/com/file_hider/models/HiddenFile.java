package com.file_hider.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hidden_files")
public class HiddenFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Many hidden files belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "file_name", length = 255)
    private String fileName;
    
    @Column(name = "file_path", length = 500)
    private String filePath;
    
    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @Column(name = "encrypted_aes_key", columnDefinition = "TEXT")
    private String encryptedAESKey;
    
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // Constructors
    public HiddenFile() { }

    public HiddenFile(User user, String fileName, String filePath, String encryptedAESKey) {
        this.user = user;
        this.fileName = fileName;
        this.filePath = filePath;
        this.encryptedAESKey = encryptedAESKey;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    public String getFileName() {
        return fileName;
    }
 
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
 
    public String getFilePath() {
        return filePath;
    }
 
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public byte[] getFileData() {
        return fileData;
    }
 
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getEncryptedAESKey() {
        return encryptedAESKey;
    }
 
    public void setEncryptedAESKey(String encryptedAESKey) {
        this.encryptedAESKey = encryptedAESKey;
    }
 
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
 
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
