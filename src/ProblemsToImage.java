/**
 * Convert problems to image from exams prior to 2004
 **/


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
    private static final int BOTTOM = 3090;
    private static final int LEFT = 130;
    private static final int RIGHT = 2420;
    private static final int CENTER = 1275;
    private static final int MIN_GAP = 40;
    private static final int MAX_GAP = 52;
    // Adapt to scrape 2021 LOCAL
    private static final String DIR_PATH = "C:/Users/winterwind/Downloads/2021_LOCAL";
    
    /**
     * @param args the command line arguments
     * @throws IOException, FileNotFoundException 
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        // Control what directory you're searching
        File dirpath = new File(DIR_PATH);
        File[] exams = dirpath.listFiles();
        
        // Practice with one at first [DONE]
        for(int i = 0; i < 1; i++) {
            PDDocument document = PDDocument.load(exams[i]);
            // Get the year of the exam for folder naming
            String year = exams[i].getName().substring(0, 4);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            
            // Create directory 
            new File("tests/locals/" + year).mkdirs();

            // Start on the first page, keep going until there are sixty problems
            int problemNumber = 1;
            int pageNumber = FIRST_PAGE;

            while(pageNumber < document.getNumberOfPages() && problemNumber <= 60) { 
                BufferedImage currentPage = pdfRenderer.renderImageWithDPI(pageNumber, 300, ImageType.RGB);
                ImageIOUtil.writeImage(currentPage, "Exam2-" + (pageNumber+1) + ".png", 300);
                
                // Start out in the left column
                int currentY = TOP;
                int lastCut = TOP;
                int consecutiveBlankLines = 0;

                // Different top value
                if(pageNumber == FIRST_PAGE) {
                    currentY = FIRST_TOP;
                    lastCut = FIRST_TOP;
                }

                // Code for the first column
                while(currentY < BOTTOM && problemNumber <= 60) {
                    // Check if there are already seventy-five lines
                    if(consecutiveBlankLines > MAX_GAP) {
                        // Crop and output image
                        int currentCut = currentY - MAX_GAP/2;
                        int width = CENTER - LEFT;
                        int height = currentCut - lastCut;

                        // Eliminate large white spaces with minimal text
                        if(height - MAX_GAP < 10) {
                            consecutiveBlankLines = 0;
                            lastCut = currentCut;
                        }
                        else if(height > 180) {
                            BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                            cropped.getGraphics().drawImage(currentPage, 0, 0, width, height, 
                                    LEFT, lastCut, CENTER, currentCut, null);

                            File outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                            ImageIO.write(cropped, "png", outputFile);

                            consecutiveBlankLines = 0;
                            lastCut = currentCut;
                            problemNumber++;
                        }
                        else {
                            lastCut = currentCut;
                            consecutiveBlankLines = 0;
                        }
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
                        if(consecutiveBlankLines > MIN_GAP) {
                            int currentCut = currentY - MAX_GAP/2;
                            int width = CENTER - LEFT;
                            int height = currentCut - lastCut;

                            if(height > 100) {
                                BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                                cropped.getGraphics().drawImage(currentPage, 0, 0, width, height, 
                                        LEFT, lastCut, CENTER, currentCut, null);

                                File outputFile = new File("temp/eval.png");
                                ImageIO.write(cropped, "png", outputFile);

                                System.out.println("Check temp");
                                Scanner sc = new Scanner(System.in);

                                if(sc.nextBoolean()) {
                                    outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                                    ImageIO.write(cropped, "png", outputFile);
                                    lastCut = currentCut;
                                    problemNumber++;
                                }
                            }
                        }
                        consecutiveBlankLines = 0;
                    }

                    currentY++;
                }

                // Bad code (duplicated code for second column) -- fix later
                consecutiveBlankLines = 0;
                currentY = TOP;
                lastCut = TOP;

                // Different top value
                if(pageNumber == FIRST_PAGE) {
                    currentY = FIRST_TOP;
                    lastCut = FIRST_TOP;
                }

                while(currentY < BOTTOM && problemNumber <= 60) {
                    // Check if there are already seventy-five lines
                    if(consecutiveBlankLines > MAX_GAP) {
                        // Crop and output image
                        int currentCut = currentY - MAX_GAP/2;
                        int width = RIGHT - CENTER;
                        int height = currentCut - lastCut;

                        // Eliminate large white spaces with minimal text
                        if(height - MAX_GAP < MAX_GAP/2) {
                            consecutiveBlankLines = 0;
                            lastCut = currentCut;
                        }
                        else if(height > 180) {
                            BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                            cropped.getGraphics().drawImage(currentPage, 0, 0, width, height, 
                                    CENTER, lastCut, RIGHT, currentCut, null);

                            File outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                            ImageIO.write(cropped, "png", outputFile);

                            consecutiveBlankLines = 0;
                            lastCut = currentCut;
                            problemNumber++;
                        }
                        else {
                            lastCut = currentCut;
                            consecutiveBlankLines = 0;
                        }
                    }

                    // Analyze line
                    boolean allWhite = true;
                    for(int x = CENTER; x < RIGHT; x++) {
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
                        if(consecutiveBlankLines > MIN_GAP) {
                            int currentCut = currentY - MAX_GAP/2;
                            int width = RIGHT - CENTER;
                            int height = currentCut - lastCut;

                            if(height > 100) {
                                BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                                cropped.getGraphics().drawImage(currentPage, 0, 0, width, height, 
                                        CENTER, lastCut, RIGHT, currentCut, null);

                                File outputFile = new File("temp/eval.png");
                                ImageIO.write(cropped, "png", outputFile);

                                System.out.println("Check temp");
                                Scanner sc = new Scanner(System.in);

                                if(sc.nextBoolean()) {
                                    outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                                    ImageIO.write(cropped, "png", outputFile);
                                    lastCut = currentCut;
                                    problemNumber++;
                                }
                            }
                        }
                        consecutiveBlankLines = 0;
                    }

                    currentY++;
                }

                pageNumber++;

                // suffix in filename will be used as the file format
                ImageIOUtil.writeImage(currentPage, "Exam2-" + (pageNumber+1) + ".png", 300);
            }
            document.close();

        }
    }
}
