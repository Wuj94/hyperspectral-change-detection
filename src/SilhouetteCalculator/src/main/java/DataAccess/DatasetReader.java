package DataAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class deriving this abstract class reads points from file assuming that every component of each data point is
 * involved in the (preceding) clustering operation.
 * Objects of DataAccess.DatasetReader subclass types must be created using the parametric constructor or using the default
 * constructor followed by a call to setUp.
 */
public abstract class DatasetReader {
    protected List<Double[]> spectralAttrs = new ArrayList<>();
    protected List<Integer> classes = new ArrayList<>();

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
     * Returns the attributes involved in the clustering operation. Class/cluster id is not included.
     * @return
     */
    public Double[][] getAttrs(){
        return spectralAttrs.toArray(new Double[1][1]);
    }

    /**
     * Returns the class/cluster id attribute of each example.
     * @return
     */
    public Integer[] getClassAttrs(){
        return classes.toArray(new Integer[1]);
    }

    /**
     * Parse every attribute in line
     * @param line contains the example
     * @param pattern a pattern matching the attributes in line
     * @param group group number of pattern's regex for attribute extraction
     * @return the attributes
     */
    protected Double[] parseAttrs(String line, Pattern pattern, int group) {
        Matcher matcher = pattern.matcher(line);
        List<Double> attributes = new ArrayList<>();

        while (matcher.find())
            attributes.add(new Double(matcher.group(group)));

        return attributes.toArray(new Double[1]);
    }
}
