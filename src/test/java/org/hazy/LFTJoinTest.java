package org.hazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Created by egan on 9/29/15.
 */
public class LFTJoinTest {
    @Test
    public void simpleIndexTest() {
        ArrayList<String> globalAttrOrdering = new ArrayList<>(Arrays.asList("Pet", "Name", "State"));
        ArrayList<Relation> relationsToJoin = new ArrayList<>();

        // Relation 1
        ArrayList<String> attrNames1 = new ArrayList<>(Arrays.asList("Name", "State"));
        Relation tree1 = new RelationTrie(attrNames1);
        Map<String, String> attrs = new TreeMap<>();
        attrs.put("State", "md");
        attrs.put("Name", "ed");
        Tuple t = new Tuple(attrs, new IntAnnot(3));
        tree1.insert(t);

        attrs = new TreeMap<>();
        attrs.put("State", "ca");
        attrs.put("Name", "mike");
        t = new Tuple(attrs, new IntAnnot(5));
        tree1.insert(t);

        // Relation 2
        ArrayList<String> attrNames2 = new ArrayList<>(Arrays.asList("Pet", "Name"));
        Relation tree2 = new RelationTrie(attrNames2);
        attrs = new TreeMap<>();
        attrs.put("Pet", "fido");
        attrs.put("Name", "ed");
        t = new Tuple(attrs, new IntAnnot(3));
        tree2.insert(t);

        attrs = new TreeMap<>();
        attrs.put("Pet", "paws");
        attrs.put("Name", "ed");
        t = new Tuple(attrs, new IntAnnot(5));
        tree2.insert(t);

        attrs = new TreeMap<>();
        attrs.put("Pet", "fido");
        attrs.put("Name", "mike");
        t = new Tuple(attrs, new IntAnnot(5));
        tree2.insert(t);

        relationsToJoin.add(tree1);
        relationsToJoin.add(tree2);
        try {
            LFTJoin join = new LFTJoin(globalAttrOrdering);
            Relation output = join.run(relationsToJoin);
            ArrayList<Tuple> results = output.getTuples();
            assertEquals(3, results.size());
            System.out.println(output.getTuples());
        } catch (Exception e) {
            fail("Thrown exception:"+e.getMessage());
        }
    }
}
