package org.hazy;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by egan on 9/29/15.
 */
public class AttrSetTest {
    @Test
    public void simpleIntersect() {
        SortedSet<String> values1 = new TreeSet<>();
        values1.add("cat");
        values1.add("dog");
        values1.add("mouse");
        AttrSet aSet1 = new AttrSet("Animals", values1);

        SortedSet<String> values2 = new TreeSet<>();
        values2.add("cat");
        values2.add("dog");
        AttrSet aSet2 = new AttrSet("Animals", values2);

        SortedSet<String> iSet = new TreeSet<>(values1);
        aSet2.filter(iSet);
        assertTrue(iSet.contains("cat"));
        assertFalse(iSet.contains("mouse"));
    }
}
