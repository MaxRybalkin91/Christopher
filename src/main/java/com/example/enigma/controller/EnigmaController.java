package com.example.enigma.controller;

import com.example.enigma.model.rest.EncodeRequest;
import com.example.enigma.service.EnigmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class EnigmaController {
    private static final String PATTERN = "[A-Za-z]+";
    private final EnigmaService enigmaService;

    @PostMapping
    public String encode(@RequestBody EncodeRequest request) {
        final String requestedText = request.getText();
        if (!requestedText.matches(PATTERN)) {
            throw new RuntimeException("Only letters are allowed!");
        }
        final char[] requestedChar = request.getText().toCharArray();
        final StringBuilder response = new StringBuilder();
        for (char c : requestedChar) {
            response.append(enigmaService.getEncoded(c));
        }
        return response.toString();
    }
}
