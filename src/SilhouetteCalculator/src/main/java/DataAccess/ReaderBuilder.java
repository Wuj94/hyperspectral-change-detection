package DataAccess;

public class ReaderBuilder {
    public enum Implementation {
        JGALILEE, BRIZZIB, YHFYHF, ARTIFICIAL, INDIAN_PINES
    }

    private static ReaderBuilder builder = null;

    private ReaderBuilder(){}

    public static ReaderBuilder getInstance(){
        if(builder == null){
            builder = new ReaderBuilder();
        }
        return builder;
    }

    /**
     * Returns an instance of a reader for the implementation represented by imp
     * @param impl
     * @return
     */
    public DatasetReader getReader(Implementation impl) {
        switch (impl){
            case JGALILEE: return new JgalileeReader();
            case YHFYHF: return new YhfyhfReader();
            case BRIZZIB:return new BrizziBReader();
            case INDIAN_PINES:return new IndianPinesReader();
            default: return new ArtificialReader();
        }
    }
}
