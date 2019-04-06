package DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Read an artificially generated data set having the same number of point for each contained cluster.
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

    /**
     * Default constructor. You need to call setUp method after.
     */
    ArtificialReader(){}

    @Override
    public void setUp(String[] filenames) {
        file = new File(filenames[0]);
        pointsPerCluster = Integer.parseInt(filenames[1]);
    }

    @Override
    public void readDataset() throws IOException {
        //-81.2622959409307,65.84777093920698
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
                dataset.put(parseSpatialAttrs(line, pattern, group), cluster);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        DatasetReader reader = new ArtificialReader("/home/data/Dropbox/Tesi/CLUSTERING/TestDatasets/_2d5c.txt",
                250000);

        reader.readDataset();

        reader.getDataset();
    }
}
