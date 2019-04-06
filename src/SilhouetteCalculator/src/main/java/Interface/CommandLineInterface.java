package Interface;

import Business.SilhouetteCalculator;
import Business.SilhouetteException;
import DataAccess.DatasetReader;
import DataAccess.ReaderBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandLineInterface {

    private static DatasetReader getReader(String[] args){
        String implem = args[0];
        String dataset = args[1];

        DatasetReader reader = null;
        switch (implem){
            case "-b": {
                List<String > filenames = new ArrayList<>(20);
                File ds = new File(dataset);
                for(File f : ds.listFiles())
                    if(f.isFile() && f.getName().startsWith("part"))
                        filenames.add(f.getAbsolutePath());
                reader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.BRIZZIB);
                reader.setUp(filenames.toArray(new String[1]));
                return reader;
            }
            case "-j": {
                String clusterCenters = args[3];
                reader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.JGALILEE);
                String[] filenames = {dataset, clusterCenters};
                reader.setUp(filenames);
                return reader;
            }
            case "-y": {
                reader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.YHFYHF);
                String[] filenames = {dataset};
                reader.setUp(filenames);
                return reader;
            }
            default: {
                System.err.println("Invalid implementation option");
                System.exit(2);
            }
        }
        return ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.YHFYHF);
    }

    private static void printHelp(){
        System.out.println("SilhouetteCalculator USAGE:" +
                " [IMPLEM] [ARG1] [OUTPUT] [CLUST]\n" +
                "     [IMPLEM]implementation in {-b, -j, -y} where b : BrizziB, y: Yhfyhf, j:Jgalilee\n" +
                "     [ARG1] output folder for BrizziB, clustering op output for Yhfyhf, input dataset for Jgalilee\n" +
                "     [OUTPUT] file path of the output file\n" +
                "     [CLUST] file path of the clustering resulting cluster centers");
    }

    /**
     * args[0] implementation in {-b, -j, -y} where b : BrizziB, y: Yhfyhf, j:Jgalilee
     * args[1] output folder for BrizziB, clustering op output for Yhfyhf, input dataset for Jgalilee
     * args[2] file path of the output file
     * args[3] file path of the clustering resulting cluster centers
     * @param args
     * @throws IOException
     * @throws SilhouetteException.DifferentLengthException
     * @throws SilhouetteException.EmptyDatasetException
     */
    public static void main(String[] args) throws IOException, SilhouetteException.DifferentLengthException, SilhouetteException.EmptyDatasetException, InterruptedException {

        if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            printHelp();
        } else {
            long start = System.currentTimeMillis();

            String outputFile = args[2];

            DatasetReader reader = CommandLineInterface.getReader(args);

            reader.readDataset();

            SilhouetteCalculator sc = new SilhouetteCalculator(reader.getAttrs(), reader.getClassAttrs());

            double silhouette = sc.parallelSihouette(4);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write("silhouette: " + silhouette);
            }

            long end = System.currentTimeMillis();
            System.out.println("Elapsed time: " + (end - start));
        }
    }
}
