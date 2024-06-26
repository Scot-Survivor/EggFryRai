package com.comp5590.database.validators.processors;

import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.validators.annontations.ValidMFA;
import jakarta.validation.ConstraintValidator;

/**
 * Validator to check if the user has MFA enabled.
 */
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
