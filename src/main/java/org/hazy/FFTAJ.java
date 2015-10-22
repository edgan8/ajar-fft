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

        ArrayList<Relation> stepJoin = new ArrayList<>();
        Set<String> stepAttrNames = new HashSet<String>();
        stepJoin.add(bRel);
        stepAttrNames.addAll(bRel.getAttributes());

        // Join input (previous output) with all relations involving xi and then project out y_(m-i)
        for (int i = 0; i < m; i++) {
            String xVarName = (new IndexedAttr(xVar, i)).toString();
            IndexedAttr aggAttr = new IndexedAttr(yVar, m-i-1);
            for (Relation r : toJoin) {
                if (r.hasAttribute(xVarName)) {
                    stepJoin.add(r);
                    stepAttrNames.addAll(r.getAttributes());
                }
            }

            System.out.println(aggAttr.toString());
            // System.out.println(stepJoin);
            System.out.println(stepAttrNames);


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
            stepJoin = new ArrayList<>();
            stepAttrNames = new HashSet<>();
            toJoin.add(stepOutput);
            stepJoin.add(stepOutput);
            stepAttrNames.addAll(stepOutput.getAttributes());
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
