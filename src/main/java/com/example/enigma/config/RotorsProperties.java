package com.example.enigma.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "rotors")
@Data
public class RotorsProperties {
    private Map<Character, Character> first;
    private Map<Character, Character> second;
    private Map<Character, Character> third;

    public void setFirst(Map<Character, Character> first) throws Exception {
        checkSize(first, "first");
        this.first = first;
    }

    public void setSecond(Map<Character, Character> second) throws Exception {
        checkSize(second, "second");
        this.second = second;
    }

    public void setThird(Map<Character, Character> third) throws Exception {
        checkSize(third, "third");
        this.third = third;
    }

    private static void checkSize(Map<Character, Character> rotor, String name) throws Exception {
        if (rotor.keySet().size() != 26
                || new HashSet<>(rotor.values()).size() != 26) {
            throw new Exception(String.format("Incorrect %s rotor settings! Check duplicates and all the letters!", name));
        }
    }
}
