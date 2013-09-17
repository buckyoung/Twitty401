
import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.shorten;
import com.rosaloves.bitlyj.BitlyException;
import com.rosaloves.bitlyj.ShortenedUrl;
import java.util.regex.Matcher;

/*
 * Includes Bitly and URL methods
 */
/**
 *
 * @author BuckYoung
 */
public class URLLogic {

    /*
     * Input: Text with URL
     * Output: Text with Shortened-URL
     */
    public static String parseAndShorten(String text) {
        String result = text;
        String url = findURL(text); //find the URL
        String shortURL = shortenURL(url); //shortened
        if (shortURL != null) { //if bitly worked
            result = text.replaceFirst(url, shortURL); //saves result
        }
        return result; //return whole block
    }
    /*
     * Input: String (text)
     * Returns: String (url)
     */

    public static String findURL(String text) {
        String result;
        int startIndex = text.indexOf("http"); //finds the url
        int endIndex = text.indexOf(" ", startIndex); //finds the end of it
        //checks if endindex returned -1
        if (endIndex > 0) {
            result = text.substring(startIndex, endIndex); //create url        
        } else {
            result = text.substring(startIndex); //if link goes to end of text (and there is no space to find in .indexOF above)           
        }
        return result;
    }

    /*
     * calls bitly service to shorten the url, returns a shortened url string
     */
    private static String shortenURL(String url) {
        String result;
        try {
            ShortenedUrl sURL = as("professir", "R_1b7bea9b96ea292e46f5da0c27a3f218").call(shorten(url));
            String shortString = sURL.getShortUrl();
            String shortLiteral = Matcher.quoteReplacement(shortString); //returns a literal (supresses special charactar meaning)
            result = shortLiteral;
        } catch (BitlyException be) { //if string is already shortened
            result = null;
        }
        return result;
    }
}
