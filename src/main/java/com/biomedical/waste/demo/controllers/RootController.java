package com.biomedical.waste.demo.controllers;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RootController {

    /** Returns a simple health response indicating the API is running. */
    @GetMapping
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(Map.of("status", "OK", "service", "biomedical-waste-platform"));
    }
}

