package com.suresoft.sw_test_forum.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ModuleNameValidator implements ConstraintValidator<ModuleName, String> {
    @Override
    public void initialize(ModuleName moduleName) {
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext cxt) {
        boolean valid = false;

        if ("module-app-web".equals(str) || "module-app-admin".equals(str)) {
            valid = true;
        }

        return valid;
    }
}