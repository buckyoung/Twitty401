/*
 * This class handles oAuth settings, keys, and methods.
 * It is also responsible for manipulating the .atf file.
 * 
 * (atf = Access Token File) - Writes access tokens to disk
 * for future use
 * 
 * @author BuckYoung
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class OAuth {
    /*
     * 
     * 
     * 
     * Consumer Key Information and Methods
     * 
     * 
     * 
     */

    private static final String CONSUMER_KEY = "JNyFWSzfqmOHK8MIfGSp0A";
    private static final String CONSUMER_KEY_SECRET = "EajCLv8P20an9kOm0dNE1gX5fdvOyvK4C1GYO8b0o";

    public static void setConsumerKeys() {
        Twitty401.twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
    }

    /*
     * 
     * 
     * 
     * Access Token and .atf File Information and Methods
     * 
     * 
     * 
     */
    private static String ACCESS_TOKEN;
    private static String ACCESS_TOKEN_SECRET;
    private static final String ATF_FILENAME = "res/savedtokens.atf";
    public static RequestToken REQUEST_TOKEN;
    /*
     * Returns TRUE if file exists
     * Returns FALSE if otherwise
     */

    public static boolean atfIsFound() {
        boolean result;
        File f = new File(ATF_FILENAME);
        if (f.exists()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /*
     * Returns FALSE if verify failed
     * Returns TRUE otherwise
     */
    public static boolean verified() {
        boolean result;
        User user = null;
        //try to verify
        try {
            user = Twitty401.twitter.verifyCredentials();
        } catch (TwitterException te) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, te);
        } 
        
        //check against user
        if (user == null) {
            result = false;
        } else {
            result = true;
        }

        return result; 

    }

    /*
     * Reads the atf file in and sets the Access Tokens 
     */
    public static void readAtf() {
        BufferedReader reader;
        File f = new File(ATF_FILENAME);
        //Try to create a new buffered reader
        try {
            reader = new BufferedReader(new FileReader(f));
            ACCESS_TOKEN = reader.readLine();
            ACCESS_TOKEN_SECRET = reader.readLine();
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Set them
        setAccessTokens();
    }

    /*
     * Will delete the .atf file and check for success
     */
    public static void deleteAtf() {
        File f = new File(ATF_FILENAME);
        if (f.delete()) { //checks for a successful delete
        } else {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, "CannotDeleteATF");
        }
        AccessToken nullToken = null;
        Twitty401.twitter.setOAuthAccessToken(nullToken);

    }
    /*
     * PRIVATE
     * Sets the Access Token and Secret 
     */

    private static void setAccessTokens() {
        if (ACCESS_TOKEN != null && ACCESS_TOKEN_SECRET != null) {
            AccessToken savedToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
            Twitty401.twitter.setOAuthAccessToken(savedToken);//sets the token
        }
    }

    /*
     * PRIVATE
     * Creates a new file with Access Token on line 1 and Access Token Secret on line 2
     */
    private static void writeAtf() {
        //prints access tokens to file
        File f = new File(ATF_FILENAME);
        BufferedWriter writer;
        //try to create the writer
        try {
            writer = new BufferedWriter(new FileWriter(f));
            writer.write(ACCESS_TOKEN + "\n" + ACCESS_TOKEN_SECRET);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * Used to get new Access Tokens from twitter
     * ALSO saves the tokens to file and sets them
     * Returns Success or Failure
     */

    public static boolean requestAccessTokens(String pin) {
        AccessToken accessToken;
        boolean result;
        //try to get Access Tokens
        try {
            accessToken = Twitty401.twitter.getOAuthAccessToken(REQUEST_TOKEN, pin);
            //Define Static Access Tokens
            ACCESS_TOKEN = accessToken.getToken();
            ACCESS_TOKEN_SECRET = accessToken.getTokenSecret();
            //Save tokens to file
            writeAtf();
            //Set Access Tokens
            setAccessTokens();
            result = true;
        } catch (TwitterException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        } catch (IllegalStateException ise){
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ise);
            result = false;
        }
        return result;
    }
    /*
     * Gets, and sets, the Request Token
     */

    public static void getRequestTokens() throws UnknownHostException {
        try {
            //try to get a Request Token 
                REQUEST_TOKEN = Twitty401.twitter.getOAuthRequestToken();
        } catch (TwitterException ex) {
            throw new UnknownHostException();
        }
         
    }
}
