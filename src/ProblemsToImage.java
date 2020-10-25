
// Imports
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.rendering.*;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class ProblemsToImage {

    // Empirically determined 
    private static final int FIRST_TOP = 650;
    private static final int FIRST_PAGE = 2;
    private static final int TOP = 200;
    private static final int BOTTOM = 3000;
    private static final int LEFT = 130;
    private static final int RIGHT = 2420;
    private static final int CENTER = 1275;
    private static final int MIN_GAP = 75;
    
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
        
        // Create directory 
        new File(year).mkdirs();
        
        // Start on the first page, keep going until there are sixty problems
        int problemNumber = 1;
        int pageNumber = FIRST_PAGE;
        
        while(pageNumber < document.getNumberOfPages() && problemNumber <= 60) { 
            BufferedImage currentPage = pdfRenderer.renderImageWithDPI(pageNumber, 300, ImageType.RGB);

            // Start out in the left column
            boolean secondColumn = false;
            int currentY = TOP;
            int lastCut = TOP;
            int consecutiveBlankLines = 0;
            
            // Different top value
            if(pageNumber == FIRST_PAGE) {
                currentY = FIRST_TOP;
                lastCut = FIRST_TOP;
            }
            
            while(currentY < BOTTOM && problemNumber <= 60) {
                // Check if there are already seventy-five lines
                if(consecutiveBlankLines > MIN_GAP) {
                    // Crop and output image
                    int currentCut = currentY - MIN_GAP/2;
                    int width = CENTER - LEFT;
                    int height = currentCut - lastCut;
                            
                    BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    cropped.getGraphics().drawImage(currentPage, 0, 0, width, height, 
                            LEFT, lastCut, CENTER, currentCut, null);
                    
                    File outputFile = new File(year + "/" + problemNumber + ".png");
                    ImageIO.write(cropped, "png", outputFile);
                    
                    consecutiveBlankLines = 0;
                    lastCut = currentCut;
                    problemNumber++;
                }
                
                // Analyze line
                boolean allWhite = true;
                for(int x = LEFT; x < CENTER; x++) {
                    int RGB = currentPage.getRGB(x, currentY);
                    if(RGB != Color.WHITE.getRGB()) {
                        allWhite = false;
                        break;
                    }
                }
                
                if(allWhite) {
                    consecutiveBlankLines++;
                }
                else {
                    consecutiveBlankLines = 0;
                }
                
                currentY++;
            }
            
            // Break for now for testing
            break;
            // suffix in filename will be used as the file format
            // ImageIOUtil.writeImage(bim, "Exam2-" + (page+1) + ".png", 300);
        }
        document.close();
        
    }

}
