package org.hazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by egan on 9/29/15.
 */
public class RelationFFTFactorTest {
    @Test
    public void testSimple() {
        int base = 3;
        int m = 2;
        IndexedAttr xAttr = new IndexedAttr("X", 0);
        IndexedAttr yAttr = new IndexedAttr("Y", 1);
        RelationFFTFactor fftFactor = new RelationFFTFactor(base, 2, xAttr, yAttr);
        assertTrue(fftFactor.hasAttribute(xAttr.toString()));

        Map<String, String> attrs = new TreeMap<>();
        attrs.put(xAttr.toString(), "1");
        AttrSet yValues = fftFactor.index(new Tuple(attrs, null), yAttr. toString());
        assertEquals(base, yValues.getValues().size());
        assertTrue(yValues.containsValue("2"));

        attrs = new TreeMap<>();
        attrs.put(xAttr.toString(), "0");
        attrs.put(yAttr.toString(), "1");
        ComplexAnnot factorValue = (ComplexAnnot)fftFactor.getAnnotation(new Tuple(attrs, null));
        assertEquals(1.0, factorValue.re, 1e-7);
        assertEquals(0.0, factorValue.im, 1e-7);

        attrs = new TreeMap<>();
        attrs.put(xAttr.toString(), "1");
        attrs.put(yAttr.toString(), "1");
        // should evaluate to e^(2pi*i/3)
        factorValue = (ComplexAnnot)fftFactor.getAnnotation(new Tuple(attrs, null));
        assertEquals(-.5, factorValue.re, 1e-7);
        assertEquals(Math.sqrt(3)/2, factorValue.im, 1e-7);
        // Uncomment when getTuples implemented
        /*
        List<Tuple> outputTuples = fftFactor.getTuples();
        assertEquals(base * base, outputTuples.size());
        System.out.println(outputTuples);
        */
    }
}
