package DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads the csv Indian Pines data set contained in the s2tec package available at
 * http://www.di.uniba.it/~appice/software/S2TEC/index.htm
 */
class IndianPinesReader extends DatasetReader {
    private File file;

    /**
     * @param filename file path of indian pines dataset in csv format, having the first two field as x,y coordinates,
     *                 199 spectral attributes and a trailing class attribute.
     */
    IndianPinesReader(String filename){
        file = new File(filename);
    }

    IndianPinesReader(){}

    /**
     * @param filenames one-object array file path of indian pines dataset in csv format, having the first two field as x,y coordinates,
     *                 199 spectral attributes and a trailing class attribute.
     */
    @Override
    public void setUp(String[] filenames) {
        file = new File(filenames[0]);
    }

    @Override
    public void readDataset() throws IOException {
        if(file == null)
            throw new IllegalStateException("setUp method has to be called first");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            final Pattern classPattern = Pattern.compile("(\\d+,\\d+),(\\d+,\\d+){199},(\\d+)");
            final Pattern spectralPattern = Pattern.compile("(\\d+),");
            final int spectralGroup = 1;
            final int classGroup = 3;

            String line;
            for(int i = 0; (line = reader.readLine()) != null; ++i){
                if(i > 1){ //skip the first two lines
                    String spectralLine = line.replaceFirst("\\d+,", "").replaceFirst("\\d+", "");
                    spectralAttrs.add(parseAttrs(spectralLine, spectralPattern, spectralGroup));
                    classes.add(parseBelongingClass(line, classPattern, classGroup));
                }
            }
        }
    }

    /**
     * Returns the identifier of the belonging class of the example in line
     * @param line
     * @param pattern pattern matching each comma-separated value
     * @param classGroup group capturing the class value in pattern's regex
     * @return
     */
    private Integer parseBelongingClass(String line, Pattern pattern, int classGroup) {
        Matcher matcher = pattern.matcher(line);

        matcher.find();

        return Integer.parseInt(matcher.group(classGroup));
    }

    /*
    public static void main(String[] args) throws IOException {
        DataAccess.DatasetReader reader = new DataAccess.IndianPinesReader("/home/data/Dropbox/Tesi/CLUSTERING/TestDatasets/Indian_Pine_DataSet.csv");

        reader.readDataset();

        reader.getAttrs();
    } */
}
