package fileManager;

import java.io.File;

public class FileLoader {
    private File file;
    public FileLoader(File file){
        this.file = file;
    }
    public void parseFile(){
        System.out.println("parsing "+ file.getAbsolutePath());
    }
}
