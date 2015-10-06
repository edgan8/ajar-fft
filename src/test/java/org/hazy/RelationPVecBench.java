package org.hazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by egan on 9/29/15.
 */
public class RelationPVecBench {
    @Test
    public void testGetTuple() {
        Annotation[] values = new IntAnnot[1000];
        for (int i = 0; i < 1000; i++) {
            values[i] = new IntAnnot(i);
        }
        Relation r = new RelationPVec("X", values, 10, 3);

        Map<String, String> attrs = new TreeMap<>();
        for (int i = 0; i < 3; i++) {
            IndexedAttr xAttr = new IndexedAttr("X", i);
            attrs.put(xAttr.toString(), "1");
        }
        Tuple t = new Tuple(attrs);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i ++) {
            r.getAnnotation(t);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Took: "+Long.toString(endTime - startTime));
    }
}
