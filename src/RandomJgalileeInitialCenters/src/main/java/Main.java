import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Main {

    /**
     * args[0] number of initial centers to generate
     * args[1] file path of  the data set
     * args[2] output file path
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help"))
            printHelp();
        else {
            final int K = Integer.parseInt(args[0]); //number of centers
            File dataSet = new File(args[1]);
            String outputFilename = args[2];
            int counter = 0; //counts the number of lines in the data set
            String line = null;

            //Read dataset
            try(BufferedReader reader = new BufferedReader(new FileReader(dataSet))){
                while ((line = reader.readLine()) != null) {
                    ++counter;
                }
            }

            //generate k random integers
            Random random = new Random();
            int[] distinct = random.ints(K, 0, counter).distinct().toArray();
            while (distinct.length != K) {
                distinct = random.ints(K, 0, counter).distinct().toArray();
            }
            Arrays.sort(distinct);

            //create output
            StringBuffer output = new StringBuffer();
            int centerNum = 1;
            try(BufferedReader reader = new BufferedReader(new FileReader(dataSet))){
                int distIdx = 0;
                for (int i = 0; (line = reader.readLine()) != null
                        && distIdx < distinct.length; ++i) {
                    if (i == distinct[distIdx]) {
                        output.append(centerNum + " " + line + "\n");
                        ++distIdx;
                        ++centerNum;
                    }
                }
            }

            //Print output to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));
            writer.write(output.toString());

            writer.close();
        }
    }

    private static void printHelp() {
        System.out.println("Program USAGE: [N] [DATASET] [OUTPUT]\n" +
                "     [N]: number of initial centers to generate\n" +
                "     [DATASET]: file path of  the data set\n" +
                "     [OUTPUT]: output file path");
    }
}
