package com.example.demo.controller;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Topic;
import com.example.demo.entity.User;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.TopicRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary() {
        Instant now = Instant.now();
        Instant monthAgo = now.minus(30, ChronoUnit.DAYS);
        Instant yearAgo = now.minus(365, ChronoUnit.DAYS);
        Instant prevMonthStart = now.minus(60, ChronoUnit.DAYS);

        List<User> allUsers = userRepository.findAll();
        long totalUsers = allUsers.size();
        long totalStudents = allUsers.stream().filter(u -> u.getRole() != null && "student".equalsIgnoreCase(u.getRole())).count();
        long totalAdmins = allUsers.stream().filter(u -> u.getRole() != null && "admin".equalsIgnoreCase(u.getRole())).count();

        long totalTopics = topicRepository.count();
        long totalEnrollments = enrollmentRepository.count();

        long newUsersMonth = allUsers.stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().isAfter(monthAgo))
                .count();
        long newUsersYear = allUsers.stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().isAfter(yearAgo))
                .count();

        long prevMonthUsers = allUsers.stream()
                .filter(u -> u.getCreatedAt() != null && !u.getCreatedAt().isBefore(prevMonthStart) && u.getCreatedAt().isBefore(monthAgo))
                .count();

        Double growthRatePercent = null;
        if (prevMonthUsers > 0) {
            double growth = ((double) (newUsersMonth - prevMonthUsers) / (double) prevMonthUsers) * 100.0;
            growthRatePercent = Math.round(growth * 10.0) / 10.0;
        }

        // group enrollments by topicId
        Map<Long, Long> countsByTopicId = new HashMap<Long, Long>();
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        for (Enrollment e : enrollments) {
            Long tid = e.getTopicId();
            if (tid == null) continue;
            Long current = countsByTopicId.get(tid);
            if (current == null) current = 0L;
            countsByTopicId.put(tid, current + 1L);
        }

        Map<Long, Topic> topicById = new HashMap<Long, Topic>();
        for (Topic t : topicRepository.findAll()) {
            topicById.put(t.getId(), t);
        }

        List<Map<String, Object>> enrollmentsByTopic = new ArrayList<Map<String, Object>>();
        List<Map.Entry<Long, Long>> entries = new ArrayList<Map.Entry<Long, Long>>(countsByTopicId.entrySet());
        entries.sort(new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> a, Map.Entry<Long, Long> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });

        for (Map.Entry<Long, Long> e : entries) {
            Long topicId = e.getKey();
            Long enrolled = e.getValue();
            Topic t = topicById.get(topicId);
            String title = t != null ? t.getTitle() : "Unknown";

            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("topicId", topicId);
            row.put("title", title);
            row.put("enrolled", enrolled);
            enrollmentsByTopic.add(row);
        }

        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("totalUsers", totalUsers);
        payload.put("totalStudents", totalStudents);
        payload.put("totalAdmins", totalAdmins);
        payload.put("totalTopics", totalTopics);
        payload.put("totalEnrollments", totalEnrollments);
        payload.put("newUsersMonth", newUsersMonth);
        payload.put("newUsersYear", newUsersYear);
        payload.put("growthRatePercent", growthRatePercent);
        payload.put("enrollmentsByTopic", enrollmentsByTopic);
        payload.put("ok", true);

        return ResponseEntity.ok(payload);
    }
}

