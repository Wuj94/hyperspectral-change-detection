import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a comma-separated-value file containing changes which columns are:
 * x,y,from,to
 * General usage is:
 * 1. read the file using instance method read()
 * 2. get the in memory representation of the file using instance method getChanges()
 */
class ChangeReader {
    private List<String> changes = null;
    private File file;

    /**
     * @param filename path of the file containing changes
     */
    ChangeReader(String filename){
        file = new File(filename);
    }

    /**
     * Reads the file
     * @throws IOException
     */
    void read() throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            changes = new ArrayList<>();
            String line = null;
            while((line = reader.readLine()) != null){
                String[] columns = line.split(",");
                String key = Float.parseFloat(columns[0]) + "," + Float.parseFloat(columns[1]);
                changes.add(key);
            }
        }
    }

    /**
     * Return in memory representation of the file. To be called after read()
     * @return
     */
    List<String> getChanges(){
        if(changes == null)
            throw new IllegalStateException("read() need to be called first.");
        return changes;
    }

}
