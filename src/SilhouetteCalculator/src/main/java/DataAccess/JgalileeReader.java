package DataAccess;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads clustering-resulting data set generated by Jgalilee implementation
 */
class JgalileeReader extends DatasetReader{
    private File pointsFile;
    private File centersFile;
    private List<Double[]> centers;
    private int dimensionality = -1;

    /**
     * @param dataPointsFilename file path of the data set given in input to Jgalilee's implementation
     * @param centersFilename file path of the output of Jgalilee's implementation
     */
    JgalileeReader(String dataPointsFilename, String centersFilename){
        pointsFile = new File(dataPointsFilename);
        centersFile = new File(centersFilename);
    }

    JgalileeReader(){}

    /**
     * @param filenames A 2-object array, namely dataPointsFilename and centersFilename.
     * dataPointsFilename file path of the data set given in input to Jgalilee's implementation
     * centersFilename file path of the output of Jgalilee's implementation
     */
    @Override
    public void setUp(String[] filenames) {
        pointsFile = new File(filenames[0]);
        centersFile = new File(filenames[1]);
    }

    @Override
    public void readDataset() throws IOException {
        if(pointsFile == null || centersFile == null)
            throw new IllegalStateException("setUp should be called first.");
        readCenters();
        readPoints();
    }

    /**
     * Read points from file passed to constructor
     * @throws IOException
     */
    private void readPoints() throws IOException {
        try (Scanner scanner= new Scanner(new BufferedReader(new FileReader(pointsFile)))) {
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                Double[] attrs = parseAttrs(line, null, 0);
                spectralAttrs.add(attrs);
                classes.add(getClosestCentroid(attrs));
            }
        }
    }

    /**
     * Associates a data point to its belonging cluster
     * @param attrs data point attributes or coordinates
     * @return identifier of the belonging cluster
     */
    private Integer getClosestCentroid(Double[] attrs) {
        int result = -1;
        double minDistance = Double.MAX_VALUE;
        double distance;
        for(int i = 1; i <= centers.size(); ++i) {
            Double[] c = centers.get(i-1);
            distance = squaredDistance(attrs, c);
            if(distance < minDistance) {
                minDistance = distance;
                result = i;
            }
        }
        return result;
    }

    /**
     * Compute squared euclidean distance from p1 to p2
     * @param p1
     * @param p2
     * @return distance(p1, p2)^2
     */
    private double squaredDistance(Double[] p1, Double[] p2) {
        int p1len = p1.length;
        if(p1len != p2.length) {
            System.err.println("p1: " + Arrays.toString(p1));
            System.err.println("p2: " + Arrays.toString(p2));
            throw new IllegalArgumentException("p1 and p2 lie in two different spaces.");
        }

        double sum = 0.0;
        for(int i = 0; i < p1len; ++i){
            sum += Math.pow(p1[i] - p2[i], 2);
        }
        return sum;
    }

    /**
     * Parse every attribute. readCenters method need to be called first.
     * @param line
     * @param pattern pattern matching attributes one by one
     * @param group of pattern's regex to extract attributes
     * @return attributes as array of double
     */
    protected Double[] parseAttrs(String line, Pattern pattern, int group) {
        Double[] result = new Double[dimensionality];

        String[] components = line.split(" ");

        for(int i = 0; i < components.length; ++i)
            result[i] = Double.parseDouble(components[i]);

        return result;
    }

    /**
     * Read centers from file given to the constructor
     * @throws IOException
     */
    private void readCenters() throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(centersFile))){
            centers = new ArrayList<>();
            final Pattern pattern = Pattern.compile("\\G(\\d+\\s)?(-?\\d+(.\\d+)?(\\w[-\\+]?\\d+)?\\s?)");
            String line;
            for(int i = 0; (line = reader.readLine()) != null; ++i){
                if(i == 0)
                    dimensionality = inferDimensionality(line, pattern);
                centers.add(parseCenter(line, pattern));

            }

        }
    }

    /**
     * Infer dimensionality of point in line
     * @param line
     * @param pattern matches each component of the point one by one
     * @return
     */
    private int inferDimensionality(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);

        int dimensionality = 0;
        while(matcher.find()){
            ++dimensionality;
        }
        return dimensionality;
    }

    /**
     * Parse a center represented by line
     * @param line
     * @param pattern pattern matching center point's component one by one
     * @return
     */
    private Double[] parseCenter(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        Double[] center;

        if(dimensionality == -1) {
            dimensionality = 0;
            while (matcher.find()) {
                ++dimensionality;
            }
            matcher.reset();
        }

        center = new Double[dimensionality];
        for(int i = 0; matcher.find(); ++i)
            center[i] = Double.parseDouble(matcher.group(2));

        return center;
    }
}
