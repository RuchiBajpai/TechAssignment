package com.ruchi.utils;

import java.util.Random;

public class NumberUtil {
    public static int getRandomNatId(){
        Random random = new Random();
        return random.nextInt(10000000);
    }
}
