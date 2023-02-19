package com.example.enigma.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Data
public class ChristopherService {
    public static final Integer START_ROTOR_INDEX = 1;
    public static final Integer END_ROTOR_INDEX = 26;
    public static final Integer MIDDLE_REFLECTOR_INDEX = 13;
    private static final Map<Integer, Character> INDEX_TO_LETTER = new HashMap<>() {{
        put(1, 'a');
        put(2, 'b');
        put(3, 'c');
        put(4, 'd');
        put(5, 'e');
        put(6, 'f');
        put(7, 'g');
        put(8, 'h');
        put(9, 'i');
        put(10, 'j');
        put(11, 'k');
        put(12, 'l');
        put(13, 'm');
        put(14, 'n');
        put(15, 'o');
        put(16, 'p');
        put(17, 'q');
        put(18, 'r');
        put(19, 's');
        put(20, 't');
        put(21, 'u');
        put(22, 'v');
        put(23, 'w');
        put(24, 'x');
        put(25, 'y');
        put(26, 'z');
    }};
    private static final Map<Character, Integer> LETTER_TO_INDEX = new HashMap<>() {{
        put('a', 1);
        put('b', 2);
        put('c', 3);
        put('d', 4);
        put('e', 5);
        put('f', 6);
        put('g', 7);
        put('h', 8);
        put('i', 9);
        put('j', 10);
        put('k', 11);
        put('l', 12);
        put('m', 13);
        put('n', 14);
        put('o', 15);
        put('p', 16);
        put('q', 17);
        put('r', 18);
        put('s', 19);
        put('t', 20);
        put('u', 21);
        put('v', 22);
        put('w', 23);
        put('x', 24);
        put('y', 25);
        put('z', 26);
    }};

    private Map<Character, Character> fastChars;
    private Map<Integer, Integer> fastLeftToRightIndexes;
    private Map<Integer, Integer> fastRightToLeftIndexes;
    private Map<Character, Character> mediumChars;
    private Map<Integer, Integer> mediumLeftToRightIndexes;
    private Map<Integer, Integer> mediumRightToLeftIndexes;
    private Map<Character, Character> slowChars;
    private Map<Integer, Integer> slowLeftToRightIndexes;
    private Map<Integer, Integer> slowRightToLeftIndexes;
    private Map<Integer, Integer> reflector;

