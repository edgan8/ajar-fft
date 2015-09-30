package org.hazy;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by egan on 9/29/15.
 */
public class AttrSetTest {
    @Test
    public void simpleIntersect() {
        SortedMap<String, Annotation> values1 = new TreeMap<>();
        values1.put("cat", new IntAnnot(2));
        values1.put("dog", new IntAnnot(3));
        values1.put("mouse", new IntAnnot(4));
        AttrSet aSet1 = new AttrSet("Animals", values1);

        SortedMap<String, Annotation> values2 = new TreeMap<>();
        values2.put("cat", new IntAnnot(3));
        values2.put("dog", null);
        AttrSet aSet2 = new AttrSet("Animals", values2);

        aSet1.intersect(aSet2);
        assertEquals("6", aSet1.getAnnotation("cat").toString());
        assertEquals("3", aSet1.getAnnotation("dog").toString());
        assertFalse(aSet1.containsValue("mouse"));
    }
}
