package org.hazy;

import java.util.Map;

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

    public static int convertFromBaseNTuple(int base, Map<String, String> baseNTuple) {
        int result = 0;
        for (String attrName : baseNTuple.keySet()) {
            IndexedAttr idxAttr = new IndexedAttr(attrName);
            int exp = idxAttr.idx;
            result += intPow(base, exp) * Integer.parseInt(baseNTuple.get(attrName));
        }
        return result;
    }

    public static int intPow(int n, int p) {
        int res = 1;
        for (int i = 0; i < p; i++) {
            res *= n;
        }
        return res;
    }
}
