package com.file_hider.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(name = "password_hash", nullable = false, length = 500)
    private String passwordHash;
    
    @Column(name = "otp_hash", length = 255)
    private String otpHash;
    
    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "public_key", columnDefinition = "TEXT")
    private String publicKey;

    // @Column(name = "private_key", columnDefinition = "TEXT")
    // private String privateKey;

    // Constructors
    public User() { }

    public User(String email, String passwordHash, String otpHash, LocalDateTime otpExpiry) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.otpHash = otpHash;
        this.otpExpiry = otpExpiry;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPasswordHash() {
        return passwordHash;
    }
 
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
 
    public String getOtpHash() {
        return otpHash;
    }
 
    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }
 
    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }
 
    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    // public String getPrivateKey() {
    //     return privateKey;
    // }

    // public void setPrivateKey(String privateKey) {
    //     this.privateKey = privateKey;
    // }
}
