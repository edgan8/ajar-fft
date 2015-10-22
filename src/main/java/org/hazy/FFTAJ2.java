package org.hazy;

import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;

/**
 * FFT using AJAR-join access patterns
 * Created by egan on 9/27/15.
 */
public class FFTAJ2 {
    // n = 2^m
    private int n;
    private int m;
    double invPowerOfTwo[];
    double cosInvPowerOfTwo[];
    double sinInvPowerOfTwo[];

    public FFTAJ2(int n) {
        int powerOfTwo = Integer.highestOneBit(n);
        if (powerOfTwo != n) {
            powerOfTwo *= 2;
        }
        this.n = powerOfTwo;
        m = 31 - Integer.numberOfLeadingZeros(powerOfTwo);

        invPowerOfTwo = new double[m+1];
        cosInvPowerOfTwo = new double[m+1];
        sinInvPowerOfTwo = new double[m+1];
        for (int i = 0; i <= m; i++) {
            invPowerOfTwo[i] = 1.0/(1 << i);
            cosInvPowerOfTwo[i] = Math.cos(Math.PI*2*invPowerOfTwo[i]);
            sinInvPowerOfTwo[i] = Math.sin(Math.PI * 2 * invPowerOfTwo[i]);
        }


    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public double[] realForward (double[] b) {
        double[] bc = new double[2*n];
        double[] bn = new double[2*n];
        int mmask = (1 << m) - 1;

        for (int i = 0; i < b.length; i++){
            bc[2 * i] = b[i];
        }

        for (int ix = 0; ix < m; ix++) {
            /*
            System.out.println("ix:"+ix);
            */
            // x_i .. x_0, y_iyagg-1 .. y_0
            int iyAgg = m - ix - 1;
            // ib indexes into bn
            for (int ib = 0; ib < n; ib++) {
                int ib_ys = ib & ((1<<iyAgg) -1);
                int ib_xs = ib - ib_ys;

                int ibc_y0 = ((ib_xs << 1) & mmask) + ib_ys;
                int ibc_y1 = ibc_y0 + (1 << iyAgg);

                /*
                System.out.println("ib:"+ib);
                System.out.println("ibys:"+Integer.toBinaryString(ib_ys));
                System.out.println("ibxs:"+Integer.toBinaryString(ib_xs));
                System.out.println("iby0:"+Integer.toBinaryString(ibc_y0));
                System.out.println("iby1:"+Integer.toBinaryString(ibc_y1));
                */

                // x_ix == 1
                if (ib >= n/2) {
                    double partialExponent = getPartialExponent(ix, ib, iyAgg);

                    double exp_yAgg_0 = Math.PI*2*partialExponent;

                    double factor_yAgg_0_real = Math.cos(exp_yAgg_0);
                    double factor_yAgg_0_im = Math.sin(exp_yAgg_0);

                    double cos_yAgg = cosInvPowerOfTwo[m-ix-iyAgg];
                    double sin_yAgg = sinInvPowerOfTwo[m-ix-iyAgg];
                    double factor_yAgg_1_real = factor_yAgg_0_real*cos_yAgg - factor_yAgg_0_im*sin_yAgg;
                    double factor_yAgg_1_im = factor_yAgg_0_real*sin_yAgg + factor_yAgg_0_im*cos_yAgg;

                    /*
                    double exp_yAgg_1 = Math.PI*2*(partialExponent + 1.0/(1<<(m-ix-iyAgg)));
                    double factor_yAgg_1_real = Math.cos(exp_yAgg_1);
                    double factor_yAgg_1_im = Math.sin(exp_yAgg_1);
                    */
                    bn[2*ib] = bc[2*ibc_y0] * factor_yAgg_0_real - bc[2*ibc_y0+1] * factor_yAgg_0_im
                            + bc[2*ibc_y1] * factor_yAgg_1_real - bc[2*ibc_y1+1] * factor_yAgg_1_im;
                    bn[2*ib + 1] = bc[2*ibc_y0+1] * factor_yAgg_0_real + bc[2*ibc_y0] * factor_yAgg_0_im
                            + bc[2*ibc_y1+1] * factor_yAgg_1_real + bc[2*ibc_y1] * factor_yAgg_1_im;
                }
                // x_ix == 0
                else {
                    bn[2*ib] = bc[2*ibc_y0] + bc[2*ibc_y1];
                    bn[2*ib+1] = bc[2*ibc_y0+1] + bc[2*ibc_y1+1];
                }
            }
            double[] temp = bc;
            bc = bn;
            bn = temp;
        }
        return bc;
    }

    public double getPartialExponent(int ix, int y, int iyAgg) {
        double partialExponent = 0;
        double partialExponentAddend = invPowerOfTwo[m-ix-iyAgg+1];
        int yMask = 1 << (iyAgg-1);
        for (int iy = iyAgg - 1; iy >= 0; iy--) {
            if ((y & yMask) == yMask) {
                partialExponent += partialExponentAddend;
            }
            partialExponentAddend /= 2;
            y = y << 1;
        }
        return partialExponent;
    }
}
