import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ChangeDetector {
    private final Map<String, ClusterPair> dataset;

    ChangeDetector(Map<String, ClusterPair> dataset){
        this.dataset = dataset;
    }

    List<String> detect(){
        Set<String> keyset = dataset.keySet();
        List<String> changes = new ArrayList<>();

        for(String k : keyset){
            if(dataset.get(k).changes())
                changes.add(k);
        }

        return changes;
    }

    /**
     * args[0] file path of the concatenated dataset
     * args[1] file path of the clustering resulting centers
     * args[2] output file path
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help"))
            printHelp();
        else {
            String points_fn = args[0];
            String centers_fn = args[1];
            String output_fn = args[2];

            DatasetReader reader = new JgalileeReader(points_fn, centers_fn);
            reader.readDataset();


            ChangeDetector detector = new ChangeDetector(reader.getDataset());
            List<String> detectedChanges = detector.detect();

            try (FileWriter fw = new FileWriter(new File(output_fn))) {
                StringBuilder sBuilder = new StringBuilder();
                for (String string : detectedChanges) {
                    sBuilder.append(string + "\n");
                }
                fw.write(sBuilder.toString());
            }
        }
    }

    private static void printHelp() {
        System.out.println("Program USAGE: [DATASET] [CENTERS] [OUTPUT]\n" +
                "     [DATASET] file path of the concatenated dataset\n" +
                "     [CENTERS] file path of the clustering resulting centers\n" +
                "     [OUTPUT] output file path");
    }
}
