package Business;

public class RandIndexException extends Exception {
    /**
     * Generic rand index exception
     * @param message
     */
    public RandIndexException(String message){
        super(message);
    }

    /**
     * Refers to datasets with 0 clusters.
     */
    public static class EmptyDatasetException extends RandIndexException {

        public EmptyDatasetException(String message) {
            super(message);
        }
    }
}
