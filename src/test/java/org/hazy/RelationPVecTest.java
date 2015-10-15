package org.hazy;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by egan on 9/29/15.
 */
public class RelationPVecTest {
    @Test
    public void testGetTuple() {
        Annotation[] values = new IntAnnot[8];
        for (int i = 0; i < 8; i++) {
            values[i] = new IntAnnot(i);
        }
        Relation r = new RelationPVec("X", values, 2, 3);
        ArrayList<Tuple> output = r.getTuples();
        System.out.println(output);
        assertEquals(8, output.size());
        assertTrue(r.hasAttribute((new IndexedAttr("X", 1)).toString()));
        assertTrue(r.hasAttribute((new IndexedAttr("X", 2)).toString()));
    }
}
