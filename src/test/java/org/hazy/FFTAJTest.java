package org.hazy;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FFTAJTest {
    @Test
    public void canFFTZeroes() throws Exception {
        int p = 2;
        int m = 3;
        int n = 8;
        double[] zeroes = new double[n];
        FFTAJ fft = new FFTAJ(p, m);
        double[] freqSamples = fft.realForward(zeroes);
        assertEquals(0.0, freqSamples[0], .001);
    }

    @Test
    public void canFFTSingle() throws Exception {
        int p = 2;
        int m = 3;
        int n = 8;
        double[] waveSamples = new double[n];
        for (int i = 0; i < n; i++) {
            waveSamples[i] = Math.cos(
                    (double) 2*Math.PI*i / n
            ) / n;
        }

        FFTAJ fft = new FFTAJ(p, m);
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
