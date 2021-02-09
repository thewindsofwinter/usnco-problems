/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 *
 * @author winterwind
 */
public class ExtensionManager {
    
    // Lots of code, including this function, from stackoverflow
    public static File changeExtension(File f, String newExtension) {
        int i = f.getName().lastIndexOf('.');
        String name = f.getName().substring(0,i);
        return new File(f.getParent(), name + newExtension);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        File directoryPath = new File("./tests");
        
        Stream<Path> str = Files.walk(Paths.get("./tests"));
        Iterator<Path> it = str.iterator();
        
        while(it.hasNext()) {
            File f = it.next().toFile();
            if(f.isFile() && f.getPath().endsWith(".PNG")) {
                System.out.println("Changing " + f.getPath() + " to .png");
                // Change to JPG first, that way case is correct
                changeExtension(f, ".jpg");
                changeExtension(f, ".png");
            }
        }

    }
}
