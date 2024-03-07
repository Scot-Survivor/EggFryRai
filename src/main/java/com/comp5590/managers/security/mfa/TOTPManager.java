package com.comp5590.managers.security.mfa;

import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.LoggerManager;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import java.util.Arrays;
import org.apache.logging.log4j.core.Logger;

public class TOTPManager {

    private static TOTPManager instance;
    private final SecretGenerator secretGenerator;
    private final QrGenerator qrGenerator;
    private final DefaultCodeVerifier codeVerifier;
    private final RecoveryCodeGenerator recoveryCodeGenerator;
    private final Logger logger;

    private TOTPManager() {
        this.secretGenerator = new DefaultSecretGenerator(AppConfig.TOTP_SECRET_CHARACTERS);
        this.qrGenerator = new ZxingPngQrGenerator();
        this.logger = LoggerManager.getInstance().getLogger(TOTPManager.class);
        CodeGenerator codeGenerator = new DefaultCodeGenerator(getHashAlgorithm(), AppConfig.TOTP_DIGITS);
        TimeProvider timeProvider = new SystemTimeProvider();
        this.codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        this.codeVerifier.setTimePeriod(AppConfig.TOTP_TIME_PERIOD);
        this.codeVerifier.setAllowedTimePeriodDiscrepancy(AppConfig.TOTP_CODE_ROLL);
        this.recoveryCodeGenerator = new RecoveryCodeGenerator();
    }

    public static TOTPManager getInstance() {
        if (instance == null) {
            instance = new TOTPManager();
        }
        return instance;
    }

    private HashingAlgorithm getHashAlgorithm() {
        return HashingAlgorithm.valueOf(AppConfig.TOTP_ALGORITHM.toUpperCase());
    }

    /**
     * Generate a secret
     * @return The secret
     */
    public String generateSecret() {
        return secretGenerator.generate();
    }

    /**
     * Generate the raw QR data
     * @param secret The secret
     * @return The QR data
     */
    public QrData generateRawQRData(String secret) {
        return new QrData.Builder()
            .label(AppConfig.APP_NAME)
            .issuer(AppConfig.TOTP_ISSUER_NAME)
            .secret(secret)
            .algorithm(getHashAlgorithm())
            .digits(AppConfig.TOTP_DIGITS)
            .period(AppConfig.TOTP_TIME_PERIOD)
            .build();
    }

    /**
     * Generate a PNG image of the QR code
     * @param secret The secret
     * @return The PNG image data
     */
    public byte[] generatePngImageData(String secret) {
        try {
            return qrGenerator.generate(generateRawQRData(secret));
        } catch (QrGenerationException e) {
            logger.error("Failed to generate QR code", e);
            logger.debug(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    /**
     * Generate a string of recovery codes seperated by a comma
     * @return String of recovery codes
     */
    public String generateRecoveryCodes() {
        String[] codes = recoveryCodeGenerator.generateCodes(8);
        return String.join(",", codes);
    }

    /**
     * Verify the code
     * @param secret The secret
     * @param code The code to verify
     * @return True if the code is valid
     */
    public boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }

    /**
     * Verify a recovery code
     * @param recoveryCodes The recovery codes
     *                      (comma separated)
     * @param code The code to verify
     */
    public boolean verifyRecoveryCode(String recoveryCodes, String code) {
        String[] codes = recoveryCodes.split(",");
        for (String c : codes) {
            if (c.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
