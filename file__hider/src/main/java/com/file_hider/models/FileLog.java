package com.file_hider.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_logs")
public class FileLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // File log entry can be linked to a user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "file_name", length = 255)
    private String fileName;
    
    @Column(name = "action", length = 50)
    private String action; // e.g., HIDE, UNHIDE, DELETE, UPLOAD
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructors
    public FileLog() { }

    public FileLog(User user, String fileName, String action) {
        this.user = user;
        this.fileName = fileName;
        this.action = action;
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
 
    public String getAction() {
        return action;
    }
 
    public void setAction(String action) {
        this.action = action;
    }
 
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
 
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
