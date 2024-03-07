package com.comp5590.validators.processors;

import com.comp5590.entities.AuthenticationDetails;
import com.comp5590.validators.annontations.ValidMFA;
import jakarta.validation.ConstraintValidator;

public class CheckMFAValidator implements ConstraintValidator<ValidMFA, AuthenticationDetails> {

    @Override
    public boolean isValid(
        AuthenticationDetails authenticationDetails,
        jakarta.validation.ConstraintValidatorContext constraintValidatorContext
    ) {
        boolean result = true;
        if (authenticationDetails == null) {
            return true;
        } else if (authenticationDetails.isTwoFactorEnabled()) {
            result =
            authenticationDetails.getAuthenticationToken() != null &&
            !authenticationDetails.getAuthenticationToken().isEmpty();
            result =
            result &&
            authenticationDetails.getRecoveryCodes() != null &&
            !authenticationDetails.getRecoveryCodes().isEmpty();
        }
        return result;
    }
}
