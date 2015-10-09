package org.hazy;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by egan on 9/29/15.
 */
public class RelationTrieTest {
    RelationTrie trie;
    ArrayList<Tuple> tuples;

    @Before
    public void setUp() {
        ArrayList<String> attrNames = new ArrayList<>(Arrays.asList("A", "B"));
        trie = new RelationTrie(attrNames);
        tuples = new ArrayList<>();

        Map<String, String> attrs = new TreeMap<>();
        for (int i = 0; i < 5; i++) {
            attrs.put("A", Integer.toString(i));
            for (int j = i; j < 5; j++) {
                attrs.put("B", Integer.toString(j));
                Tuple t = new Tuple(attrs, new IntAnnot(i+j));
                tuples.add(t);
                trie.insert(t);
            }
        }
    }

    @Test
    public void indexTest() {
        assertTrue(trie.hasAttribute("A"));

        Map<String, String> attrs = new TreeMap<>();
        attrs.put("A", "1");
        Tuple indexTuple = new Tuple(attrs, null);
        AttrSet bSet = trie.index(indexTuple, "B");

        assertTrue(bSet.containsValue("1"));
        assertTrue(bSet.containsValue("4"));
        assertFalse(bSet.containsValue("0"));

        assertEquals("0", trie.getAnnotation(tuples.get(0)).toString());

        System.out.println(trie);
    }

    @Test
    public void basicTest() {
        assertEquals(15, trie.getTuples().size());
        assertTrue(trie.hasAttribute("A"));
        assertFalse(trie.hasAttribute("C"));
        assertEquals(2, trie.getAttributes().size());
    }

    @Test
    public void aggregateTest() {
        RelationTrie agRel = trie.aggregate("B");
        List<Tuple> resultTuples = agRel.getTuples();
        assertEquals(5, resultTuples.size());
        // 1 + 2 + 3 + 4
        assertEquals("10", resultTuples.get(0).getAnnot().toString());
    }
}
