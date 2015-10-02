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
        String[] globalAttrOrdering = {"Pet", "Name", "State"};
        LFTJoin join = new LFTJoin(globalAttrOrdering);
        ArrayList<RelationTrie> relationsToJoin = new ArrayList<>();

        // Relation 1
        String[] attrOrdering1 = {"Name", "State"};
        RelationTrie tree1 = new RelationTreeImpl(attrOrdering1);
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
        String[] attrOrdering2 = {"Pet", "Name"};
        RelationTrie tree2 = new RelationTreeImpl(attrOrdering2);
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
            RelationTrie output = join.run(relationsToJoin);
            ArrayList<Tuple> results = output.getTuples();
            assertEquals(3, results.size());
            System.out.println(output.getTuples());
        } catch (Exception e) {
            fail("Thrown exception:"+e.getMessage());
        }
    }
}
