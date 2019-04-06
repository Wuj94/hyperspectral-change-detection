package Business;

import java.util.Arrays;

/**
 * Calculates silhouette.
 */
public class SilhouetteCalculator {
    private final Double[][] spectralAttrs;
    private final Integer[] clusterId;
    private final int nClusters;

    private class ParSilhouetteCalc extends Thread{
        private int start;
        private int end;
        private double[] silhouette;

        ParSilhouetteCalc(int start, int end){
            this.start = start;
            this.end = end;
            silhouette = new double[end - start];
        }

        @Override
        public void run() {
            for(int i = start; i < end; ++i) {
                int idx = i - start;
                //choose an example e
                Double[] e1 = spectralAttrs[i];
                double eWithin = 0.0;
                int eWithinNum = -1;
                double[] eBetween = new double[nClusters];
                int[] eBetweenNum = new int[nClusters];

                for (int j = 0; j < spectralAttrs.length; ++j) {
                    //compute within and beetween distance for e
                    Double[] e2 = spectralAttrs[j];
                    if (clusterId[i] == clusterId[j]) {
                        eWithin += distance(e1, e2);
                        ++eWithinNum;
                    } else {
                        eBetween[clusterId[j] - 1] += distance(e1, e2);
                        ++eBetweenNum[clusterId[j] - 1];
                    }
                }
                eWithin = eWithin / eWithinNum;
                for (int k = 0; k < eBetween.length; ++k)
                    eBetween[k] = eBetween[k] / eBetweenNum[k]; //contains a nan but I don't care
                double finalBetween = min(eBetween); //this method never returns the nan value
                silhouette[idx] = (finalBetween - eWithin) / Math.max(finalBetween, eWithin);
            }
        }

        /**
         * @param exId example id
         * @return silhouette for exId
         */
        double getSilhouette(int exId){
            return silhouette[exId];
        }
    }

    /**
     * @param spectralAttributes a matrix where each row is an example
     * @param belongingCluster a vector of the same length of spectralAttributes' row length having belonging cluster/class of
     *                         each example, represented by any row
     * @throws SilhouetteException.EmptyDatasetException
     * @throws SilhouetteException.DifferentLengthException
     */
    public SilhouetteCalculator(Double[][] spectralAttributes, Integer[] belongingCluster) throws SilhouetteException.EmptyDatasetException, SilhouetteException.DifferentLengthException {
        if(spectralAttributes.length == 0 || belongingCluster.length == 0)
            throw new SilhouetteException.EmptyDatasetException("Empty data set.");
        if(spectralAttributes.length != belongingCluster.length)
            throw new SilhouetteException.DifferentLengthException("The arguments must have the same length.");

        nClusters = Arrays.stream(belongingCluster).distinct().toArray().length;
        spectralAttrs = spectralAttributes;
        clusterId = belongingCluster;
    }

    /**
     * Start calculation
     * @return
     */
    public double silhouette() {
        double silhouette = 0.0;
        for(int i = 0; i < spectralAttrs.length; ++i){
            //choose an example e
            Double[] e1 = spectralAttrs[i];
            double eWithin = 0.0;
            int eWithinNum = -1;
            double[] eBetween = new double[nClusters];
            int[] eBetweenNum = new int[nClusters];

            for(int j = 0; j < spectralAttrs.length; ++j){
                //compute within and beetween distance for e
                Double[] e2 = spectralAttrs[j];
                if(clusterId[i] == clusterId[j]){
                    eWithin += distance(e1, e2);
                    ++eWithinNum;
                }else{
                    eBetween[clusterId[j]-1] += distance(e1, e2);
                    ++eBetweenNum[clusterId[j]-1];
                }
            }
            eWithin = eWithin / eWithinNum;
            for(int k = 0; k < eBetween.length; ++k)
                eBetween[k] = eBetween[k] / eBetweenNum[k]; //contains a nan but I don't care
            double finalBetween = min(eBetween); //this method never returns the nan value
            silhouette += (finalBetween - eWithin) / Math.max(finalBetween, eWithin);
        }

        return silhouette / spectralAttrs.length;
    }

    /**
     * Start calculation in parallel threads
     * @param threads number of thread to use for this calculation. if threads < 2 a serial
     *                calculation will be carried out.
     */
    public double parallelSihouette(int threads) throws InterruptedException {
        if(spectralAttrs.length / threads < 100)
            return silhouette();
        else{
            int[] startIdxs = new int[threads]; //start indexes of the array slice
            int[] endIdxs = new int[threads]; //end (not included) indexes of the array slice
            //invariance: startIdxs.length == endIdxs.length

            int exPerThread = spectralAttrs.length / threads;
            startIdxs[0] = 0;
            for(int i = 1; i < startIdxs.length; ++i){
                startIdxs[i] = startIdxs[i-1] + exPerThread;
                endIdxs[i - 1] = startIdxs[i];
            }
            endIdxs[startIdxs.length - 1] = spectralAttrs.length;

            ParSilhouetteCalc[] calcs = new ParSilhouetteCalc[threads];
            for(int i = 0; i < threads; ++i){
                calcs[i] = new ParSilhouetteCalc(startIdxs[i], endIdxs[i]);
                calcs[i].start();
            }

            for(ParSilhouetteCalc calc : calcs)
                calc.join();

            double sil = 0.0;
            for(int i = 0; i < spectralAttrs.length; ++i){
                int calcIdx = 0;

                for(int k = 0; i > endIdxs[k] -1; ++k)
                    ++calcIdx;

                int exIdx = 0;
                exIdx = i - calcIdx*exPerThread;

                sil += calcs[calcIdx].getSilhouette(exIdx);
            }
            return  sil / spectralAttrs.length;
        }
    }

    /**
     * Eucliedean distance between e1 and e2
     * @param e1
     * @param e2
     * @return
     */
    private double distance(Double[] e1, Double[] e2) {
        if(e1.length != e2.length)
            throw new IllegalArgumentException("e1 and e2 lie in two different dimensional spaces.");

        double sum = 0.0;
        for(int i = 0; i < e1.length; ++i)
            sum += Math.pow(e1[i] - e2[i], 2);
        return Math.sqrt(sum);
    }

    /**
     * Returns the minimum value in vector
     * @param vector
     * @return
     */
    private double min(double[] vector){
        if (vector.length == 0)
            throw new IllegalStateException();
        double min = Double.MAX_VALUE;
        for(double e : vector){
            if(!Double.isNaN(e) && e < min)
                min = e;
        }
        return min;
    }
}
