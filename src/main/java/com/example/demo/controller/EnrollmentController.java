package com.example.demo.controller;

import com.example.demo.entity.Enrollment;
import com.example.demo.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addEnrollment(@RequestBody Map<String, Long> body) {
        Long userId = body.get("user_id");
        Long topicId = body.get("topic_id");
        if (userId == null || topicId == null) return ResponseEntity.badRequest().build();

        Enrollment enrollment = new Enrollment(userId, topicId);
        enrollmentRepository.save(enrollment);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Enrollment added");
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeEnrollment(@RequestBody Map<String, Long> body) {
        Long userId = body.get("user_id");
        Long topicId = body.get("topic_id");
        if (userId == null || topicId == null) return ResponseEntity.badRequest().build();

        List<Enrollment> list = enrollmentRepository.findByUserId(userId);
        for (Enrollment e : list) {
            if (e.getTopicId() != null && e.getTopicId().equals(topicId)) {
                enrollmentRepository.delete(e);
            }
        }

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("message", "Enrollment removed");
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user/{userId}")
    public List<Enrollment> getUserEnrollments(@PathVariable Long userId) {
        return enrollmentRepository.findByUserId(userId);
    }
}