    public void getSettings(String encoded, String search) {
        int fastRotorPosition = 1;
        int mediumRotorPosition = 1;
        int slowRotorPosition = 1;
        int reflectorTopIndexLeft = 0;
        int reflectorTopIndexRight = 0;
        int requiredIterations = END_ROTOR_INDEX * 26 * 26;
        int iterates = 0;
        String encodedCopy = "";
        while (!encodedCopy.contains(search)) {
            fastLeftToRightIndexes = new HashMap<>();
            fastRightToLeftIndexes = new HashMap<>();
            mediumLeftToRightIndexes = new HashMap<>();
            mediumRightToLeftIndexes = new HashMap<>();
            slowLeftToRightIndexes = new HashMap<>();
            slowRightToLeftIndexes = new HashMap<>();

            encodedCopy = encoded;
            for (int i = START_ROTOR_INDEX; i != END_ROTOR_INDEX + 1; i++) {
                int fastPosition = END_ROTOR_INDEX + i - fastRotorPosition;
                if (fastPosition > END_ROTOR_INDEX) {
                    fastPosition -= END_ROTOR_INDEX;
                } else if (fastPosition < START_ROTOR_INDEX) {
                    fastPosition += END_ROTOR_INDEX;
                }

                int mediumPosition = END_ROTOR_INDEX + i - mediumRotorPosition;
                if (mediumPosition > END_ROTOR_INDEX) {
                    mediumPosition -= END_ROTOR_INDEX;
                } else if (mediumPosition < START_ROTOR_INDEX) {
                    mediumPosition += END_ROTOR_INDEX;
                }

                int slowPosition = END_ROTOR_INDEX + i - slowRotorPosition;
                if (slowPosition > END_ROTOR_INDEX) {
                    slowPosition -= END_ROTOR_INDEX;
                } else if (slowPosition < START_ROTOR_INDEX) {
                    slowPosition += END_ROTOR_INDEX;
                }

                fastRightToLeftIndexes.put(i, fastPosition);
                mediumRightToLeftIndexes.put(i, mediumPosition);
                slowRightToLeftIndexes.put(i, slowPosition);
            }
            fastLeftToRightIndexes = fastRightToLeftIndexes.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            mediumLeftToRightIndexes = mediumRightToLeftIndexes.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            slowLeftToRightIndexes = slowRightToLeftIndexes.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

            fastRotorPosition++;
            if (fastRotorPosition > END_ROTOR_INDEX) {
                fastRotorPosition = START_ROTOR_INDEX;
                mediumRotorPosition += 1;
            }
            if (mediumRotorPosition > END_ROTOR_INDEX) {
                mediumRotorPosition = START_ROTOR_INDEX;
                slowRotorPosition += 1;
            }
            if (slowRotorPosition > END_ROTOR_INDEX) {
                fastRotorPosition = 1;
                mediumRotorPosition = 1;
                slowRotorPosition = 1;
            }

            if (iterates == 0) {
                reflector = fillReflector(reflectorTopIndexLeft, reflectorTopIndexRight);
            }

            final StringBuilder decoded = new StringBuilder();
            for (char toDecode : encodedCopy.toCharArray()) {
                int firstLeftToRightIndex = LETTER_TO_INDEX.get(toDecode) - fastRotorPosition;
                if (firstLeftToRightIndex < START_ROTOR_INDEX) {
                    firstLeftToRightIndex += END_ROTOR_INDEX;
                } else if (firstLeftToRightIndex > END_ROTOR_INDEX) {
                    firstLeftToRightIndex -= END_ROTOR_INDEX;
                }
                int firstLeftToRight = fastLeftToRightIndexes.get(firstLeftToRightIndex);

                int secondLeftToRightIndex = mediumRotorPosition - firstLeftToRight;
                if (secondLeftToRightIndex < START_ROTOR_INDEX) {
                    secondLeftToRightIndex += END_ROTOR_INDEX;
                } else if (secondLeftToRightIndex > END_ROTOR_INDEX) {
                    secondLeftToRightIndex -= END_ROTOR_INDEX;
                }
                int secondLeftToRight = mediumLeftToRightIndexes.get(secondLeftToRightIndex);

                int thirdLeftToRightIndex = slowRotorPosition - secondLeftToRight;
                if (thirdLeftToRightIndex < START_ROTOR_INDEX) {
                    thirdLeftToRightIndex += END_ROTOR_INDEX;
                } else if (firstLeftToRightIndex > END_ROTOR_INDEX) {
                    thirdLeftToRightIndex -= END_ROTOR_INDEX;
                }
                int thirdLeftToRight = slowLeftToRightIndexes.get(thirdLeftToRightIndex);
                int reflection = reflector.get(thirdLeftToRight);

                int firstReflectedIndex = slowRotorPosition - reflection;
                if (firstReflectedIndex < START_ROTOR_INDEX) {
                    firstReflectedIndex += END_ROTOR_INDEX;
                }

                int firstRightToLeft = slowRightToLeftIndexes.get(Math.max(firstReflectedIndex, START_ROTOR_INDEX));

                int secondReflectedIndex = slowRotorPosition - firstRightToLeft;
                if (secondReflectedIndex < START_ROTOR_INDEX) {
                    secondReflectedIndex += END_ROTOR_INDEX;
                }
                int secondRightToLeft = mediumRightToLeftIndexes.get(Math.max(secondReflectedIndex, START_ROTOR_INDEX));

                int thirdReflectedIndex = fastRotorPosition - secondRightToLeft;
                if (thirdReflectedIndex < START_ROTOR_INDEX) {
                    thirdReflectedIndex += END_ROTOR_INDEX;
                }
                int thirdRightToLeft = fastRightToLeftIndexes.get(Math.max(thirdReflectedIndex, START_ROTOR_INDEX));
                decoded.append(INDEX_TO_LETTER.get(thirdRightToLeft));
            }
            encodedCopy = decoded.toString();
            iterates++;
            if (iterates == requiredIterations) {
                reflectorTopIndexRight++;
                if (reflectorTopIndexRight > END_ROTOR_INDEX - 1) {
                    reflectorTopIndexLeft++;
                    reflectorTopIndexRight = 0;
                }
                if (reflectorTopIndexLeft > END_ROTOR_INDEX - 1) {
                    reflectorTopIndexLeft = 0;
                    reflectorTopIndexRight = 0;
                }
                iterates = 0;
                System.out.println("Reflector pairs are: " + reflector);
            }
        }
        System.out.println("Got it!!!");
    }

    private Map<Integer, Integer> fillReflector(int reflectorTopIndexLeft,
                                                int reflectorTopIndexRight) {
        final Map<Integer, Integer> reflector = new HashMap<>();
        for (int i = 1; i != MIDDLE_REFLECTOR_INDEX + 1; i++) {
            reflector.put(i, MIDDLE_REFLECTOR_INDEX + i);
        }
        reflector.putAll(reflector.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getValue, Map.Entry::getKey)));
        return reflector.entrySet().stream()
                .collect(Collectors.toMap(entry -> {
                            int newKey = entry.getKey() + reflectorTopIndexLeft;
                            if (newKey > END_ROTOR_INDEX) {
                                newKey -= END_ROTOR_INDEX;
                            }
                            return newKey;
                        },
                        entry -> {
                            int newValue = entry.getValue() + reflectorTopIndexRight;
                            if (newValue > END_ROTOR_INDEX) {
                                newValue -= END_ROTOR_INDEX;
                            }
                            return newValue;
                        }));
    }
}
