package DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Read a data set generated using DatasetGenerator program having the same number of point for each contained cluster.
 */
class ArtificialReader extends DatasetReader {
    private File file;
    private int pointsPerCluster;

    /**
     * @param filename file path of the data set to read
     * @param pointsPerCluster
     */
    ArtificialReader(String filename, int pointsPerCluster){
        file = new File(filename);
        this.pointsPerCluster = pointsPerCluster;
    }

    ArtificialReader(){}

    @Override
    public void setUp(String[] filenames) {
        file = new File(filenames[0]);
        pointsPerCluster = Integer.parseInt(filenames[1]);
    }

    @Override
    public void readDataset() throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            Pattern pattern = Pattern.compile("(-?\\d+(\\.\\d+)?(\\w[-\\+]?\\d+)?),?");
            final int group = 1;

            String line;
            int cluster = 1;
            for(int i = 1;(line = reader.readLine()) != null; ++i){
                if(i == pointsPerCluster){
                    ++cluster;
                    i = 1;
                }
                spectralAttrs.add(parseAttrs(line, pattern, group));
                classes.add(cluster);
            }
        }
    }

    /*
    public static void main(String[] args) throws IOException {
        DataAccess.DatasetReader ar = new DataAccess.ArtificialReader("/home/data/Dropbox/Tesi/CLUSTERING/TestDatasets/_2d5c.txt",
                250000);
        ar.readDataset();
        ar.getClassAttrs();
    }*/
}
