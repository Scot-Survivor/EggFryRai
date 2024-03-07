package com.comp5590.validators.processors;

import com.comp5590.entities.User;
import com.comp5590.enums.UserRole;
import com.comp5590.validators.annontations.RequiredRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckRoleValidator implements ConstraintValidator<RequiredRole, User> {

    private UserRole role;

    public void initialize(RequiredRole role) {
        this.role = role.value();
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        if (user == null) {
            return true;
        }
        boolean result = false;
        if (role == UserRole.PATIENT) {
            result = user.getRole() == UserRole.PATIENT;
        } else if (role == UserRole.DOCTOR) {
            result = user.getRole() == UserRole.DOCTOR;
        }

        if (!result) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Invalid role").addConstraintViolation();
        }

        return result;
    }
}
