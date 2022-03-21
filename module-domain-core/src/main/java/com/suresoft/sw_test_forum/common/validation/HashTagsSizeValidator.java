package com.suresoft.sw_test_forum.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HashTagsSizeValidator implements ConstraintValidator<HashTagsSize, String> {
    long max;

    @Override
    public void initialize(HashTagsSize hashTags) {
        max = hashTags.max();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext cxt) {
        return str.length() < max;
    }
}