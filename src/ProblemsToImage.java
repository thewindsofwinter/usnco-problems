
// Imports
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.rendering.*;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class ProblemsToImage {

    /**
     * @param args the command line arguments
     * @throws IOException, FileNotFoundException 
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        File dirpath = new File("C:/Users/winterwind/Downloads/Exam PDFs");
        File[] exams = dirpath.listFiles();
        
        PDDocument document = PDDocument.load(exams[10]);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        
        for(int page = 0; page < document.getNumberOfPages(); ++page) { 
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // suffix in filename will be used as the file format
            ImageIOUtil.writeImage(bim, "Exam2-" + (page+1) + ".png", 300);
        }
        document.close();
        
    }

}
