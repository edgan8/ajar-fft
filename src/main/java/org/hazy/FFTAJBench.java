package org.hazy;

import java.util.Arrays;
import java.util.Random;

public class FFTAJBench {
    public static void main(String[] args) throws Exception {
        canFFTLarge();
        System.exit(0);
    }

    public static void canFFTLarge() throws Exception {
        int p = 10;
        int m = 6;
        int n = 1000000;
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
        System.out.println("First 10 feqs:");
        for (int i = 0; i < 10; i++) {
            System.out.println(freqSamples[2*i]+":"+freqSamples[2*i+1]);
        }
        System.out.println("Took: "+Long.toString(endTime - startTime));
    }
}
