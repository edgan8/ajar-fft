package org.hazy;

/**
 * Created by egan on 9/29/15.
 */
public class IntAnnot implements Annotation{
    private final int val;
    public IntAnnot(int x) {
        val = x;
    }
    public int getVal() {
        return val;
    }
    public IntAnnot zero() {
        return new IntAnnot(0);
    }
    public IntAnnot one() {
        return new IntAnnot(1);
    }
    public IntAnnot add(Annotation b) {
        return new IntAnnot(val + ((IntAnnot)b).val);
    }
    public IntAnnot mult(Annotation b) {
        return new IntAnnot(val * ((IntAnnot)b).val);
    }
    public IntAnnot copy() {
        return new IntAnnot(val);
    }
    public String toString() {
        return Integer.toString(val);
    }
}
