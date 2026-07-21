package it.uniroma3.biblio.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NotAnnoFuturoValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAnnoFuturo {
	
	String message() default "L'anno non può essere nel futuro";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
