package org.hazy;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by egan on 9/29/15.
 */
public class RelationTreeImplTest {
    @Test
    public void simpleIndexTest() {
        String[] attrOrdering = {"A", "B"};
        RelationTrie tree = new RelationTreeImpl(attrOrdering);

        Map<String, String> attrs = new TreeMap<>();
        attrs.put("B", "ed");
        attrs.put("A", "1");
        Tuple t = new Tuple(attrs, new IntAnnot(3));
        tree.insert(t);

        attrs = new TreeMap<>();
        attrs.put("B", "mike");
        attrs.put("A", "1");
        t = new Tuple(attrs, new IntAnnot(5));
        tree.insert(t);
        assertTrue(tree.hasAttribute("A"));

        attrs = new TreeMap<>();
        attrs.put("A", "1");
        Tuple indexTuple = new Tuple(attrs, new IntAnnot(7));
        AttrSet aSet = tree.index(indexTuple, "B");

        assertTrue(aSet.containsValue("mike"));
        assertTrue(aSet.containsValue("ed"));
    }
}
