package Business;

/**
 * Generic exception about silhouette calculation
 */
public class SilhouetteException extends Exception{
    public SilhouetteException(){}

    public SilhouetteException(String message){
        super(message);
    }

    public static class DifferentLengthException extends SilhouetteException{
        public DifferentLengthException(){}

        public DifferentLengthException(String message) {
            super(message);
        }
    }

    public static class EmptyDatasetException extends SilhouetteException {
        public EmptyDatasetException(){
        }
        public EmptyDatasetException(String message){
            super(message);
        }
    }
}
