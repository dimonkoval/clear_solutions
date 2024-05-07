package org.example.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDate;
import java.time.Period;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AgeCalculatorTest {

    @Test
    void calculateAge_Ok() {
        LocalDate birthDate = LocalDate.parse("1900-01-01");
        int actual = AgeCalculator.calculateAge(birthDate);
        int expected = Period.between(birthDate, LocalDate.now()).getYears();
        assertEquals(expected, actual);
    }
}