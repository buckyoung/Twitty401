
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import twitter4j.TwitterException;
import twitter4j.User;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author BuckYoung
 */
public class PopUp extends JPopupMenu {

    //Sorta-Singleton Pattern (it changes dynamically, so it cannot be final)
    private static JPopupMenu instance;
    private static boolean popUpExists = false;
    //Menus 
    private static JPopupMenu userOwns = new JPopupMenu();
    private static JPopupMenu otherOwns = new JPopupMenu();
    //Special Menu item (we gotta check for it!)
    private static JMenuItem openURLMenuItem = new JMenuItem("Open URL...");
    private static JMenuItem visitUserMenuItem = new JMenuItem();
    //Passed From TweetObject
    private static TweetObject tweet;
    //Created with TweetObject
    private static long sourceID;
    private static User user;
    //UserMenu and OtherMenu (if user owns or other owns)
    private static MenuElement[] userSubElements;
    private static MenuElement[] otherSubElements;
    //Mentions:
    public static ArrayList<String> mentions;
    private ActionListener AL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equalsIgnoreCase("Delete")) { //will only show up on UserOwns
                PopUpActionListenerLogic.delete(tweet, user, sourceID);
            } else if (ae.getActionCommand().equalsIgnoreCase("Favorite")) { //Show up on user and other
                PopUpActionListenerLogic.favorite(sourceID);
            } else if (ae.getActionCommand().equalsIgnoreCase("Retweet")) { //other
                PopUpActionListenerLogic.retweet(sourceID);
            } else if (ae.getActionCommand().equalsIgnoreCase("Reply")) { //other
                PopUpActionListenerLogic.reply(user, sourceID);
            } else if (ae.getActionCommand().equalsIgnoreCase("Open URL...")) { //user and other
                PopUpActionListenerLogic.openURL(tweet);
            } else if (ae.getActionCommand().contains("Visit")) { //has a mention
                String textOnMenuItem = ae.getActionCommand();
                textOnMenuItem.replace("Visit", " "); //gets rid of visit
                textOnMenuItem.trim(); //trims down to just username
                PopUpActionListenerLogic.visitUser(textOnMenuItem);
            }
            //Close the popup down
            destroyPopup();
        }
    };

    /*
     * Popup Constructor! (create menus)
     */
    public PopUp() {
        createMenus();
    }

    /*
     * Creates a popup if one is not already visible - if one is visible, destroys it! and does not create a new one!
     * WHEN CLICKING TWEET TEXT AREA
     */
    public static void getInstance(Point mousePoint, TweetObject tweet) {
        if (!popUpExists) { //If instance is not showing, create one!
            //User and ID must be set before creating a new popup!
            PopUp.user = tweet.getUser();
            PopUp.sourceID = tweet.getSourceID();
            PopUp.tweet = tweet;
            instance = new JPopupMenu();
            //Add Proper MenuItems
            determinePopup();
            //setProperties
            instance.setLocation(mousePoint);
            popUpExists = true;
            instance.setVisible(true);
        } else {
            destroyPopup();

        }
    }

    /*
     * Destroys the popup -- called from many other locations!
     */
    public static void destroyPopup() {
        if (instance != null) {
            instance.setVisible(false);
            popUpExists = false;
            instance = null;
        }
    }

    /*
     * Creates three menus and adds action listeners
     */
    private void createMenus() {

        //
        //
        //USER OWNS:
        //
        String[] userOwnsTweet = new String[2]; //Items to display if a user owns a tweet
        //user owns
        userOwnsTweet[0] = "Delete";
        userOwnsTweet[1] = "Favorite";
        //Create menu's, and add action listener
        JMenuItem menuItem;
        for (String string : userOwnsTweet) {
            menuItem = new JMenuItem(string);
            menuItem.addActionListener(AL);
            userOwns.add(menuItem);

        }
        //
        //
        //OTHER OWNS:
        //
        String[] othersTweet = new String[3]; //Items to display if target tweet belongs to another person
        //other owns
        othersTweet[0] = "Retweet";
        othersTweet[1] = "Favorite";
        othersTweet[2] = "Reply";
        //Create menus, add action listener
        for (String string : othersTweet) {
            menuItem = new JMenuItem(string);
            menuItem.addActionListener(AL);
            otherOwns.add(menuItem);
        }
        //Creates an element holder (so we can add the menu items dynamically later:
        userSubElements = userOwns.getSubElements();
        otherSubElements = otherOwns.getSubElements();
        //Special case: 
        openURLMenuItem.addActionListener(AL);
        visitUserMenuItem.addActionListener(AL);
    }

    /*
     * Determine which menu items to show
     */
    private static void determinePopup() {
        try {
            //Should we include an Open Url Item?            
            if (tweet.hasURL) { //HAS URL!
                instance.add(openURLMenuItem);

            }
            if (tweet.hasMention) {
                //Includes mention and separator
                mentions = getMentions(tweet.getText());
                for (String userMentioned : mentions) {
                    visitUserMenuItem.setText("Visit " + userMentioned);
                    instance.add(visitUserMenuItem);
                }
            }
            if (tweet.hasURL || tweet.hasMention) {
                instance.addSeparator();
            }
            //Decide on Own or Other:
            if (Twitty401.twitter.getScreenName().equalsIgnoreCase(user.getScreenName())) { //Own
                for (MenuElement element : userSubElements) {
                    instance.add((JMenuItem) element);
                }
            } else {
                for (MenuElement element : otherSubElements) { //OTHER
                    instance.add((JMenuItem) element);
                }
            }
        } catch (TwitterException ex) {
            Logger.getLogger(PopUp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(PopUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Input: String (text)
     * Returns: ArrayList of Mentions (strings ... /usernames/)
     */
    public static ArrayList<String> getMentions(String text) {
        ArrayList<String> results = new ArrayList();
        int count = 0;
        int startIndex; //finds the first mention
        int endIndex = 0; //finds the end of it

        do { //WHILE WE CAN PARSE THE TEXT, ADD MENTIONS TO AN ARRAY LIST
            startIndex = endIndex;
            startIndex = text.indexOf("@", startIndex)+1; //finds the first mention, and doesnt include @sign
            if (startIndex<=0){
                break;
            }
            endIndex = text.indexOf(" ", startIndex);
            System.out.println(startIndex +"  "+ endIndex);
            //checks for EOTweet
            if (endIndex > 0) {
                results.add(text.substring(startIndex, endIndex)); //creates mention     
                System.out.println(results.get(count));
            } else {
                results.add(text.substring(startIndex)); //creates mention if last word of tweet           
            }
            //Lets check for punctuation which accidentally is included in the username
            int lastLetterIndex = results.get(count).length() - 1;
            //checks if there is punctuation at the end
            // if the last letter of the result is not a character or a digit //
            //if (!Character.isLetter(results.get(count).charAt(lastLetterIndex)) || !Character.isDigit(results.get(count).charAt(lastLetterIndex))) {
            //    results.set(count, results.get(count).substring(0, lastLetterIndex - 1)); //get rid of punct at end
            //}
            count += 1;
        } while (startIndex > 0);
        return results;
    }
}
