package org.hazy;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Created by egan on 9/29/15.
 */
public class TupleTest {
    @Test
    public void simpleTuple() {
        Map<String, String> attrs = new TreeMap<>();
        attrs.put("name", "ed");
        attrs.put("val", "1");
        Tuple t = new Tuple(attrs, null);
        t = t.append("place", "stanford");
        t = t.append("fish", "gold");
        t.setAnnot(new IntAnnot(8));
        assertEquals(8, ((IntAnnot)t.getAnnot()).getVal());
        assertTrue(t.containsAttr("place"));
        assertTrue(t.containsAttr("name"));
    }

    @Test
    public void matchTest() {
        Map<String, String> attrs = new TreeMap<>();
        attrs.put("name", "ed");
        attrs.put("val", "1");
        Tuple t = new Tuple(attrs, null);

        attrs = new TreeMap<>();
        attrs.put("blah", "x");
        Tuple t2 = new Tuple(attrs, null);
        assertTrue(t.match(t2));
        assertTrue(t2.match(t));

        attrs = new TreeMap<>();
        attrs.put("name", "mike");
        Tuple t3 = new Tuple(attrs, null);
        assertFalse(t3.match(t));
    }

    @Test
    public void equalAttrsTest() {
        Map<String, String> attrs = new TreeMap<>();
        attrs.put("name", "ed");
        attrs.put("val", "1");
        Tuple t = new Tuple(attrs, null);

        attrs = new TreeMap<>();
        attrs.put("name", "ed");
        Tuple t2 = new Tuple(attrs, null);

        assertTrue(t.equalAttrs(t));
        assertFalse(t.equalAttrs(t2));
    }
}
