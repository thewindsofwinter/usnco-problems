
// Imports
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class ModernProblemsToImage {

    // Empirically determined 
    private static final int FIRST_TOP = 650;
    private static final int FIRST_PAGE = 2;
    private static final int TOP = 200;
    private static final int BOTTOM = 3000;
    private static final int LEFT = 120;
    private static final int RIGHT = 2420;
    private static final int CENTER = 1275;
    
    private static final int NUMBER_LEFT = 50;
    private static final int NUMBER_RIGHT = 115;
    private static final int NUMBER_HEIGHT = 50;
    private static final int GAP = 70;
    // Adapt to scrape 2021 LOCAL
    private static final String DIR_PATH = "C:/Users/winterwind/Downloads/2021_LOCAL";
    
    /**
     * @param args the command line arguments
     * @throws IOException, FileNotFoundException 
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        File dirpath = new File(DIR_PATH);
        File[] exams = dirpath.listFiles();
        
        // Practice with one at first [DONE]
        for(int i = 0; i < exams.length; i++) {
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
                // ImageIOUtil.writeImage(currentPage, "Exam2-" + (pageNumber+1) + ".png", 300);
                
                // Start out in the left column
                int currentY = TOP;
                int lastCut = TOP;

                // Detect if there is a fake problem number
                boolean fakeFlag = true;

                // Different top value
                if(pageNumber == FIRST_PAGE) {
                    currentY = FIRST_TOP;
                    lastCut = FIRST_TOP;
                }

                // Code for the first column
                while(currentY < BOTTOM && problemNumber <= 60) {
                    
                    boolean allWhite = true;
                    for(int x = LEFT + NUMBER_LEFT; x < LEFT + NUMBER_RIGHT; x++) {
                        int RGB = currentPage.getRGB(x, currentY);
                        if(RGB != Color.WHITE.getRGB()) {
                            allWhite = false;
                            break;
                        }
                    }

                    if(!allWhite && fakeFlag) {
                        fakeFlag = false;
                        
                        // Check to make sure there is gap after number
                        int countGap = 0;
                        while(countGap < GAP && currentY < BOTTOM) {
                            allWhite = true;
                            for(int x = LEFT + NUMBER_LEFT; x < LEFT + NUMBER_RIGHT; x++) {
                                int RGB = currentPage.getRGB(x, currentY);
                                if(RGB != Color.WHITE.getRGB()) {
                                    allWhite = false;
                                    break;
                                }
                            }
                            
                            if(allWhite) {
                                countGap++;
                            }
                            else {
                                countGap = 0;
                            }
                            currentY++;
                        }
                    }
                    else if(!allWhite && !fakeFlag) {
                        int currentCut = currentY - NUMBER_HEIGHT/2;
                        int width = CENTER - LEFT;
                        int height = currentCut - lastCut;

                        if(height > 50) {
                            BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                            cropped.getGraphics().drawImage(currentPage, 0, 0, width, height, 
                                    LEFT, lastCut, CENTER, currentCut, null);
                                
                            File outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                            ImageIO.write(cropped, "png", outputFile);
                                
                            lastCut = currentCut;
                            problemNumber++;
                        }
                        
                        // Check for fake
                        for(int x = LEFT + NUMBER_LEFT; x < LEFT + NUMBER_RIGHT; x++) {
                            int RGB = currentPage.getRGB(x, currentY + GAP);
                            if(RGB != Color.WHITE.getRGB()) {
                                fakeFlag = true;
                                break;
                            }
                        }
                        
                        // Check to make sure there is gap after number
                        int countGap = 0;
                        while(countGap < GAP && currentY < BOTTOM) {
                            allWhite = true;
                            for(int x = LEFT + NUMBER_LEFT; x < LEFT + NUMBER_RIGHT; x++) {
                                int RGB = currentPage.getRGB(x, currentY);
                                if(RGB != Color.WHITE.getRGB()) {
                                    allWhite = false;
                                    break;
                                }
                            }
                            
                            if(allWhite) {
                                countGap++;
                            }
                            else {
                                countGap = 0;
                            }
                            currentY++;
                        }
                    }
                    
                    currentY++;
                }
                
                // Get last problem
                boolean lastWhite = true;
                int count = 0;
                while(lastWhite) {
                    currentY--;
                    count++;
                    for(int x = LEFT; x < CENTER; x++) {
                        int RGB = currentPage.getRGB(x, currentY);
                        if(RGB != Color.WHITE.getRGB()) {
                            lastWhite = false;
                            break;
                        }
                    }
                }
                
                int bottomCut = Math.min(currentY + NUMBER_HEIGHT/2, currentY + count);
                int w = CENTER - LEFT;
                int h = bottomCut - lastCut;

                if(h > 100 && problemNumber < 60) {
                    BufferedImage cropped = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    cropped.getGraphics().drawImage(currentPage, 0, 0, w, h, 
                            LEFT, lastCut, CENTER, bottomCut, null);

                    File outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                    ImageIO.write(cropped, "png", outputFile);
                    
                    problemNumber++;
                }
                

                // Bad code (duplicated code for second column) -- fix later
                currentY = TOP;
                lastCut = TOP;
                fakeFlag = true;

                // Different top value
                if(pageNumber == FIRST_PAGE) {
                    currentY = FIRST_TOP;
                    lastCut = FIRST_TOP;
                }

                while(currentY < BOTTOM && problemNumber <= 60) {
                    // Analyze line
                    boolean allWhite = true;
                    for(int x = CENTER + NUMBER_LEFT; x < CENTER + NUMBER_RIGHT; x++) {
                        int RGB = currentPage.getRGB(x, currentY);
                        if(RGB != Color.WHITE.getRGB()) {
                            allWhite = false;
                            break;
                        }
                    }
                    
                    if(!allWhite && fakeFlag) {
                        fakeFlag = false;
                        
                        // Check to make sure there is gap after number
                        int countGap = 0;
                        while(countGap < GAP && currentY < BOTTOM) {
                            allWhite = true;
                            for(int x = CENTER + NUMBER_LEFT; x < CENTER + NUMBER_RIGHT; x++) {
                                int RGB = currentPage.getRGB(x, currentY);
                                if(RGB != Color.WHITE.getRGB()) {
                                    allWhite = false;
                                    break;
                                }
                            }
                            
                            if(allWhite) {
                                countGap++;
                            }
                            else {
                                countGap = 0;
                            }
                            currentY++;
                        }
                    }
                    else if(!allWhite && !fakeFlag) {
                        int currentCut = currentY - NUMBER_HEIGHT/2;
                        int width = RIGHT - CENTER;
                        int height = currentCut - lastCut;

                        if(height > 50) {
                            BufferedImage cropped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                            cropped.getGraphics().drawImage(currentPage, 0, 0, width, height, 
                                    CENTER, lastCut, RIGHT, currentCut, null);
                                
                            File outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                            ImageIO.write(cropped, "png", outputFile);
                                
                            lastCut = currentCut;
                            problemNumber++;
                        }
                        
                        
                        // Check for fake
                        for(int x = CENTER + NUMBER_LEFT; x < CENTER + NUMBER_RIGHT; x++) {
                            int RGB = currentPage.getRGB(x, currentY + GAP);
                            if(RGB != Color.WHITE.getRGB()) {
                                fakeFlag = true;
                                break;
                            }
                        }
                        
                        // Check to make sure there is gap after number
                        int countGap = 0;
                        while(countGap < GAP && currentY < BOTTOM) {
                            allWhite = true;
                            for(int x = CENTER + NUMBER_LEFT; x < CENTER + NUMBER_RIGHT; x++) {
                                int RGB = currentPage.getRGB(x, currentY);
                                if(RGB != Color.WHITE.getRGB()) {
                                    allWhite = false;
                                    break;
                                }
                            }
                            
                            if(allWhite) {
                                countGap++;
                            }
                            else {
                                countGap = 0;
                            }
                            currentY++;
                        }
                    }

                    currentY++;
                }
                
                // Get last problem
                lastWhite = true;
                count = 0;
                while(lastWhite) {
                    currentY--;
                    count++;
                    for(int x = CENTER; x < RIGHT; x++) {
                        int RGB = currentPage.getRGB(x, currentY);
                        if(RGB != Color.WHITE.getRGB()) {
                            lastWhite = false;
                            break;
                        }
                    }
                }
                
                bottomCut = Math.min(currentY + NUMBER_HEIGHT/2, currentY + count);
                w = RIGHT - CENTER;
                h = bottomCut - lastCut;

                if(h > 100 && problemNumber < 60) {
                    BufferedImage cropped = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    cropped.getGraphics().drawImage(currentPage, 0, 0, w, h, 
                            CENTER, lastCut, RIGHT, bottomCut, null);

                    File outputFile = new File("tests/locals/" + year + "/" + problemNumber + ".png");
                    ImageIO.write(cropped, "png", outputFile);
                    
                    problemNumber++;
                }

                pageNumber++;

                // suffix in filename will be used as the file format
                // ImageIOUtil.writeImage(currentPage, "Exam2-" + (pageNumber+1) + ".png", 300);
            }
            document.close();

        }
    }

}
