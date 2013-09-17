
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;
import twitter4j.User;

/*
 * Keeps things clean over in PopUp.java!
 * 
 * ALL METHODS HERE ARE CALLED FROM POPUP.JAVA
 */
/**
 *
 * @author BuckYoung
 */
public class PopUpActionListenerLogic {

    //Deletes a user owned tweet
    public static void delete(TweetObject tweet, User user, long sourceID) {
        //DELETETWEET
        try {
            Twitty401.twitter.destroyStatus(sourceID);
        } catch (TwitterException ex) {
            Logger.getLogger(TweetObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        //RELOAD TIMELINE
        if (tweet.parent == Timeline.Type.USER) { //reload the user timeline (after delete)
            Timeline.createTimeline(Timeline.Type.USER, user, null);
        } else if (tweet.parent == Timeline.Type.HOME) { //reload home timeline (after delete)
            Timeline.createTimeline(Timeline.Type.HOME, null, null);
        }
    }
    
    //Favorites a user or other owned tweet
    static void favorite(long sourceID) {
        try {
            Twitty401.twitter.createFavorite(sourceID);
        } catch (TwitterException ex) {
            Logger.getLogger(PopUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //retweets an other owned tweet
    static void retweet(long sourceID) {
        try {
            Twitty401.twitter.retweetStatus(sourceID);
        } catch (TwitterException ex) {
            Logger.getLogger(PopUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Replies to an other owned tweet
    static void reply(User user, long sourceID) {
        TweetPanel.createTweetPanel(user, sourceID);
        Twitty401.mainGUI.setAllUnselected();
    }

    //Browses a URL (in desktop supported browser)
    static void openURL(TweetObject tweet) {
        if (Desktop.isDesktopSupported()) {
            URI uri;
            try {
                uri = new URI(URLLogic.findURL(tweet.getText()));
                Desktop.getDesktop().browse(uri);
            } catch (URISyntaxException ex) {
                Logger.getLogger(PopUp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PopUp.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    static void visitUser(String userName) {
        User user;
        try {
            user = Twitty401.twitter.showUser(userName);
            Timeline.createTimeline(Timeline.Type.USER, user, null); //load their timeline
        } catch (TwitterException ex) {
            Logger.getLogger(PopUpActionListenerLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        PopUp.destroyPopup();
    }
}
