/*
 * 
 * 
 * Twitty401: A CS401 Final Project by
 * Justin Ruoff, Neel Krishna, and Buck Young
 * 
 *  Don't have a Twitter account? Use our test-account! 
 *      @: "TwittyTwester", pass: "twitty401"
 * 
 */

import java.awt.Point;
import javax.swing.ImageIcon;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class Twitty401 {

    public static Twitter twitter;
    public static MainGUI mainGUI;
    private static SignInGUI siGUI;
    private static FirstTimeUserGUI ftuGUI;
    public static final ImageIcon logo = new ImageIcon("res/99logo.jpg");

    public static void main(String[] args) {
        //creates a useable twitter object
        createTwitter();
        //Determines which UI to open on
        decideInitialGUI();
        //NOTE: Everything from here on out is handled by MAIN GUI
        //The flow of the program is Here, then bounces around Sign-inGUI and FirstTimeUserGUI -- then from Signin, MAIN GUI is created, then all logic is handled in mainGUI and Beyond
    }


    /*
     * Decides which GUI to display at Start of Program 
     */
    private static void decideInitialGUI() {

        //Attempt to find .ATF file
        if (OAuth.atfIsFound()) {
            OAuth.readAtf();
            if (OAuth.verified()) {
                //Display SI GUI
                siGUI = new SignInGUI();
            } else { //if !verified
                OAuth.deleteAtf();
                //display FTU GUI
                 ftuGUI = new FirstTimeUserGUI();
            }
        } else { //if !atfisfound
            //display FTU GUI
             ftuGUI = new FirstTimeUserGUI();
        }
    }

    /*
     * creates FTUgui (called from sign in gui)
     */
    public static void createFTUGUI(Point point) {
        ftuGUI = new FirstTimeUserGUI(point);
        siGUI.dispose(); 

    }
    
    /*
     * creates siGUI (called form FTUGUI)
     */
    public static void createSIGUI(Point point) {
        siGUI = new SignInGUI(point);
        ftuGUI.dispose();
    }


    /*
     * Creates/Destroys a new twitter object to release old user information 
     */
    public static void resetTwitter() {
        twitter.shutdown();
        createTwitter();
    }

    private static void createTwitter() {
        //Create a valid Twitter object.
        twitter = new TwitterFactory().getInstance();
        OAuth.setConsumerKeys();
    }

    /*
     * sets MainGUI Object so we can reference it from elsewhere.
     */
    public static void setMainGUI(MainGUI main) {
        mainGUI = main;
    }

    
}