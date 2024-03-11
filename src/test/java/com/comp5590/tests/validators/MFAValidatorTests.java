package com.comp5590.tests.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.tests.basic.SetupTests;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

public class MFAValidatorTests extends SetupTests {

    @Test
    public void test2FAFailsOnMissingValues() {
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setEmail("example@example.org");
        authenticationDetails.setPassword("password");
        authenticationDetails.setTwoFactorEnabled(true);
        assertFalse(validator.validate(authenticationDetails).isEmpty());

        authenticationDetails.setRecoveryCodes("recoveryCodes");
        assertFalse(validator.validate(authenticationDetails).isEmpty());
        authenticationDetails.setRecoveryCodes(null);

        authenticationDetails.setAuthenticationToken("authenticationToken");
        assertFalse(validator.validate(authenticationDetails).isEmpty());
    }

    @Test
    public void test2FAPassesWith2FA() {
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setEmail("example@example.org");
        authenticationDetails.setPassword("password");
        authenticationDetails.setTwoFactorEnabled(true);
        authenticationDetails.setAuthenticationToken("authenticationToken");
        authenticationDetails.setRecoveryCodes("recoveryCodes");
        assertTrue(validator.validate(authenticationDetails).isEmpty());
    }

    @Test
    public void test2FAPassesWithout2FA() {
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setEmail("example@example.org");
        authenticationDetails.setPassword("password");
        authenticationDetails.setTwoFactorEnabled(false);
        assertTrue(validator.validate(authenticationDetails).isEmpty());
    }
}
