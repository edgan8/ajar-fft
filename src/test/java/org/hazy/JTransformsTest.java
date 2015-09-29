package org.hazy;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jtransforms.fft.DoubleFFT_1D;

import java.util.Arrays;

public class JTransformsTest {
    @Test
    public void canFFTZeroes() {
        int n = 8;
        double[] zeroes = new double[n];
        DoubleFFT_1D fft = new DoubleFFT_1D(n);
        fft.realForward(zeroes);
        assertEquals(zeroes[0], 0.0, .001);
    }

    @Test
    public void canFFTSingle() {
        int n = 8;
        double[] waveSamples = new double[2*n];
        for (int i = 0; i < n; i++) {
            waveSamples[i] = Math.cos(
                    (double) 2*Math.PI*i / n
            ) / n;
        }

        DoubleFFT_1D fft = new DoubleFFT_1D(n);
        fft.realForwardFull(waveSamples);

        // no constant component
        assertEquals(waveSamples[0], 0.0, .001);
        assertEquals(waveSamples[1], 0.0, .001);

        // Single frequency
        assertEquals(waveSamples[2*1], 0.5, .001);
        assertEquals(waveSamples[2*1+1], 0.0, .001);

        // Also need the negative frequency
        assertEquals(waveSamples[2*(n-1)], 0.5, .001);
        assertEquals(waveSamples[2*n-1], 0.0, .001);
    }
}
