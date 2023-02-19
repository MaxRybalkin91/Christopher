package com.example.enigma;

import com.example.enigma.controller.EnigmaController;
import com.example.enigma.service.ChristopherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = EnigmaApplication.class)
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@Slf4j
class ChristopherServiceApplicationTests {
    @Autowired
    private EnigmaController enigmaController;
    @Autowired
    private ChristopherService christopherService;

    @Test
    void checkEncoding() {
    }
}
