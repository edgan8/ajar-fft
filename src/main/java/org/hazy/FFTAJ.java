package org.hazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * FFT implemented using hardcoded optimal join plan
 * Created by egan on 10/2/15.
 */
public class FFTAJ {
    public int p;
    public int m;
    public int n;
    static final String xVar = "X";
    static final String yVar = "Y";

    public FFTAJ(int p, int m) {
        this.p = p;
        this.m = m;
        this.n = MathUtils.intPow(p, m);
    }

    public Relation getInputRelation(double [] b) {
        Annotation[] complexAnnots = new ComplexAnnot[b.length];
        for (int i = 0; i < b.length; i++) {
            complexAnnots[i] = new ComplexAnnot(b[i], 0.0);
        }
        Relation bRel = new RelationPVec(yVar, complexAnnots, p, m);
        return bRel;
    }

    public ArrayList<Relation> getFactors() {
        ArrayList<Relation> fftFactors = new ArrayList<Relation>();
        for (int j=0; j < m; j++) {
            for (int k=0; k < m - j; k++) {
                fftFactors.add(new RelationFFTFactor(
                        p,
                        m,
                        new IndexedAttr(xVar, j),
                        new IndexedAttr(yVar, k)));
            }
        }
        return fftFactors;
    }

    /**
     * TODO: come up with better ordering
     * @return good attribute ordering to performing join
     */
    public ArrayList<String> getAttrOrdering() {
        ArrayList<String> attrOrdering = new ArrayList<>(2*m);
        for (int i = 0; i < m; i++) {
            attrOrdering.add((new IndexedAttr(xVar, i)).toString());
        }
        for (int i = 0; i < m; i++) {
            attrOrdering.add((new IndexedAttr(yVar, i)).toString());
        }
        return attrOrdering;
    }

    public double[] realForward (double[] b) throws Exception {
        ArrayList<Relation> toJoin = getFactors();
        Relation bRel = getInputRelation(b);
        toJoin.add(bRel);

        // join all relation involving y_i and project it out for each y_i
        for (int i = m - 1; i >= 0; i--) {
            IndexedAttr aggAttr = new IndexedAttr(yVar, i);
            ArrayList<Relation> stepJoin = new ArrayList<>();
            Set<String> stepAttrNames = new HashSet<String>();
            for (Relation r : toJoin) {
                if (r.hasAttribute(aggAttr.toString())) {
                    stepJoin.add(r);
                    stepAttrNames.addAll(r.getAttributes());
                }
            }
            /*
            System.out.println(aggAttr.toString());
            System.out.println(stepJoin);
            System.out.println(stepAttrNames);
            */

            ArrayList<String> stepAttrOrdering = getAttrOrdering();
            stepAttrOrdering.retainAll(stepAttrNames);

            LFTJoin join = new LFTJoin(stepAttrOrdering);
            Relation stepOutput = join.run(stepJoin).aggregate(aggAttr.toString());
            for (Relation r : stepJoin) {
                boolean removed = toJoin.remove(r);
                if (!removed) {
                    throw (new Exception("could not find relation"));
                }
            }
            toJoin.add(stepOutput);
        }
        if (toJoin.size() != 1) throw new AssertionError("more than 1 rel left");
        Relation outputRel = toJoin.get(0);

        double[] outputNums = new double[n * 2];
        ArrayList<Tuple> outputTuples = outputRel.getTuples();
        for (Tuple t : outputTuples) {
            int xVecIdx = MathUtils.convertFromBaseNTuple(p, t.getAttrs());
            ComplexAnnot xVal = (ComplexAnnot)outputRel.getAnnotation(t);
            outputNums[2*xVecIdx] = xVal.re;
            outputNums[2*xVecIdx + 1] = xVal.im;
        }
        return outputNums;
    }
}
