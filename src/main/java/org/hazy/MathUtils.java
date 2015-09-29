package org.hazy;

/**
 * Created by egan on 9/27/15.
 */
public class MathUtils {
    /**
     * Writes out the digits in reverse order. num = sum base^i * out^i
     * @param num integer to convert
     * @param base exponential base
     * @param digitsOut pre-allocated array to write output
     * @return number of digits
     */
    public static int convertBaseN(int num, int base, int[] digitsOut) {
        int i = 0;
        while (num > 0) {
            digitsOut[i] = num % base;
            num /= base;
            i++;
        }
        return i;
    }
}
