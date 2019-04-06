public class MetricException extends Exception {
    public MetricException(String message) {
        super(message);
    }

    public static class EmptyDataset extends MetricException{
        public EmptyDataset(String message){
            super(message);
        }

        public EmptyDataset(){
            super("Empty data set.");
        }
    }
}
