package com.bezkoder.springjwt.Utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationUtils {

    public static <T> String validate(T dto) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(dto);

        if (null == constraintViolations || constraintViolations.size() == 0) {
            return "";
        }
        String message = "";
        for (ConstraintViolation<T> violation : constraintViolations) {
            System.out.println(violation.getMessage());
            message += violation.getMessage() + ", ";
        }
        return message;
    }
}
