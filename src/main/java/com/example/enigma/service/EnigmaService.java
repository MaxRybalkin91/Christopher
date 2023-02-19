package com.example.enigma.service;

import com.example.enigma.config.ReflectorProperties;
import com.example.enigma.config.RotorsProperties;
import com.example.enigma.model.Rotor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Data
public class EnigmaService {
    public static final Integer MIN_ROTOR_POSITION = 1;
    public static final Integer MAX_ROTOR_POSITION = 26;
    private static final String ROTOR_POSITION_REGEXP = "^[1-9]|1[0-9]|2[0-6]$";
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

    private final RotorsProperties rotorsProperties;
    private final ReflectorProperties reflectorProperties;
    private final Rotor fastRotor;
    private final Rotor mediumRotor;
    private final Rotor slowRotor;

    private Integer fastRotorPosition = 1;
    private Integer mediumRotorPosition = 1;
    private Integer slowRotorPosition = 1;

    public EnigmaService(RotorsProperties rotorsProperties, ReflectorProperties reflectorProperties) {
        this.rotorsProperties = rotorsProperties;
        this.reflectorProperties = reflectorProperties;
        this.fastRotor = new Rotor(getIndexes(rotorsProperties.getFirst()));
        this.mediumRotor = new Rotor(getIndexes(rotorsProperties.getSecond()));
        this.slowRotor = new Rotor(getIndexes(rotorsProperties.getThird()));

    }

    public void setRotorsPosition(int rotor1, int rotor2, int rotor3) {
        if (checkRotorRegexpNotMatched(rotor1)) {
            throw new RuntimeException("FastRotor position must be between 1 and 26!");
        } else if (checkRotorRegexpNotMatched(rotor2)) {
            throw new RuntimeException("MediumRotor position must be between 1 and 26!");
        } else if (checkRotorRegexpNotMatched(rotor3)) {
            throw new RuntimeException("SlowRotor position must be between 1 and 26!");
        }
        this.fastRotorPosition = rotor1;
        this.mediumRotorPosition = rotor2;
        this.slowRotorPosition = rotor3;
    }

    public char getEncoded(char toEncode) {
        fastRotorPosition++;
        if (fastRotorPosition > MAX_ROTOR_POSITION) {
            fastRotorPosition = 1;
            mediumRotorPosition++;
        }
        if (mediumRotorPosition > MAX_ROTOR_POSITION) {
            mediumRotorPosition = 1;
            slowRotorPosition++;
        }
        if (slowRotorPosition > MAX_ROTOR_POSITION) {
            fastRotorPosition = 1;
            mediumRotorPosition = 1;
            slowRotorPosition = 1;
        }

        int firstLeftToRightIndex = LETTER_TO_INDEX.get(toEncode) - fastRotorPosition;
        if (firstLeftToRightIndex < 1) {
            firstLeftToRightIndex += MAX_ROTOR_POSITION;
        }
        int firstLeftToRight = fastRotor.getLeftToRight().get(firstLeftToRightIndex);

        int secondLeftToRightIndex = mediumRotorPosition - firstLeftToRight;
        if (secondLeftToRightIndex < 1) {
            secondLeftToRightIndex += MAX_ROTOR_POSITION;
        }
        int secondLeftToRight = mediumRotor.getLeftToRight().get(secondLeftToRightIndex);

        int thirdLeftToRightIndex = slowRotorPosition - secondLeftToRight;
        if (thirdLeftToRightIndex < 1) {
            thirdLeftToRightIndex += MAX_ROTOR_POSITION;
        }
        int thirdLeftToRight = slowRotor.getLeftToRight().get(thirdLeftToRightIndex);
        int reflection = reflectorProperties.getPairs().get(thirdLeftToRight);

        int firstReflectedIndex = slowRotorPosition - reflection;
        if (firstReflectedIndex < 1) {
            firstReflectedIndex += MAX_ROTOR_POSITION;
        }
        int firstRightToLeft = slowRotor.getRightToLeft().get(firstReflectedIndex);

        int secondReflectedIndex = slowRotorPosition - firstRightToLeft;
        if (secondReflectedIndex < 1) {
            secondReflectedIndex += MAX_ROTOR_POSITION;
        }
        int secondRightToLeft = mediumRotor.getRightToLeft().get(secondReflectedIndex);

        int thirdReflectedIndex = fastRotorPosition - secondRightToLeft;
        if (thirdReflectedIndex < 1) {
            thirdReflectedIndex += MAX_ROTOR_POSITION;
        }
        int thirdRightToLeft = fastRotor.getRightToLeft().get(thirdReflectedIndex);
        return INDEX_TO_LETTER.get(thirdRightToLeft);
    }

    private static Map<Integer, Integer> getIndexes(Map<Character, Character> rotorsProperties) {
        return rotorsProperties.entrySet().stream().collect(Collectors.toMap(
                e -> LETTER_TO_INDEX.get(e.getKey()),
                e -> LETTER_TO_INDEX.get(e.getValue())
        ));
    }

    private boolean checkRotorRegexpNotMatched(int rotor) {
        return !(rotor + "").matches(ROTOR_POSITION_REGEXP);
    }
}
