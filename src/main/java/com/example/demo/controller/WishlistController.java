package com.example.demo.controller;

import com.example.demo.entity.Wishlist;
import com.example.demo.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @GetMapping("/{userId}")
    public List<Wishlist> getWishlist(@PathVariable Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody Wishlist item) {
        Wishlist saved = wishlistRepository.save(item);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Item added to wishlist");
        res.put("ok", true);
        res.put("id", saved.getId());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long id) {
        if (!wishlistRepository.existsById(id)) return ResponseEntity.notFound().build();
        wishlistRepository.deleteById(id);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Item removed");
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePriority(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String priority = body.get("priority");

        return wishlistRepository.findById(id).map(item -> {
            item.setPriority(priority);
            wishlistRepository.save(item);
            Map<String, Object> res = new HashMap<String, Object>();
            res.put("message", "Priority updated");
            res.put("ok", true);
            return ResponseEntity.ok(res);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check/{userId}/{itemId}")
    public ResponseEntity<?> checkItem(@PathVariable Long userId, @PathVariable String itemId) {
        boolean exists = wishlistRepository.existsByUserIdAndItemId(userId, itemId);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("isInWishlist", exists);
        return ResponseEntity.ok(res);
    }
}

