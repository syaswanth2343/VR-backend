package com.example.demo.controller;

import com.example.demo.entity.Topic;
import com.example.demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    public List<Topic> listTopics() {
        return topicRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createTopic(@RequestBody Topic topic) {
        Topic saved = topicRepository.save(topic);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Topic created");
        // React admin expects `topicId` (used for subsequent PDF upload).
        res.put("topicId", saved.getId());
        res.put("id", saved.getId());
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTopic(@PathVariable Long id, @RequestBody Topic updates) {
        return topicRepository.findById(id)
                .map(topic -> {
                    if (updates.getTitle() != null) topic.setTitle(updates.getTitle());
                    if (updates.getDescription() != null) topic.setDescription(updates.getDescription());
                    if (updates.getInstructor() != null) topic.setInstructor(updates.getInstructor());
                    if (updates.getLevel() != null) topic.setLevel(updates.getLevel());
                    Topic saved = topicRepository.save(topic);
                    Map<String, Object> res = new HashMap<String, Object>();
                    res.put("message", "Topic updated");
                    res.put("id", saved.getId());
                    res.put("ok", true);
                    return ResponseEntity.ok(res);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id) {
        if (!topicRepository.existsById(id)) return ResponseEntity.notFound().build();
        topicRepository.deleteById(id);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Topic deleted");
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }
}

