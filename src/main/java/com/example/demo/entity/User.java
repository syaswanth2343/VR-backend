package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    /** Not serialized in JSON responses (login still compares password in code). */
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private String role;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Lob
    @Column(name = "settings_json")
    private String settingsJson;

    @Column(name = "created_at")
    private Instant createdAt;

    public User() {}

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUserCode() { return userCode; }
    public void setUserCode(String userCode) { this.userCode = userCode; }

    @JsonProperty("user_code")
    public String getUser_code() { return userCode; }

    @JsonProperty("user_code")
    public void setUser_code(String user_code) { this.userCode = user_code; }

    @JsonProperty("full_name")
    public String getFull_name() { return fullName; }

    @JsonProperty("full_name")
    public void setFull_name(String full_name) { this.fullName = full_name; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @JsonProperty("is_active")
    public Boolean getIs_active() { return isActive; }

    @JsonProperty("is_active")
    public void setIs_active(Boolean active) { this.isActive = active; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { this.isActive = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @JsonProperty("created_at")
    public Instant getCreated_at() { return createdAt; }

    @JsonProperty("created_at")
    public void setCreated_at(Instant created_at) { this.createdAt = created_at; }

    @JsonProperty("settings")
    public Map<String, Object> getSettings() {
        if (settingsJson == null) return null;
        String trimmed = settingsJson.trim();
        if (trimmed.isEmpty()) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(trimmed, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            // If corrupted, degrade gracefully.
            return new HashMap<String, Object>();
        }
    }

    @JsonProperty("settings")
    public void setSettings(Map<String, Object> settings) {
        if (settings == null) {
            this.settingsJson = null;
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.settingsJson = mapper.writeValueAsString(settings);
        } catch (Exception e) {
            this.settingsJson = null;
        }
    }
}

