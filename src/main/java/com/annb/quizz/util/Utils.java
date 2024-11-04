package com.annb.quizz.util;

import java.util.Random;

public class Utils {

    public static String generateUniqueCode() {
        Random random = new Random();
        // Generate a random number between 10000 and 99999
        int randomNum = 10000 + random.nextInt(90000);
        return String.valueOf(randomNum);
    }
}
