package org.hazy;

import org.jtransforms.fft.DoubleFFT_1D;
import pl.edu.icm.jlargearrays.ConcurrencyUtils;

import java.util.Random;

public class JTransformBench {
    public static void main(String[] args) throws Exception {
        canFFTLarge();
        System.exit(0);
    }

    public static void canFFTLarge() throws Exception {
        int n = 1 << 26;

        Random r = new Random();

        double[] waveSamples = new double[2*n];
        for (int i = 0; i < n; i++) {
            waveSamples[2*i] = Math.cos(
                    (double) 2*Math.PI*i / n
            ) / n;
            /*
            waveSamples[2*i] = r.nextDouble();
            */
        }
        ConcurrencyUtils.setNumberOfThreads(1);

        DoubleFFT_1D fft = new DoubleFFT_1D(n);
        long startTime = System.currentTimeMillis();

        fft.complexForward(waveSamples);
        double[] freqSamples = waveSamples;

        long endTime = System.currentTimeMillis();
        System.out.println("Took: " + Long.toString(endTime - startTime));

        System.out.println("First 10 freqs:");
        for (int i = 0; i < 10; i++) {
            System.out.println(freqSamples[2*i]+":"+freqSamples[2*i+1]);
        }
    }
}
