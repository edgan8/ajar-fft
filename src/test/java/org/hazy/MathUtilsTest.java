package org.hazy;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void convertFromTuple() {
        String x = "X";
        Map<String, String> t = new HashMap<>();
        t.put(new IndexedAttr(x, 0).toString(), "1");
        t.put(new IndexedAttr(x, 1).toString(), "0");
        t.put(new IndexedAttr(x, 2).toString(), "2");
        int val = MathUtils.convertFromBaseNTuple(3, t);
        assertEquals(19, val);
    }
}
