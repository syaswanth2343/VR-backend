package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    public Map<String, String> health() {
        Map<String, String> res = new HashMap<String, String>();
        res.put("status", "ok");
        return res;
    }
}

