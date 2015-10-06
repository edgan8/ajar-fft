package org.hazy;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by egan on 9/29/15.
 */
public class RelationListTest {
    @Test
    public void simpleIndexTest() {
        ArrayList<String> attrNames = new ArrayList<>(Arrays.asList("A", "B"));
        Relation tree = new RelationList(attrNames);

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
    @Test
    public void aggregateTest() {
        ArrayList<String> attrNames = new ArrayList<>(Arrays.asList("A", "B"));
        Relation rList = new RelationList(attrNames);

        Map<String, String> attrs = new TreeMap<>();
        for (int i = 0; i < 5; i++) {
            attrs.put("A", Integer.toString(i));
            for (int j = 0; j < 5; j++) {
                attrs.put("B", Integer.toString(j));
                Tuple t = new Tuple(attrs, new IntAnnot(1));
                rList.insert(t);
            }
        }
        Relation agList = rList.aggregate("B");
        List<Tuple> resultTuples = agList.getTuples();
        assertEquals(5, resultTuples.size());
        assertEquals("5", resultTuples.get(0).getAnnot().toString());
        assertFalse(agList.hasAttribute("B"));
        assertTrue(agList.hasAttribute("A"));
    }
}
