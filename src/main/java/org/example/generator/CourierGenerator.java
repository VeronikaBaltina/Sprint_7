package org.example.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.Courier;

public class CourierGenerator {
    public static Courier getRandom() {
        String login = getAlphabeticRandom();
        String password = getAlphabeticRandom();
        String firstName = getAlphabeticRandom();
        return new Courier(login, password, firstName);
    }

    private static String getAlphabeticRandom() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
