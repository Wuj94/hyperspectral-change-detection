package Interface;

import Business.RandIndex;
import Business.RandIndexException;
import Business.Transformer;
import DataAccess.ReaderBuilder;
import DataAccess.DatasetReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CommandLineInterface {
    private static void printHelp(){
        System.out.println("  USAGE RandIndexCalculator [GROUND_TRUTH_TYPE] [IMPLEMENTATION] [GROUND_TRUTH_FILE] [IMPLEMENTATION_OUT_PATH] \n" +
                "      {IMPLEMENTATION_IN_DATAPOINTS_FILE}\n" +
                "      Every argument is mandatory.\n" +
                "      GROUND_TRUTH_TYPE:\n" +
                "           -a  artificial dataset generated using TestDatasetGenerator\n" +
                "           -i  indian pines dataset in s2tec (available at: http://www.di.uniba.it/~appice/software/S2TEC/index.htm)\n" +
                "      IMPLEMENTATION: \n" +
                "           -b  BrizziB's MapReduce Kmeans implementation (available on GitHub)\n" +
                "           -j  Jgalilee's MapReduce Kmeans implementation (available on GitHub) \n" +
                "           -y  Yhfyhf's MapReduce Kmeans implementation (available on GitHub)\n" +
                "      OUTPUT FOLDER:   file path of the output file\n" +
                "      GROUND_TRUTH_FILE:     file path of ground truth file\n" +
                "      IMPLEMENTATION_OUT_PATH:  path of implementation's output folder\n" +
                "      IMPLEMENTATION_IN_DATAPOINT_FILE:    file path of input data points file (for Jgalilee implementation only)\n" +
                "      POINTS_PER_CLUSTER:  number of points per cluster (for GROUNT_TRUTH_TYPE -a only)");
    }

    /**
     * Program entry point.
     * USAGE RandIndexCalculator [GROUND_TRUTH_TYPE] [IMPLEMENTATION] [GROUND_TRUTH_FILE] [IMPLEMENTATION_OUT_PATH]
     * {IMPLEMENTATION_IN_DATAPOINTS_FILE}
     * Every argument is mandatory.
     * GROUND_TRUTH_TYPE:
     *      -a  artificial dataset generated using TestDatasetGenerator
     *      -i  indian pines dataset in s2tec (available at: http://www.di.uniba.it/~appice/software/S2TEC/index.htm)
     * IMPLEMENTATION:
     *      -b  BrizziB's MapReduce Kmeans implementation (available on GitHub)
     *      -j  Jgalilee's MapReduce Kmeans implementation (available on GitHub)
     *      -y  Yhfyhf's MapReduce Kmeans implementation (available on GitHub)
     * OUTPUT FOLDER:   file path of the output file
     * GROUND_TRUTH_FILE:     file path of ground truth file
     * IMPLEMENTATION_OUT_PATH:  path of implementation's output folder
     * IMPLEMENTATION_IN_DATAPOINT_FILE:    file path of input data points file (for Jgalilee implementation only)
     * POINTS_PER_CLUSTER:  number of points per cluster (for GROUNT_TRUTH_TYPE -a only)
     */
    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            printHelp();
            return;
        }else{
            if(args[0].equals("-h") || args[0].equals("--help")) {
                printHelp();
                return;
            } else {
                DatasetReader gtReader;
                DatasetReader impReader;
                if (args.length >= 4 && (args[1].equals("-b") || args[1].equals("-y"))) {
                    //either BrizziB or Jgalilee
                    if(args[1].equals("-b")){
                        //BrizziB
                        List<String> filenames = new ArrayList<>();
                        File outpath = new File(args[3]);
                        for(File f : outpath.listFiles()){
                            if(f.isFile()) {
                                String name = f.getName();
                                if (name.contains("r-") && !name.contains("crc"))
                                    filenames.add(f.getAbsolutePath());
                            }
                        }
                        if(filenames.size() == 0){
                            printHelp();
                            System.err.println("\nERROR: No suitable file in directory IMPLEMENTATION_OUT_PATH");
                            return;
                        } else {
                            impReader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.BRIZZIB);
                            impReader.setUp(filenames.toArray(new String[1]));
                        }
                    } else if(args[1].equals("-y")) {
                        //Yhfyhf
                        String filename = "";
                        File outpath = new File(args[3]);
                        boolean error = true;
                        for(File f : outpath.listFiles()){
                            if(f.isFile()){
                                String name = f.getName();
                                if(name.contains("r-") && !name.contains("crc")) {
                                    error = false;
                                    filename = f.getAbsolutePath();
                                }
                            }
                        }
                        if(error){
                            printHelp();
                            System.out.println("\nERROR: No suitable file in directory IMPLEMENTATION_OUT_PATH");
                            return;
                        }else
                            impReader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.YHFYHF);
                    } else {
                        printHelp();
                        return;
                    }

                } else if (args.length >= 5 && (args[1].equals("-j"))) {
                    //Jgalilee
                    File dataPoints = new File(args[5]);
                    File centers = new File(args[4]);
                    String centersFn = null;
                    for(File f : centers.listFiles()){
                        if(f.isFile()){
                            String name = f.getName();
                            if(name.contains("r-") && !name.contains("crc")){
                                centersFn = f.getAbsolutePath();
                            }
                        }
                    }
                    if(centersFn == null){
                        printHelp();
                        System.out.println("\nERROR: No suitable file in directory IMPLEMENTATION_OUT_PATH");
                        return;
                    } else {
                        impReader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.JGALILEE);
                        String[] res = {dataPoints.getAbsolutePath(), centersFn};
                        impReader.setUp(res);
                    }
                } else {
                    printHelp();
                    return;
                }

                if (args.length >= 5 && args[0].equals("-a")) {
                    //artificial
                    if(args[1].equals("-j")) {
                        gtReader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.ARTIFICIAL);
                        String[] res = {args[3], args[6]};
                        gtReader.setUp(res);
                    }
                    else {
                        gtReader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.ARTIFICIAL);
                        String[] res = {args[3], args[5]};
                        gtReader.setUp(res);
                    }
                } else if (args.length >= 4 && args[0].equals("-i")) {
                    //indian pines
                    gtReader = ReaderBuilder.getInstance().getReader(ReaderBuilder.Implementation.INDIAN_PINES);
                    String[] res = {args[3]};
                    gtReader.setUp(res);
                } else {
                    printHelp();
                    return;
                }

                gtReader.readDataset();
                impReader.readDataset();

                Transformer transformer = new Transformer(gtReader.getDataset(), impReader.getDataset());
                RandIndex rand = new RandIndex(transformer);

                double result;
                try {
                    result = rand.randIndex();
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(args[2]))){
                        writer.write("rand index: " + Double.toString(result));
                    }
                }catch (RandIndexException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
