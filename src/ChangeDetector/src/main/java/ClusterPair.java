class ClusterPair {
    private int c1 = -1;
    private int c2 = -1;

    ClusterPair(int c){
        c1 = c;
    }

    void setC2(int c){
        if(c2 == -1)
            c2 = c;
        else
            throw new IllegalStateException("Cluster pair is complete.");
    }

    boolean changes() {
        if(c2 == -1)
            return false;
        return c1 != c2;
    }

    public String toString(){
        return c1 + "," + c2;
    }

}
