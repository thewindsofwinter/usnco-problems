
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author winterwind
 */
public class SolutionsToCSV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        File dirpath = new File("C:/Users/winterwind/Downloads/Exam PDFs");
        File[] exams = dirpath.listFiles();
        
        // Practice with one at first [WIP]
        for(int i = 0; i < exams.length; i++) {
            PDDocument pd = PDDocument.load(exams[i]);
            
            // Get the year of the exam for folder naming
            String year = exams[i].getName().substring(0, 4);
            PDFRenderer pdfRenderer = new PDFRenderer(pd);
            
            // Create directory 
            new File("answers/" + year).mkdirs();
            int pageNumber = pd.getNumberOfPages() - 1;
            
            BufferedImage currentPage = pdfRenderer.renderImageWithDPI(pageNumber, 300, ImageType.RGB);
            ImageIOUtil.writeImage(currentPage, "answers/" + year + "/Exam-" + (pageNumber+1) + ".png", 300);
            
            // Make sure to close at end
            pd.close();
        }
    }
    
}
