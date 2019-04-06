package Business;

/**
 * Computes rand index
 */
public class RandIndex {
    private final Transformer transformer;

    /**
     * @param transformer for the data set involved in the rand index calculation
     */
    public RandIndex(Transformer transformer){
        this.transformer = transformer;
    }

    /**
     * Computes rand index.
     * @return rand index.
     */
    public double randIndex() throws RandIndexException {
        if(transformer.datasetsSize() == 0)
            throw new RandIndexException.EmptyDatasetException("Data sets passed are empty.");
        int[][] dataMatrix = transformer.merge();

        if(dataMatrix.length < 2){
            throw new RandIndexException("Cannot compute rand index on a singleton data set.");
        }

        long a = Long.parseUnsignedLong("0"),b = Long.parseUnsignedLong("0");
        long c = Long.parseUnsignedLong("0"), d = Long.parseUnsignedLong("0");
        for(int i = 0; i < dataMatrix.length; ++i){
            int clas1 = dataMatrix[i][0];
            int clust1 = dataMatrix[i][1];
            for(int j = i + 1; j < dataMatrix.length; ++j) {
                int clas2 = dataMatrix[j][0];
                int clust2 = dataMatrix[j][1];

                if(clas1 == clas2 && clust1 == clust2)
                    ++a;
                else if(clas1 != clas2 && clust1 == clust2)
                    ++b;
                else if(clas1 == clas2 && clust1 != clust2)
                    ++c;
                else
                    ++d;
            }
        }
        return new Double(a + d) / new Double(a + b + c + d);
    }
}
