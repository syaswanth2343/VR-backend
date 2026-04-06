package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "ellurusaiyaswanth@gmail.com";
    private static final String ADMIN_PASSWORD = "123456";

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        // Ensure an admin account exists with the requested email/password.
        Optional<User> existing = userRepository.findByEmail(ADMIN_EMAIL);
        User admin;

        if (existing.isPresent()) {
            admin = existing.get();
        } else {
            admin = new User();
            admin.setUsername(ADMIN_USERNAME);
            admin.setEmail(ADMIN_EMAIL);
            admin.setRole("admin");
        }

        admin.setPassword(ADMIN_PASSWORD);
        if (admin.getUserCode() == null || admin.getUserCode().trim().isEmpty()) {
            // If not yet persisted, userCode will be filled after initial save.
            admin.setUserCode("TR");
        }
        if (admin.getCreatedAt() == null) admin.setCreatedAt(Instant.now());
        if (admin.getIsActive() == null) admin.setIsActive(true);

        admin = userRepository.save(admin);

        // If userCode placeholder, finalize it with ID.
        if ("TR".equals(admin.getUserCode())) {
            admin.setUserCode("TR" + admin.getId());
            userRepository.save(admin);
        }
    }
}

