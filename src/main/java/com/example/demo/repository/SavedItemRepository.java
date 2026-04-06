package com.example.demo.repository;

import com.example.demo.entity.SavedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedItemRepository extends JpaRepository<SavedItem, Long> {
    List<SavedItem> findByUserId(Long userId);
    boolean existsByUserIdAndItemId(Long userId, String itemId);
}

