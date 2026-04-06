package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "login_history")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "login_time")
    private Instant loginTime;

    @Column(name = "logout_time")
    private Instant logoutTime;

    public LoginHistory() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    @JsonProperty("user_id")
    public Long getUser_id() { return userId; }

    @JsonProperty("user_id")
    public void setUser_id(Long user_id) { this.userId = user_id; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    @JsonProperty("ip_address")
    public String getIp_address() { return ipAddress; }

    @JsonProperty("ip_address")
    public void setIp_address(String ip_address) { this.ipAddress = ip_address; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    @JsonProperty("user_agent")
    public String getUser_agent() { return userAgent; }

    @JsonProperty("user_agent")
    public void setUser_agent(String user_agent) { this.userAgent = user_agent; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    @JsonProperty("device_type")
    public String getDevice_type() { return deviceType; }

    @JsonProperty("device_type")
    public void setDevice_type(String device_type) { this.deviceType = device_type; }

    public Instant getLoginTime() { return loginTime; }
    public void setLoginTime(Instant loginTime) { this.loginTime = loginTime; }

    @JsonProperty("login_time")
    public Instant getLogin_time() { return loginTime; }

    @JsonProperty("login_time")
    public void setLogin_time(Instant login_time) { this.loginTime = login_time; }

    public Instant getLogoutTime() { return logoutTime; }
    public void setLogoutTime(Instant logoutTime) { this.logoutTime = logoutTime; }

    @JsonProperty("logout_time")
    public Instant getLogout_time() { return logoutTime; }

    @JsonProperty("logout_time")
    public void setLogout_time(Instant logout_time) { this.logoutTime = logout_time; }
}

