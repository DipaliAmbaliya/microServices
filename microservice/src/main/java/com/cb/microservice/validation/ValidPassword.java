package com.cb.microservice.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {ValidPasswordValidator.class}
)
public @interface ValidPassword {
        String message() default "Please provide a valid password";

        Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
