package org.hazy;

import org.apache.commons.math3.complex.Complex;

/**
 * FFT implemented using the tranform described in MPF naively
 * Created by egan on 9/27/15.
 */
public class FFTAJ2 {
    // n = 2^m
    private int n;
    private int m;

    public FFTAJ2(int n) {
        int powerOfTwo = Integer.highestOneBit(n);
        if (powerOfTwo != n) {
            powerOfTwo *= 2;
        }
        m = 31 - Integer.numberOfLeadingZeros(powerOfTwo);
        this.n = powerOfTwo;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public double[] realForward (double[] b) {
        double[] freqs = new double[2*n];
        for (int x = 0; x < n; x++) {
            Complex freq = calcFreqTerm(b, x);
            freqs[2*x] = freq.getReal();
            freqs[2*x+1] = freq.getImaginary();
        }
        return freqs;
    }

    public Complex calcFreqTerm(double[] b, int x) {
        int[] xdigits = new int[m];
        int[] ydigits = new int[m];
        MathUtils.convertBaseN(x, 2, xdigits);
        Complex sum = new Complex(0);

        for (int y = 0; y < n; y++) {
            MathUtils.convertBaseN(y, 2, ydigits);
            Complex prod = new Complex(b[y]);
            for (int j = 0; j < m; j++) {
                for (int k = 0; k + j < m; k++) {
                    int xj = xdigits[j];
                    int yk = ydigits[k];
                    if (xj == 1 && yk == 1) {
                        double exponent = (2 * Math.PI) / (Math.pow(2.0, m - j - k));
                        double factorReal = Math.cos(exponent);
                        double factorIm = Math.sin(exponent);

                        prod = prod.multiply(new Complex(factorReal, factorIm));
                    }
                }
            }
            sum = sum.add(prod);
        }

        return sum;
    }
}
