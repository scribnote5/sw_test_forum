package com.suresoft.sw_test_forum.common.validation;

import com.suresoft.sw_test_forum.util.ByteSizeUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EditorValidator implements ConstraintValidator<Editor, String> {
    long max;

    @Override
    public void initialize(Editor editor) {
        max = editor.max();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext cxt) {
        return ByteSizeUtil.getByteSize(str) < max;
    }
}