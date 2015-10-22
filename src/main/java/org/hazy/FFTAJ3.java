package org.hazy;

/**
 * FFT using realistic AJAR-join access patterns
 * Created by egan on 9/27/15.
 */
public class FFTAJ3 {
    // n = 2^m
    private int n;
    private int m;
    private int mmask;

    public double[] bc;
    public double[] bn;
    double cosInvPowerOfTwo[];
    double sinInvPowerOfTwo[];

    public FFTAJ3(int size) {
        int powerOfTwo = Integer.highestOneBit(size);
        if (powerOfTwo != size) {
            powerOfTwo *= 2;
        }
        this.n = powerOfTwo;
        m = 31 - Integer.numberOfLeadingZeros(powerOfTwo);
        mmask = (1 << m) - 1;

        cosInvPowerOfTwo = new double[m+1];
        sinInvPowerOfTwo = new double[m+1];
        for (int i = 0; i <= m; i++) {
            double angle = Math.PI*2/(1 << i);
            cosInvPowerOfTwo[i] = Math.cos(angle);
            sinInvPowerOfTwo[i] = Math.sin(angle);
        }

        bc = new double[2*n];
        bn = new double[2*n];
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    /**
     * Simple recursive loop with X_ix = 0 since all the relations have annotation = 1 then
     * @param depth indexes into the current attribute in the attribute ordering
     * @param outputIdx the tuple of attribute values, used to index into the input array
     */
    public void recurseJoinXIsZero(int depth, int outputIdx) {
        if (depth == m) {
            // s1: yAgg = 0, s2: yAgg = 1
            // These bit manipulations just convert our tuple involving the
            // new x but not the aggregated y to one not including the new x but
            // including the aggregated y.
            int s1 = (2*outputIdx) & mmask;
            int s2 = s1 + 1;
            bn[2*outputIdx] = bc[2*s1] + bc[2*s2];
            bn[2*outputIdx+1] = bc[2*s1+1] + bc[2*s2+1];
            return;
        }
        recurseJoinXIsZero(depth + 1, 2*outputIdx);
        recurseJoinXIsZero(depth + 1, 2*outputIdx + 1);
        return;
    }

    public void recurseJoinXIsOne(int ix, int depth, int outputIdx, double annotRe, double annotIm) {
        if (depth <= ix) {
            recurseJoinXIsOne(ix, depth + 1, 2 * outputIdx, annotRe, annotIm);
            recurseJoinXIsOne(ix, depth + 1, 2 * outputIdx + 1, annotRe, annotIm);
            return;
        } else {
            int iy = depth - ix - 1;
            // We need to multiply the current annotation whenever we have a Y_iy = 1
            double cosY = cosInvPowerOfTwo[m-ix-iy];
            double sinY = sinInvPowerOfTwo[m-ix-iy];

            double newAnnotRe = annotRe*cosY - annotIm*sinY;
            double newAnnotIm = annotRe*sinY + annotIm*cosY;
            if (depth == m ) {
                // s1: yAgg = 0, s2: yAgg = 1
                int s1 = (2*outputIdx) & mmask;
                int s2 = s1 + 1;
                bn[2*outputIdx] =
                        bc[2*s1]*annotRe - bc[2*s1+1]*annotIm
                                + bc[2*s2]*newAnnotRe - bc[2*s2+1]*newAnnotIm;
                bn[2*outputIdx+1] =
                        bc[2*s1]*annotIm + bc[2*s1+1]*annotRe
                                + bc[2*s2]*newAnnotIm + bc[2*s2+1]*newAnnotRe;
                return;
            } else {
                recurseJoinXIsOne(ix, depth + 1, 2*outputIdx, annotRe, annotIm);
                recurseJoinXIsOne(ix, depth + 1, 2*outputIdx + 1, newAnnotRe, newAnnotIm);
            }
        }
    }

    /**
     * ATTRIBUTE ORDERING:
     * x_ix, ..., x0, y0, ..., y_iAgg
     * x_ix is at depth 0
     * @param b input array of reals
     * @return array of complex freqs
     */
    public double[] realForward (double[] b) {
        // We need to reverse the input since we lay out the ys backwards
        for (int i = 0; i < b.length; i++){
            int revIndex = Integer.reverse(i) >>> (32-m);
            if (revIndex < b.length) {
                bc[2 * i] = b[revIndex];
            } else {
                bc[2*i] = 0;
            }
        }
        // bc is initially the user input but in bit reversed order since the
        // Ys are reversed in the attribute order.
        // In later stages bc is the output from the previous bag.

        for (int ix = 0; ix < m; ix++) {
            recurseJoinXIsZero(1, 0);
            recurseJoinXIsOne(ix, 1, 1, 1.0, 0.0);

            // swap the input and output matrices to avoid reallocating
            double[] temp = bc;
            bc = bn;
            bn = temp;
        }
        return bc;
    }
}
