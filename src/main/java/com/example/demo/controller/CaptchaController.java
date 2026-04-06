package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CaptchaController {

    private static final String SECRET = "demo-captcha-secret";

    @GetMapping("/captcha-challenge")
    public ResponseEntity<?> challenge() {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        long ts = System.currentTimeMillis();
        String sig = sign(nonce + ":" + ts);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("nonce", nonce);
        res.put("ts", ts);
        res.put("sig", sig);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/verify-captcha")
    public ResponseEntity<?> verify(@RequestBody Map<String, Object> body) {
        // Frontend also does client-side validation; this is just a basic server acknowledgement.
        Object honeypot = body.get("honeypot");
        boolean ok = (honeypot == null) || (honeypot.toString().trim().isEmpty());

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", ok);
        return ResponseEntity.ok(res);
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < raw.length; i++) {
                sb.append(String.format("%02x", raw[i]));
            }
            return sb.toString();
        } catch (Exception e) {
            return "0";
        }
    }
}

