package com.example.enigma.model;

import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Rotor {
    private Map<Integer, Integer> leftToRight;
    private Map<Integer, Integer> rightToLeft;

    public Rotor(Map<Integer, Integer> leftToRight) {
        this.leftToRight = leftToRight;
        this.rightToLeft = leftToRight.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
