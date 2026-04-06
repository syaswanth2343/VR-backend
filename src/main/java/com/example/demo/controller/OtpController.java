package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class OtpController {

    private static final String DEMO_OTP = "123456";

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, Object> body) {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("ok", true);
        res.put("message", "OTP sent (demo)");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, Object> body) {
        Object otpObj = body.get("otp");
        String otp = otpObj == null ? null : otpObj.toString();

        if (DEMO_OTP.equals(otp)) {
            Map<String, Object> res = new HashMap<String, Object>();
            res.put("ok", true);
            return ResponseEntity.ok(res);
        }

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Invalid OTP");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> body) {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("ok", true);
        res.put("message", "Password reset successful (demo)");
        return ResponseEntity.ok(res);
    }
}

