package org.hazy;

import org.jtransforms.fft.DoubleFFT_1D;
import pl.edu.icm.jlargearrays.ConcurrencyUtils;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class JTransformBench {
    @Test
    public void canFFTLarge() throws Exception {
        int n = 4000000;
        double[] waveSamples = new double[2*n];
        for (int i = 0; i < n; i++) {
            Random r = new Random();
            waveSamples[i] = r.nextDouble();
            waveSamples[i] = Math.cos(
                    (double) 2*Math.PI*i / n
            ) / n;
        }
        ConcurrencyUtils.setNumberOfThreads(1);

        DoubleFFT_1D fft = new DoubleFFT_1D(n);
        long startTime = System.currentTimeMillis();

        fft.realForwardFull(waveSamples);
        double[] freqSamples = waveSamples;

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
