package Business;

import java.util.Map;
import java.util.Set;

/**
 * Transforms data sets passed to the ctor to a matrix containing belonging class and belonging cluster
 */
public class Transformer {
    private Map<String, Integer> dataset1;
    private Map<String, Integer> dataset2;

    /**
     * @param dataset1 classified data set
     * @param dataset2 clustering-resulting data set having the same keys (examples) of dataset1
     * @throws IllegalArgumentException
     */
    public Transformer(Map<String, Integer> dataset1, Map<String, Integer> dataset2) throws IllegalArgumentException {
        if(dataset1.size() != dataset2.size())
            throw new IllegalArgumentException("The passed data sets have different size");


        this.dataset1 = dataset1;
        this.dataset2 = dataset2;
    }

    /**
     * Merge the data sets passed to constructor into a data matrix having only the belonging class identifier and
     * the belonging cluster identifier as columns.
     * @return
     */
    public int[][] merge(){
        Set<String> keys = dataset1.keySet();
        int[][] result = new int[keys.size()][2];

        int i = 0;
        for(String k : keys){
            int[] row = new int[2];
            Integer clas = dataset1.get(k);
            Integer cluster = dataset2.get(k);

            if(clas == null || cluster == null)
                throw new IllegalStateException("The datasets have different keys");

            row[0] = clas;
            row[1] = cluster;
            result[i] = row;
            ++i;
        }

        return result;
    }

    public int datasetsSize(){
        return dataset1.size();
    }
}
