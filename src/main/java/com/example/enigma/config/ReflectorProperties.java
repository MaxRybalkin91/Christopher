package com.example.enigma.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "reflector")
@Data
public class ReflectorProperties {
    private Map<Integer, Integer> pairs;

    public void setPairs(Map<Integer, Integer> pairs) throws Exception {
        checkSize(pairs);
        this.pairs = pairs;
        this.pairs.putAll(pairs.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getValue, Map.Entry::getKey)));
    }

    private static void checkSize(Map<Integer, Integer> pairs) throws Exception {
        if (pairs.size() != 13
                || (pairs.entrySet().stream().anyMatch(entry ->
                entry.getKey() < 1
                        && entry.getKey() > 13
                        && entry.getValue() < 14
                        && entry.getValue() > 26))) {
            throw new Exception("Incorrect reflector settings! Check duplicates and all the number pairs!");
        }
    }
}
