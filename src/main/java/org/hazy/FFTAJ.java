package org.hazy;

import java.util.ArrayList;
import java.util.Map;

/**
 * FFT implemented using optimal join plan
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
    public String[] getAttrOrdering() {
        String[] attrOrdering = new String[2*m];
        for (int i = 0; i < m; i++) {
            attrOrdering[i] = (new IndexedAttr(xVar, i)).toString();
            attrOrdering[m+i] = (new IndexedAttr(yVar, i)).toString();
        }
        return attrOrdering;
    }

    public double[] realForward (double[] b) throws Exception {
        ArrayList<Relation> toJoin = getFactors();
        toJoin.add(getInputRelation(b));

        LFTJoin join = new LFTJoin(getAttrOrdering());
        double[] outputNums = new double[n * 2];
        Relation outputRel = join.run(toJoin);
        for (int i = 0; i < m ; i++) {
            outputRel = outputRel.aggregate(new IndexedAttr(yVar, i).toString());
        }
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
