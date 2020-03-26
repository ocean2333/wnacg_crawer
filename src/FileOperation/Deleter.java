package FileOperation;

import java.io.File;

public class Deleter {

    public static void delete(String path){
        File f = new File(path);
        f.delete();
    }
}
