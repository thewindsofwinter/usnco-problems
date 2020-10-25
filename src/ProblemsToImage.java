
// Imports
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.rendering.*;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class ProblemsToImage {

    // Empirically determined 
    private static final int FIRST_TOP = 650;
    private static final int TOP = 200;
    private static final int BOTTOM = 3000;
    private static final int LEFT = 130;
    private static final int RIGHT = 2420;
    private static final int CENTER = 1275;
    private static final int GAP = 75;
    
    /**
     * @param args the command line arguments
     * @throws IOException, FileNotFoundException 
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        File dirpath = new File("C:/Users/winterwind/Downloads/Exam PDFs");
        File[] exams = dirpath.listFiles();
        
        // Practice with one at first
        PDDocument document = PDDocument.load(exams[0]);
        // Get the year of the exam for folder naming
        String year = exams[0].getName().substring(0, 4);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        
        for(int page = 0; page < document.getNumberOfPages(); ++page) { 
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // suffix in filename will be used as the file format
            ImageIOUtil.writeImage(bim, "Exam2-" + (page+1) + ".png", 300);
        }
        document.close();
        
    }

}
