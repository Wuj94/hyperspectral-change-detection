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

    public DatasetReader getReader(Implementation impl) {
        switch (impl){
            case JGALILEE: return new JgalileeReader();
            case YHFYHF: return new YhfyhfReader();
            case ARTIFICIAL: return new ArtificialReader();
            case INDIAN_PINES: return new IndianPinesReader();
            default: return new BrizziBReader();
        }
    }
}
