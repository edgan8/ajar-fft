package org.hazy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilsTest {
    @Test
    public void convertSimple() {
        int n = 15;
        int[] digits = new int[10];
        int len = MathUtils.convertBaseN(n, 3, digits);
        assertEquals(3, len);
        assertEquals(0, digits[0]);
        assertEquals(2, digits[1]);
    }
}
