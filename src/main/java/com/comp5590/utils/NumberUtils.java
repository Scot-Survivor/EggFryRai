package com.comp5590.utils;

import java.util.concurrent.ThreadLocalRandom;

public class NumberUtils {

    // generate random number between 2 nums (inclusive)
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // generate random phone number
    public static String randomPhoneNumber() {
        StringBuilder phoneNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            phoneNumber.append((int) (Math.random() * 10));
        }
        return phoneNumber.toString();
    }

    // generate random fax number
    public static String randomFaxNumber() {
        StringBuilder faxNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            faxNumber.append((int) (Math.random() * 10));
        }
        return faxNumber.toString();
    }
}
