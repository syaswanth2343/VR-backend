package com.example.demo.controller;

import com.example.demo.entity.SavedItem;
import com.example.demo.repository.SavedItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saved-items")
public class SavedItemController {

    @Autowired
    private SavedItemRepository savedItemRepository;

    @GetMapping("/{userId}")
    public List<SavedItem> getSavedItems(@PathVariable Long userId) {
        return savedItemRepository.findByUserId(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSavedItem(@RequestBody SavedItem item) {
        SavedItem saved = savedItemRepository.save(item);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Item saved");
        res.put("ok", true);
        res.put("id", saved.getId());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeSavedItem(@PathVariable Long id) {
        if (!savedItemRepository.existsById(id)) return ResponseEntity.notFound().build();
        savedItemRepository.deleteById(id);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Item removed");
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/check/{userId}/{itemId}")
    public ResponseEntity<?> checkItem(@PathVariable Long userId, @PathVariable String itemId) {
        boolean exists = savedItemRepository.existsByUserIdAndItemId(userId, itemId);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("isSaved", exists);
        return ResponseEntity.ok(res);
    }
}

