package com.example.enigma;

import com.example.enigma.controller.EnigmaController;
import com.example.enigma.model.rest.EncodeRequest;
import com.example.enigma.service.EnigmaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.example.enigma.service.EnigmaService.MAX_ROTOR_POSITION;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EnigmaApplication.class)
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@Slf4j
class EnigmaServiceApplicationTests {
    @Autowired
    private EnigmaController enigmaController;
    @Autowired
    private EnigmaService enigmaService;

    @Test
    void checkEncoding() {
        final String textToEncode = "maksim";
        final EncodeRequest request = new EncodeRequest();
        request.setText(textToEncode);
        int requiredIterations = 26*26*26;
        int iteration = 0;
        for (int i = 1; i <= MAX_ROTOR_POSITION; i++) {
            for (int j = 1; j <= MAX_ROTOR_POSITION; j++) {
                for (int k = 1; k <= MAX_ROTOR_POSITION; k++) {
                    request.setText(textToEncode);
                    enigmaService.setRotorsPosition(i, j, k);
                    final String encoded = enigmaController.encode(request);
                    request.setText(encoded);
                    enigmaService.setRotorsPosition(i, j, k);
                    final String decoded = enigmaController.encode(request);
                    assertEquals(textToEncode, decoded);
                    iteration++;
                }
            }
        }
        assertEquals(iteration, requiredIterations);
    }
}
