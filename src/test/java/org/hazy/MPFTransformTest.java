package org.hazy;

import org.jtransforms.fft.DoubleFFT_1D;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MPFTransformTest {
    @Test
    public void canFFTZeroes() {
        int n = 8;
        double[] zeroes = new double[n];
        MPFTransform fft = new MPFTransform(n);
        double[] freqSamples = fft.realForward(zeroes);
        assertEquals(0.0, freqSamples[0], .001);
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

        MPFTransform fft = new MPFTransform(n);
        double[] freqSamples = fft.realForward(waveSamples);

        // no constant component
        assertEquals(0.0, freqSamples[0], 1e-10);
        assertEquals(0.0, freqSamples[1], 1e-10);

        // Single frequency
        assertEquals(0.5, freqSamples[2], 1e-10);
        assertEquals(0.0, freqSamples[3], 1e-10);

        // Also need the negative frequency
        assertEquals(0.5, freqSamples[2*(n-1)], 1e-10);
        assertEquals(0.0, freqSamples[2*n-1], 1e-10);

        // Every other frequency zeroed
        assertEquals(0.0, freqSamples[4], 1e-10);
        assertEquals(0.0, freqSamples[5], 1e-10);
    }
}
