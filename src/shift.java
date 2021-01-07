
// Imports
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

public class shift {

    /**
     * @param args the command line arguments
     * @throws IOException, FileNotFoundException 
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        // Fix issues I've created
        int year = 2006;
        int start = 58;
        int end = 59;
        
        // If you need to shift backwards, use this block
        /* for(int i = start; i <= end; i++) {
            BufferedImage in = ImageIO.read(new File("tests/" + year + "/" + i + ".png"));
            ImageIO.write(in, "png", new File("tests/" + year + "/" + (i - 2) + ".png"));
        } */
        
        // If you need to shift forward, use this block
        for(int i = end; i >= start; i--) {
            BufferedImage in = ImageIO.read(new File("tests/" + year + "/" + i + ".png"));
            ImageIO.write(in, "png", new File("tests/" + year + "/" + (i + 1) + ".png"));
        }
    }

}
