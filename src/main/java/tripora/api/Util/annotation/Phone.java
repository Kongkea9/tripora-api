package tripora.api.Util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import tripora.api.Util.Validator.GuideValidator;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GuideValidator.KhmerPhoneValidator.class)
public @interface Phone {

    String message() default "Invalid phone number";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}