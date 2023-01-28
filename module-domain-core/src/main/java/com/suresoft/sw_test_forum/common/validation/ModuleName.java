package com.suresoft.sw_test_forum.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.suresoft.sw_test_forum.common.validation.ModuleNameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleName {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}