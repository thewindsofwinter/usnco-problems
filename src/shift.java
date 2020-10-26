
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
        int year = 2003;
        int start = 53;
        int end = 59;
        
        /* for(int i = start; i <= end; i++) {
            BufferedImage in = ImageIO.read(new File("tests/" + year + "/" + i + ".png"));
            ImageIO.write(in, "png", new File("tests/" + year + "/" + (i - 1) + ".png"));
        } */
        
        for(int i = end; i >= start; i--) {
            BufferedImage in = ImageIO.read(new File("tests/" + year + "/" + i + ".png"));
            ImageIO.write(in, "png", new File("tests/" + year + "/" + (i + 1) + ".png"));
        }
    }

}
