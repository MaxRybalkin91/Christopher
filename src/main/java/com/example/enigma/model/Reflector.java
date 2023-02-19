package com.example.enigma.model;

import lombok.Data;

import java.util.Map;

@Data
public class Reflector {
    private Map<Character, Character> pairs;
}
