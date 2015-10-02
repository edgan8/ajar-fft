package org.hazy;

/**
 * Created by egan on 9/29/15.
 */
public class ComplexAnnot implements Annotation{
    public final double re;
    public final double im;
    public ComplexAnnot(double x, double y) {
        re = x;
        im = y;
    }
    public ComplexAnnot zero() {
        return new ComplexAnnot(0, 0);
    }
    public ComplexAnnot one() {
        return new ComplexAnnot(1, 0);
    }
    public ComplexAnnot add(Annotation b) {
        return new ComplexAnnot(
                re + ((ComplexAnnot)b).re,
                im + ((ComplexAnnot)b).im);
    }
    public ComplexAnnot mult(Annotation b) {
        double bre = ((ComplexAnnot)b).re;
        double bim = ((ComplexAnnot)b).im;
        return new ComplexAnnot(
                re * bre - im * bim,
                re * bim + im * bre
        );

    }
    public ComplexAnnot copy() {
        return new ComplexAnnot(re, im);
    }
    public String toString() {
        return Double.toString(re)+"+"+Double.toString(im)+"i";
    }

}
