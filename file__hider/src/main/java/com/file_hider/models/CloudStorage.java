package com.file_hider.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cloud_storage")
public class CloudStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Cloud storage record linked to a user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    // Link to the hidden file record
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private HiddenFile hiddenFile;
    
    @Column(name = "cloud_url", length = 500)
    private String cloudUrl;
    
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // Constructors
    public CloudStorage() { }

    public CloudStorage(User user, HiddenFile hiddenFile, String cloudUrl) {
        this.user = user;
        this.hiddenFile = hiddenFile;
        this.cloudUrl = cloudUrl;
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
 
    public HiddenFile getHiddenFile() {
        return hiddenFile;
    }
 
    public void setHiddenFile(HiddenFile hiddenFile) {
        this.hiddenFile = hiddenFile;
    }
 
    public String getCloudUrl() {
        return cloudUrl;
    }
 
    public void setCloudUrl(String cloudUrl) {
        this.cloudUrl = cloudUrl;
    }
 
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
 
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
