package com.file_hider.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "key_shares")
public class KeyShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Each key share belongs to a user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "encrypted_share_data", nullable = false, columnDefinition = "TEXT")
    private String encryptedShareData;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type", nullable = false)
    private StorageType storageType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public KeyShare() { }

    public KeyShare(User user, String encryptedShareData, StorageType storageType) {
        this.user = user;
        this.encryptedShareData = encryptedShareData;
        this.storageType = storageType;
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
 
    public String getEncryptedShareData() {
        return encryptedShareData;
    }
 
    public void setEncryptedShareData(String encryptedShareData) {
        this.encryptedShareData = encryptedShareData;
    }
 
    public StorageType getStorageType() {
        return storageType;
    }
 
    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

