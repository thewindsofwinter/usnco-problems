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
    
    // THIS CODE DOESNT WORK DONT USE IT
    
    // Lots of code, including this function, from stackoverflow
    public static void modify(File file) 
    {
        int index = file.getPath().lastIndexOf(".");
        //print filename
        //System.out.println(file.getName().substring(0, index));
        //print extension
        //System.out.println(file.getName().substring(index));
        //use file.renameTo() to rename the file
        file.renameTo(new File(file.getPath().substring(0, index) + ".png"));
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        // Whoops, didnt work
        File directoryPath = new File("./tests");
        
        Stream<Path> str = Files.walk(Paths.get("./tests"));
        Iterator<Path> it = str.iterator();
        
        while(it.hasNext()) {
            File f = it.next().toFile();
            if(f.isFile() && f.getPath().endsWith(".PNG")) {
                System.out.println("Changing " + f.getPath() + " to .png");
                modify(f);
            }
        }

    }
}
