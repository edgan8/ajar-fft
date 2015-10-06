package org.hazy;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by egan on 9/29/15.
 */
public class RelationTrieTest {
    @Test
    public void simpleIndexTest() {
        ArrayList<String> attrNames = new ArrayList<>(Arrays.asList("A", "B"));
        Relation trie = new RelationTrie(attrNames);

        Map<String, String> attrs = new TreeMap<>();
        attrs.put("B", "ed");
        attrs.put("A", "1");
        Tuple t = new Tuple(attrs, new IntAnnot(3));
        trie.insert(t);

        attrs = new TreeMap<>();
        attrs.put("B", "mike");
        attrs.put("A", "1");
        t = new Tuple(attrs, new IntAnnot(5));
        trie.insert(t);
        assertTrue(trie.hasAttribute("A"));

        attrs = new TreeMap<>();
        attrs.put("A", "1");
        Tuple indexTuple = new Tuple(attrs, new IntAnnot(7));
        AttrSet aSet = trie.index(indexTuple, "B");

        assertTrue(aSet.containsValue("mike"));
        assertTrue(aSet.containsValue("ed"));

        System.out.println(trie);
    }
}
