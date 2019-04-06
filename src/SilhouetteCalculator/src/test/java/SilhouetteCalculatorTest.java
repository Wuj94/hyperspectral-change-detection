import Business.SilhouetteCalculator;
import Business.SilhouetteException;
import junit.framework.TestCase;
import org.junit.Test;


public class SilhouetteCalculatorTest {

    /**
     * See relative file in test folder
     */
    @Test
    public void silhouetteTest2p1d3c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException {
        Double[][] spectralAttrs = new Double[6][1];
        Integer[] clusterId = new Integer[6];

        Double[] p11 = {2.412};
        Double[] p12 = {3.737};
        spectralAttrs[0] = p11;
        clusterId[0] = 1;
        spectralAttrs[1] = p12;
        clusterId[1] = 1;

        Double[] p21 = {8.1};
        Double[] p22 = {6.254};
        spectralAttrs[2] = p21;
        clusterId[2] = 2;
        spectralAttrs[3] = p22;
        clusterId[3] = 2;


        Double[] p31 = {13.12};
        Double[] p32 = {11.98};
        spectralAttrs[4] = p31;
        clusterId[4] = 3;
        spectralAttrs[5] = p32;
        clusterId[5] = 3;

        TestCase.assertEquals(0.6520260775, new SilhouetteCalculator(spectralAttrs, clusterId).silhouette(), 0.000000001);
    }

    /**
     * See relative file in test folder
     */
    @Test
    public void silhouetteTest3p2d2c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException {
        Double[][] spectralAttrs = new Double[6][2];
        Integer[] clusterId = new Integer[6];

        Double[] p11 = {2.0, 4.1};
        Double[] p12 = {2.1, 4.1};
        Double[] p13 = {1.9, 3.7};
        spectralAttrs[0] = p11;
        spectralAttrs[1] = p12;
        spectralAttrs[2] = p13;
        clusterId[0] = 1;
        clusterId[1] = 1;
        clusterId[2] = 1;

        Double[] p21 = {6.9, 1.1};
        Double[] p22 = {7.2, 0.8};
        Double[] p23 = {6.9, 0.7};
        spectralAttrs[3] = p21;
        spectralAttrs[4] = p22;
        spectralAttrs[5] = p23;
        clusterId[3] = 2;
        clusterId[4] = 2;
        clusterId[5] = 2;


        TestCase.assertEquals(0.9404421788, new SilhouetteCalculator(spectralAttrs, clusterId).silhouette(), 0.000000001);
    }

    /**
     * See relative file in Validator/test folder
     */
    @Test
    public void silhouetteTest2p1d2c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException {
        Double[][] spectralAttrs = new Double[4][1];
        Integer[] clusterId = new Integer[4];

        Double[] p11 = {2.412};
        Double[] p12 = {3.737};
        spectralAttrs[0] = p11;
        spectralAttrs[1] = p12;
        clusterId[0] = 1;
        clusterId[1] = 1;

        Double[] p21 = {8.1};
        Double[] p22 = {6.254};
        spectralAttrs[2] = p21;
        spectralAttrs[3] = p22;
        clusterId[2] = 2;
        clusterId[3] = 2;

        TestCase.assertEquals(0.5972088147, new SilhouetteCalculator(spectralAttrs, clusterId).silhouette(), 0.000000001);
    }

    /**
     * See relative file in Validator/test folder
     */
    @Test
    public void silhouetteTest2p1d4c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException {
        Double[][] spectralAttrs = new Double[8][1];
        Integer[] clusterId = new Integer[8];

        Double[] p11 = {2.412};
        Double[] p12 = {3.737};
        spectralAttrs[0] = p11;
        spectralAttrs[1] = p12;
        clusterId[0] = 1;
        clusterId[1] = 1;

        Double[] p21 = {8.1};
        Double[] p22 = {6.254};
        spectralAttrs[2] = p21;
        spectralAttrs[3] = p22;
        clusterId[2] = 2;
        clusterId[3] = 2;

        Double[] p31 = {13.12};
        Double[] p32 = {11.98};
        spectralAttrs[4] = p31;
        spectralAttrs[5] = p32;
        clusterId[4] = 3;
        clusterId[5] = 3;

        Double[] p41 = {16.04};
        Double[] p42 = {17.01};
        spectralAttrs[6] = p41;
        spectralAttrs[7] = p42;
        clusterId[6] = 4;
        clusterId[7] = 4;

        TestCase.assertEquals(0.65753473, new SilhouetteCalculator(spectralAttrs,clusterId).silhouette(), 0.000000001);
    }

