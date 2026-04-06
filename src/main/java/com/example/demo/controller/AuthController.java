package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            // Allow login with numeric ID or email (matches the UI hint).
            try {
                Long asId = Long.valueOf(username);
                userOpt = userRepository.findById(asId);
                if (!userOpt.isPresent()) {
                    userOpt = userRepository.findByEmail(username);
                }
            } catch (Exception ignore) {
                userOpt = userRepository.findByEmail(username);
            }
        }
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword() != null && user.getPassword().equals(password)) {
                Map<String, Object> res = new HashMap<String, Object>();
                res.put("ok", true);
                res.put("token", "dummy-jwt-token-" + user.getId());
                res.put("user", user);
                return ResponseEntity.ok(res);
            }
        }

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Invalid credentials");
        return ResponseEntity.status(401).body(res);
    }
}

