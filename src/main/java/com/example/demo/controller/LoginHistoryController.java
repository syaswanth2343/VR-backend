package com.example.demo.controller;

import com.example.demo.entity.LoginHistory;
import com.example.demo.repository.LoginHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/login-history")
public class LoginHistoryController {

    @Autowired
    private LoginHistoryRepository historyRepository;

    @GetMapping("/{userId}")
    public List<LoginHistory> getHistory(@PathVariable Long userId) {
        return historyRepository.findByUserId(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> recordLogin(@RequestBody LoginHistory history) {
        if (history.getLoginTime() == null) {
            history.setLoginTime(Instant.now());
        }
        LoginHistory saved = historyRepository.save(history);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Login recorded");
        res.put("ok", true);
        res.put("loginId", saved.getId());
        res.put("id", saved.getId());
        return ResponseEntity.ok(res);
    }

    @PutMapping("/logout/{loginId}")
    public ResponseEntity<?> recordLogout(@PathVariable Long loginId) {
        return historyRepository.findById(loginId)
                .map(h -> {
                    h.setLogoutTime(Instant.now());
                    historyRepository.save(h);
                    Map<String, Object> res = new HashMap<String, Object>();
                    res.put("message", "Logout recorded");
                    res.put("ok", true);
                    return ResponseEntity.ok(res);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

