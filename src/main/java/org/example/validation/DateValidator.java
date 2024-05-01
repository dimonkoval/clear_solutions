package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<ValidDateFormat, LocalDate> {
    private static final String DATE_VALIDATION_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";


    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
//        this.DATE_VALIDATION_REGEX = annotation..dateFormat();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null ) {
            return true; // Дозволяємо null або порожні значення
        }

        try {
//            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_VALIDATION_REGEX));
            String dateString = date.toString();
//            DateTimeFormatter.ofPattern(DATE_VALIDATION_REGEX);
            return dateString.matches(DATE_VALIDATION_REGEX);
//             true; // Якщо перетворення пройшло успішно, дата відповідає формату
        } catch (Exception e) {
            return false; // Якщо перетворення не вдалося, дата не відповідає формату
        }
    }
}
