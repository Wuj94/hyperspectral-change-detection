import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates various metrics (precision, accuracy, ...) for the change detection task.
 * General usage is:
 * 1. calculate confusion matrix using instance method confusionMatrix()
 * 2. calculate a metric using instance method (precision, recall, ...)
 */
class MetricsCalculator {
    private final List<String> groundTruth;
    private final List<String> detected;
    private final long datasetSize;
    private Long tp,fp,tn,fn = null; //confusion matrix elements

    MetricsCalculator(List<String> groundTruth, List<String> detected, long datasetSize) {
        this.groundTruth = groundTruth;
        this.detected = detected;
        this.datasetSize = datasetSize;
    }

    /**
     * Calculates elements of the confusion matrix
     */
    void confusionMatrix() throws MetricException.EmptyDataset {
        if(datasetSize == 0)
            throw new MetricException.EmptyDataset("Cannot calculate confusion matrix on any empty data set.");
        Set<String> keys = new HashSet<>(groundTruth);
        keys.addAll(detected);
        tp = Long.parseUnsignedLong("0");
        fp = Long.parseUnsignedLong("0");
        tn = Long.parseUnsignedLong("0");
        fn = Long.parseUnsignedLong("0");

        for(String k : keys){
            boolean gtPair = groundTruth.contains(k);
            boolean detPair = detected.contains(k);

            if(!detPair){
                ++fn;
            } else if(!gtPair) {
                ++fp;
            } else {
                ++tp;
            }
        }
        tn = datasetSize - fn - fp - tp;
    }

    float precision() throws ArithmeticException{
        if(tp == null)
            throw new IllegalStateException("confusionMatrix() need to be called first");
        return tp / new Float(tp + fp);
    }

    float recall() throws ArithmeticException{
        if(tp == null)
            throw new IllegalStateException("confusionMatrix() need to be called first");
        return tp / new Float(tp + fn);
    }

    float accuracy() throws ArithmeticException{
        if(tp == null)
            throw new IllegalStateException("confusionMatrix() need to be called first");
        return new Float(tp + tn) / (datasetSize);
    }

    float fScore() throws ArithmeticException{
        if(tp == null)
            throw new IllegalStateException("confusionMatrix() need to be called first");
        return (2*tp) / new Float(2*tp + fp + fn);
    }

    /**
     * takes as program arguments:
     * 1. the file path of the ground truth containing changes which columns are:
     * x,y,from,to
     * 2. the file path of the output of ChangeDetector program containing changes which columns are:
     * x,y,from,to
     * 3. data set file path
     * 4. output file path.
     * @param args
     */
    public static void main(String[] args) throws IOException, MetricException.EmptyDataset {
        if(args[0].equals("-h") || args[0].equals("--help"))
            printHelp();

        String gtFile = args[0];
        String detFile = args[1];
        String dataset = args[2];
        String outFile = args[3];

        long nPoints = countRows(dataset);

        ChangeReader gtReader = new ChangeReader(gtFile);
        ChangeReader detReader = new ChangeReader(detFile);

        gtReader.read();
        detReader.read();

        MetricsCalculator metric = new MetricsCalculator(gtReader.getChanges(), detReader.getChanges(), nPoints);
        metric.confusionMatrix();
        float precision = metric.precision();
        float recall = metric.recall();
        float accuracy = metric.accuracy();
        float fscore = metric.fScore();

        StringBuilder output = new StringBuilder();
        output.append("Precision: " + precision + "\n");
        output.append("Recall: " + recall + "\n");
        output.append("Accuracy: " + accuracy + "\n");
        output.append("F-score: " + fscore + "\n");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)))){
            writer.write(output.toString());
        }
    }

    private static void printHelp() {
        System.out.println("program USAGE: [GT] [DETECTED] [DATASET] [OUT]\n" +
                "     [GT]: the file path of the ground truth containing changes which columns are: x,y,from,to\n" +
                "     [DETECTED]: the file path of the output of ChangeDetector program containing changes which columns are: x,y,from,to\n" +
                "     [DATASET] data set file path\n" +
                "     [OUT] output file path.\n");
    }

    private static long countRows(String dataset) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(dataset))){
            long rows = Long.parseUnsignedLong("0");

            while (reader.readLine() != null){
                ++rows;
            }

            return rows;
        }
    }
}
