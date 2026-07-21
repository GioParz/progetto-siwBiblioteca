package it.uniroma3.biblio.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotAnnoFuturoValidator implements ConstraintValidator<NotAnnoFuturo, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value <= LocalDate.now().getYear();
    }
}
