package org.hazy;

import java.util.Random;

public class FFTAJ2Bench {
    public static void main(String[] args) throws Exception {
        canFFTLarge();
        System.exit(0);
    }

    public static void canFFTLarge() throws Exception {
        int n = 4000000;
        double[] waveSamples = new double[n];
        for (int i = 0; i < n; i++) {
            Random r = new Random();
            waveSamples[i] = r.nextDouble();
            waveSamples[i] = Math.cos(
                    (double) 2*Math.PI*i / n
            ) / n;
        }

        FFTAJ2 fft = new FFTAJ2(n);
        long startTime = System.currentTimeMillis();

        double[] freqSamples = fft.realForward(waveSamples);

        long endTime = System.currentTimeMillis();
        System.out.println("First 10 freqs:");
        for (int i = 0; i < 10; i++) {
            System.out.println(freqSamples[2*i]+":"+freqSamples[2*i+1]);
        }
        System.out.println("Took: "+Long.toString(endTime - startTime));
    }
}
