package com.comp5590.database.validators.processors;

import com.comp5590.database.entities.User;
import com.comp5590.database.validators.annontations.RequiredRole;
import com.comp5590.enums.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator to check if the user has the required role.
 */
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
