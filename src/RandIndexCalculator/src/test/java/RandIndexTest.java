import Business.RandIndex;
import Business.RandIndexException;
import Business.Transformer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RandIndexTest {
    /**
     * Taken from Vendramin, Campello, and Hruschka: Relative Clustering Validity Criteria, p 221
     */
    @Test
    public void randIndexTest1() throws RandIndexException {
        Map<String, Integer> ds1 = new HashMap<>();
        ds1.put("1,1", 1);
        ds1.put("1,2", 1);
        ds1.put("1,3", 1);
        ds1.put("1,4", 1);
        ds1.put("2,5", 2);
        ds1.put("2,6", 2);
        ds1.put("2,7", 2);
        ds1.put("2,8", 2);


        Map<String, Integer> ds2 = new HashMap<>();
        ds2.put("1,4",1);
        ds2.put("2,5", 1);
        ds2.put("2,6", 1);
        ds2.put("1,1", 2);
        ds2.put("1,2", 2);
        ds2.put("1,3", 2);
        ds2.put("2,7", 3);
        ds2.put("2,8", 3);

        Assert.assertEquals(0.6786 ,new RandIndex(new Transformer(ds1, ds2)).randIndex(), 0.0001);
    }

    /**
     * Ground truth has 1 class containing 1 point. This point is also contained in
     * the clustering-resulting dataset having only one 1 cluster.
     * We expect an exception because at least two datapoints are required.
     * @throws RandIndexException
     */
    @Test(expected = RandIndexException.class)
    public void randIndexTest1g1c1p() throws RandIndexException {
        Map<String, Integer> gt = new HashMap<>();
        gt.put("1,1", 1);

        Map<String, Integer> clust = new HashMap<>();
        clust.put("1,1", 1);

        Assert.assertEquals(1 ,new RandIndex(new Transformer(gt, clust)).randIndex(), 0.0001);
    }

    /**
     * Ground truth has 1 class containing 2 point. This two points are also contained in
     * the clustering-resulting data set having only one 1 cluster.
     * We trivially expect 1 as rand index.
     * @throws RandIndexException
     */
    @Test
    public void randIndexTest1g1c2p() throws RandIndexException {
        Map<String,Integer> gt = new HashMap<>();
        gt.put("1", 1);
        gt.put("2", 1);

        Map<String,Integer> clust = new HashMap<>();
        clust.put("1", 1);
        clust.put("2", 1);

        Assert.assertEquals(1 ,new RandIndex(new Transformer(gt, clust)).randIndex(), 0.0001);
    }

    /**
     * See relative file in RandIndexCalculator/test folder
     */
    @Test
    public void randIndexTest1g2c2p() throws RandIndexException {
        Map<String,Integer> gt = new HashMap<>();
        gt.put("a", 1);
        gt.put("b", 1);

        Map<String,Integer> clust = new HashMap<>();
        clust.put("a", 1);
        clust.put("b", 2);

        Assert.assertEquals(0, new RandIndex(new Transformer(gt, clust)).randIndex(), 0.0001);
    }

    /**
     * See relative file in RandIndexCalculator/test folder
     */
    @Test
    public void randIndexTest2g1c2p() throws RandIndexException {
        Map<String,Integer> gt = new HashMap<>();
        gt.put("a", 1);
        gt.put("b", 2);

        Map<String,Integer> clust = new HashMap<>();
        clust.put("a", 1);
        clust.put("b", 1);

        Assert.assertEquals(0, new RandIndex(new Transformer(gt, clust)).randIndex(), 0.0001);
    }
}
