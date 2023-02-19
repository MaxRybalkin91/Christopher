package com.example.enigma.controller;

import com.example.enigma.model.rest.EncodeRequest;
import com.example.enigma.service.ChristopherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnigmaController {
    private static final String PATTERN = "[A-Za-z]+";
    private final ChristopherService christopherService;

    @PostMapping
    public String encode(@RequestBody EncodeRequest request) {
        return "";
    }
}
