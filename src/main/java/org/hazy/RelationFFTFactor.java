package org.hazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by egan on 10/1/15.
 */
public class RelationFFTFactor implements Relation {
    // n = base ^ m
    public int base;
    public int m;
    public IndexedAttr var1, var2;
    public AttrSet var1AttrSet, var2AttrSet;
    String v1, v2;

    public RelationFFTFactor(
            int base,
            int m,
            IndexedAttr var1,
            IndexedAttr var2
    ) {
        this.base = base;
        this.m = m;
        this.var1 = var1;
        this.var2 = var2;
        this.v1 = var1.toString();
        this.v2 = var2.toString();
        TreeSet<String> attrValues = new TreeSet<>();
        for (int i = 0; i < base; i++) {
            attrValues.add(Integer.toString(i));
        }
        this.var1AttrSet = new AttrSet(var1.toString(), attrValues);
        this.var2AttrSet = new AttrSet(var2.toString(), attrValues);
    }

    public ArrayList<Tuple> getTuples() {
        throw new UnsupportedOperationException("Fat FFT Factor");
    }
    public ArrayList<String> getAttributes() {
        return new ArrayList<>(Arrays.asList(var1.toString(), var2.toString()));
    }

    public boolean hasAttribute(String attrStr) {
        if (!IndexedAttr.isIndexed(attrStr)) {
            return false;
        }
        return v1.equals(attrStr) || v2.equals(attrStr);
    }

    public AttrSet index(Tuple t, String attr) {
        if(attr.equals(v1)) {
            return var1AttrSet;
        } else if (attr.equals(v2)) {
            return var2AttrSet;
        }
        return null;
    }

    public Annotation getAnnotation(Tuple t) {
        int var1Value = Integer.parseInt(t.getAttrValue(v1));
        int var2Value = Integer.parseInt(t.getAttrValue(v2));
        double exponent = (2 * Math.PI * var1Value * var2Value) / (Math.pow(base, m - var1.idx - var2.idx));
        double factorReal = Math.cos(exponent);
        double factorIm = Math.sin(exponent);
        return new ComplexAnnot(factorReal, factorIm);
    }

    @Override
    public Relation select(Tuple t) {
        if (t.containsAttr(v1) && t.containsAttr(v2)) {
            return (new RelationEmpty(getAnnotation(t)));
        } else {
            return this;
        }
    }

    @Override
    public boolean supportsSelect() {
        return false;
    }

    public void insert(Tuple t) {
        throw new UnsupportedOperationException("Can't insert into FFT Factor");
    }

    @Override
    public Relation aggregate(String attr) {
        throw new UnsupportedOperationException("Can't aggregate into FFT Factor");
    }
}
