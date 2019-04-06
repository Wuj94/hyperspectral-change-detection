package DataAccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class deriving this abstract class reads points from file assuming that the first two component of each data point are
 * spatial attributes
 */
public abstract class DatasetReader {
    protected final Map<String, Integer> dataset = new HashMap<>();

    /**
     * Set up resources
     * @param filenames
     */
    public abstract void setUp(String[] filenames);

    /**
     * Read the dataset
     * @throws IOException
     */
    public abstract void readDataset() throws IOException;

    /**
     * @return data set previously read using readDataset method
     */
    public Map<String, Integer> getDataset(){
        if(dataset.size() == 0)
            throw new IllegalStateException("You need to read the data set using readDataset method first.");
        return dataset;
    }

    /**
     * Parse the x,y coordinates of the example in line
     * @param line contains the example
     * @param regex a pattern matching the spatial attributes in line
     * @param group group number of regex for attribute extraction
     * @return the coordinates formatted as x,y
     */
    final protected String parseSpatialAttrs(String line, Pattern regex, int group) {
        Matcher matcher = regex.matcher(line);
        Float[] xy = new Float[2];
        for(int i = 0; i < 2 && matcher.find(); ++i) {
            xy[i] = new Float(matcher.group(group));
        }
        return String.valueOf(xy[0]) + "," + String.valueOf(xy[1]);
    }
}