package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);

        // Generate fields the React UI expects.
        if (savedUser.getUserCode() == null || savedUser.getUserCode().trim().isEmpty()) {
            savedUser.setUserCode("TR" + savedUser.getId());
        }
        if (savedUser.getCreatedAt() == null) {
            savedUser.setCreatedAt(Instant.now());
        }
        if (savedUser.getIsActive() == null) {
            savedUser.setIsActive(true);
        }

        savedUser = userRepository.save(savedUser);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "User created");
        res.put("userCode", savedUser.getUserCode());
        res.put("userId", savedUser.getId());
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updates) {
        return userRepository.findById(id).map(user -> {
            if (updates.getUsername() != null) user.setUsername(updates.getUsername());
            if (updates.getEmail() != null) user.setEmail(updates.getEmail());
            if (updates.getPassword() != null) user.setPassword(updates.getPassword());
            if (updates.getRole() != null) user.setRole(updates.getRole());
            if (updates.getFullName() != null) user.setFullName(updates.getFullName());
            if (updates.getPhone() != null) user.setPhone(updates.getPhone());
            if (updates.getIsActive() != null) user.setIsActive(updates.getIsActive());
            if (updates.getSettings() != null) user.setSettings(updates.getSettings());
            userRepository.save(user);

            Map<String, Object> res = new HashMap<String, Object>();
            res.put("message", "User updated");
            res.put("ok", true);
            return ResponseEntity.ok(res);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        userRepository.deleteById(id);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "User deleted");
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(users.get(0));
    }
}

