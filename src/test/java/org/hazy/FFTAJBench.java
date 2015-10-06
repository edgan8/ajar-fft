package org.hazy;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class FFTAJBench {
    @Test
    public void canFFTLarge() throws Exception {
        int p = 10;
        int m = 4;
        int n = 10000;
        double[] waveSamples = new double[n];
        for (int i = 0; i < n; i++) {
            Random r = new Random();
            waveSamples[i] = r.nextDouble();
            waveSamples[i] = Math.cos(
                    (double) 2*Math.PI*i / n
            ) / n;
        }

        FFTAJ fft = new FFTAJ(p, m);
        long startTime = System.currentTimeMillis();

        double[] freqSamples = fft.realForward(waveSamples);

        long endTime = System.currentTimeMillis();
        System.out.println("Took: "+Long.toString(endTime - startTime));

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
