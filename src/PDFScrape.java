// Imports
import java.awt.Desktop;
import java.util.*;
import java.io.*;
import java.net.URI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class PDFScrape {

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }   

    
    /**
     * @param args the command line arguments
     * @throws IOException, FileNotFoundException 
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        Document d = Jsoup.connect("https://www.acs.org/content/acs/en/education"
                + "/students/highschool/olympiad/pastexams.html").get();
        
        Elements links = d.getElementsByTag("a");
        for(Element link : links) {
            if(link.text().equals("Part I")) {
                String url = "https://www.acs.org" + link.attr("href");
                openWebpage(URI.create(url));
            }
        }
    }

}
