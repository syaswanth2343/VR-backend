package com.example.demo.controller;

import com.example.demo.entity.Topic;
import com.example.demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/pdfs")
public class PdfController {

    @Autowired
    private TopicRepository topicRepository;

    private static class PdfMeta {
        final Long id;
        final String storedFilename;
        final String originalFilename;
        final Long topicId;
        final long size;
        final Instant uploadedAt;

        PdfMeta(Long id, String storedFilename, String originalFilename, Long topicId, long size, Instant uploadedAt) {
            this.id = id;
            this.storedFilename = storedFilename;
            this.originalFilename = originalFilename;
            this.topicId = topicId;
            this.size = size;
            this.uploadedAt = uploadedAt;
        }
    }

    private static final ConcurrentMap<Long, PdfMeta> PDF_STORE = new ConcurrentHashMap<Long, PdfMeta>();
    private static final AtomicLong PDF_ID_SEQ = new AtomicLong(1);
    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads");

    @GetMapping
    public List<Map<String, Object>> listPdfs(@RequestParam(required = false) Long topicId) {
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();

        for (PdfMeta meta : PDF_STORE.values()) {
            if (topicId != null && (meta.topicId == null || !topicId.equals(meta.topicId))) {
                continue;
            }

            Topic topic = meta.topicId != null ? topicRepository.findById(meta.topicId).orElse(null) : null;
            String topicTitle = topic != null ? topic.getTitle() : null;

            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", meta.id);
            row.put("filename", meta.storedFilename);
            row.put("original_name", meta.originalFilename);
            row.put("size", meta.size);
            row.put("uploaded_at", meta.uploadedAt.toString());
            row.put("topicId", meta.topicId);
            row.put("topic_id", meta.topicId);
            if (topicTitle != null) row.put("topic_title", topicTitle);
            row.put("url", "/api/pdfs/download/" + meta.id);

            out.add(row);
        }

        return out;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file,
                                        @RequestParam(required = false) Long topicId) {
        if (file == null || file.isEmpty()) {
            Map<String, Object> res = new LinkedHashMap<String, Object>();
            res.put("message", "No file received");
            res.put("ok", false);
            return ResponseEntity.badRequest().body(res);
        }

        try {
            Files.createDirectories(UPLOAD_DIR);

            String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document.pdf";
            String storedFilename = UUID.randomUUID().toString() + "_" + originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path storedPath = UPLOAD_DIR.resolve(storedFilename);

            file.transferTo(storedPath);

            long size = Files.size(storedPath);
            Long id = PDF_ID_SEQ.getAndIncrement();
            Instant uploadedAt = Instant.now();

            PDF_STORE.put(id, new PdfMeta(id, storedFilename, originalName, topicId, size, uploadedAt));

            Map<String, Object> payload = new LinkedHashMap<String, Object>();
            payload.put("message", "File uploaded successfully");
            payload.put("ok", true);
            payload.put("id", id);
            payload.put("filename", storedFilename);
            payload.put("original_name", originalName);
            payload.put("size", size);
            payload.put("uploaded_at", uploadedAt.toString());
            payload.put("topicId", topicId);
            payload.put("topic_id", topicId);
            payload.put("url", "/api/pdfs/download/" + id);

            return ResponseEntity.ok(payload);
        } catch (IOException e) {
            Map<String, Object> res = new LinkedHashMap<String, Object>();
            res.put("message", "Failed to store upload");
            res.put("ok", false);
            return ResponseEntity.status(500).body(res);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePdf(@PathVariable Long id) {
        PdfMeta meta = PDF_STORE.remove(id);
        if (meta == null) return ResponseEntity.notFound().build();

        try {
            Path storedPath = UPLOAD_DIR.resolve(meta.storedFilename);
            Files.deleteIfExists(storedPath);
        } catch (IOException ignored) {
            // Demo: ignore delete failures.
        }

        Map<String, Object> res = new LinkedHashMap<String, Object>();
        res.put("message", "Pdf deleted");
        res.put("ok", true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) {
        PdfMeta meta = PDF_STORE.get(id);
        if (meta == null) return ResponseEntity.notFound().build();

        try {
            Path storedPath = UPLOAD_DIR.resolve(meta.storedFilename);
            if (!Files.exists(storedPath)) return ResponseEntity.notFound().build();

            Resource resource = new UrlResource(storedPath.toUri());

            String encodedFilename = URLEncoder.encode(meta.originalFilename, StandardCharsets.UTF_8.name());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(meta.size)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}

