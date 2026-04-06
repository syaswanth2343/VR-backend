package com.example.demo.repository;

import com.example.demo.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    boolean existsByUserIdAndItemId(Long userId, String itemId);
}