    /**
     * See relative file in test folder
     */
    @Test
    public void parallelSilhouetteTest2p1d3c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException, InterruptedException {
        Double[][] spectralAttrs = new Double[6][1];
        Integer[] clusterId = new Integer[6];

        Double[] p11 = {2.412};
        Double[] p12 = {3.737};
        spectralAttrs[0] = p11;
        clusterId[0] = 1;
        spectralAttrs[1] = p12;
        clusterId[1] = 1;

        Double[] p21 = {8.1};
        Double[] p22 = {6.254};
        spectralAttrs[2] = p21;
        clusterId[2] = 2;
        spectralAttrs[3] = p22;
        clusterId[3] = 2;


        Double[] p31 = {13.12};
        Double[] p32 = {11.98};
        spectralAttrs[4] = p31;
        clusterId[4] = 3;
        spectralAttrs[5] = p32;
        clusterId[5] = 3;

        TestCase.assertEquals(0.6520260775, new SilhouetteCalculator(spectralAttrs, clusterId).parallelSihouette(4), 0.000000001);
    }

    /**
     * See relative file in test folder
     */
    @Test
    public void parallelSilhouetteTest3p2d2c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException, InterruptedException {
        Double[][] spectralAttrs = new Double[6][2];
        Integer[] clusterId = new Integer[6];

        Double[] p11 = {2.0, 4.1};
        Double[] p12 = {2.1, 4.1};
        Double[] p13 = {1.9, 3.7};
        spectralAttrs[0] = p11;
        spectralAttrs[1] = p12;
        spectralAttrs[2] = p13;
        clusterId[0] = 1;
        clusterId[1] = 1;
        clusterId[2] = 1;

        Double[] p21 = {6.9, 1.1};
        Double[] p22 = {7.2, 0.8};
        Double[] p23 = {6.9, 0.7};
        spectralAttrs[3] = p21;
        spectralAttrs[4] = p22;
        spectralAttrs[5] = p23;
        clusterId[3] = 2;
        clusterId[4] = 2;
        clusterId[5] = 2;


        TestCase.assertEquals(0.9404421788, new SilhouetteCalculator(spectralAttrs, clusterId).parallelSihouette(4), 0.000000001);
    }

    /**
     * See relative file in Validator/test folder
     */
    @Test
    public void parallelSilhouetteTest2p1d2c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException, InterruptedException {
        Double[][] spectralAttrs = new Double[4][1];
        Integer[] clusterId = new Integer[4];

        Double[] p11 = {2.412};
        Double[] p12 = {3.737};
        spectralAttrs[0] = p11;
        spectralAttrs[1] = p12;
        clusterId[0] = 1;
        clusterId[1] = 1;

        Double[] p21 = {8.1};
        Double[] p22 = {6.254};
        spectralAttrs[2] = p21;
        spectralAttrs[3] = p22;
        clusterId[2] = 2;
        clusterId[3] = 2;

        TestCase.assertEquals(0.5972088147, new SilhouetteCalculator(spectralAttrs, clusterId).parallelSihouette(4), 0.000000001);
    }

    /**
     * See relative file in Validator/test folder
     */
    @Test
    public void parallelSilhouetteTest2p1d4c() throws SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException, InterruptedException {
        Double[][] spectralAttrs = new Double[8][1];
        Integer[] clusterId = new Integer[8];

        Double[] p11 = {2.412};
        Double[] p12 = {3.737};
        spectralAttrs[0] = p11;
        spectralAttrs[1] = p12;
        clusterId[0] = 1;
        clusterId[1] = 1;

        Double[] p21 = {8.1};
        Double[] p22 = {6.254};
        spectralAttrs[2] = p21;
        spectralAttrs[3] = p22;
        clusterId[2] = 2;
        clusterId[3] = 2;

        Double[] p31 = {13.12};
        Double[] p32 = {11.98};
        spectralAttrs[4] = p31;
        spectralAttrs[5] = p32;
        clusterId[4] = 3;
        clusterId[5] = 3;

        Double[] p41 = {16.04};
        Double[] p42 = {17.01};
        spectralAttrs[6] = p41;
        spectralAttrs[7] = p42;
        clusterId[6] = 4;
        clusterId[7] = 4;

        TestCase.assertEquals(0.65753473, new SilhouetteCalculator(spectralAttrs,clusterId).parallelSihouette(4), 0.000000001);
    }

}
