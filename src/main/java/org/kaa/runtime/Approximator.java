package org.kaa.runtime;

import java.math.BigDecimal;

/**
 * @author sbt-kopilov-aa
 * @version 5.0
 * @since 03/12/2014
 * собственная реализация работы с большими числами
 */
public class Approximator {

    public Approximator() {
    }

    protected String getDegreeOfTwo(int degree) {
        BigDecimal bigDecimal = new BigDecimal("2");
        BigDecimal mult = BigDecimal.valueOf(2);
        String value = "2";
        for (int i = 2; i <= degree; i++) {
            value = multiplyStep(value);
            mult = mult.multiply(bigDecimal);
        }
        System.out.println(mult);

        return value;
    }

    //умножает число на 2
    protected String multiplyStep(String source) {
        String target = "";
        boolean increase = false;

        for (int i = source.length()-1; i >= 0 ; i--) {
            int sourceDigit = Integer.valueOf(String.valueOf(source.charAt(i)));
            int targetDigit = sourceDigit * 2;
            if (increase) {
                targetDigit++;
            }
            increase = targetDigit >= 10;
            if (increase) {
                targetDigit -= 10;
            }
            target = String.valueOf(targetDigit) + target;
        }
        if (increase) {
            target = "1" + target;
        }

        return target;
    }

}
