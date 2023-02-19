package com.example.enigma.model.rest;

import lombok.Data;

import java.util.Locale;

@Data
public class EncodeRequest {
    private String text;

    public void setText(String text) {
        this.text = text.toLowerCase(Locale.ROOT);
    }
}
