
import java.awt.Cursor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 *
 * @author BuckYoung
 */
public class Timeline {

    public enum Type {

        HOME, MENTIONS, USER, SEARCH, FAVORITES
    };
    private static Type type;
    private static User user;
    private static Query query;
    private static Paging paging = new Paging();
    private static QueryResult queryResult = null;

    /*
     * Timeline creation -- NEW TIMELINE
     */
    public static void createTimeline(Type type, User user, Query query) {
        MainGUI.setScrollToTop();
        MainGUI.setWaiting();
        Timeline.type = type;
        Timeline.user = user;
        Timeline.query = query;
        //Resets page to 1:
        Timeline.paging.setPage(1);
        //GetMainReady to add elements
        MainGUI.resetMainPanel(); //Removes old, sets layoutmanager
        //If we are on page one, we may need to add some panels!
        //Check if we should add a searchpanel
        if (Timeline.type == Type.SEARCH) {
            SearchPanel.addSearchPanel(Timeline.query);
        }
        //Check if we should add a UserPanel
        if (Timeline.type == Type.USER) {
            UserPanel.addUserPanel(Timeline.user);
            FollowPanel.addFollowPanel(Timeline.user);
        }
        if (Timeline.type == Type.FAVORITES) {
            UserPanel.addUserPanel(Timeline.user);
        }

        //Add tweets to window!
        populateTweets();
        MainGUI.setDoneWaiting();
    }


    /*
     * Asks twitter for appropriate timeline: Home/Mentions/User/Search
     */
    private static List<Status> getTimeline() {
        List<Status> result = null;
        switch (Timeline.type) {
            case HOME:
                try {
                    result = Twitty401.twitter.getHomeTimeline(Timeline.paging);
                } catch (TwitterException ex) {
                    Logger.getLogger(Timeline.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case MENTIONS:
                try {
                    result = Twitty401.twitter.getMentionsTimeline(Timeline.paging);
                } catch (TwitterException ex) {
                    Logger.getLogger(Timeline.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case USER:
                try {
                    result = Twitty401.twitter.getUserTimeline(user.getScreenName(), Timeline.paging);
                } catch (TwitterException ex) {
                    //USER IS PRIVATE
                }
                break;
            case SEARCH:
                Query searchQuery;
                try {
                    if (Timeline.paging.getPage() > 1) { //if need to load next page
                        if (queryResult.hasNext()) {
                            searchQuery = queryResult.nextQuery();
                        } else {
                            searchQuery = null;
                        }
                    } else { //if first page
                        searchQuery = Timeline.query;
                    }
                    queryResult = Twitty401.twitter.search(searchQuery);
                    result = queryResult.getTweets();
                } catch (TwitterException ex) {
                } catch (NullPointerException npe) {
                    LoadMore.showNoMore();
                }
                break;
            case FAVORITES:
                try {
                    result = Twitty401.twitter.getFavorites(user.getScreenName(), Timeline.paging);
                } catch (TwitterException ex) {
                    Logger.getLogger(Timeline.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
        }
        return result;
    }

    /*
     * adds tweets to panel!  
     */
    private static void populateTweets() {
        //Determines timeline
        List<Status> timeline = getTimeline();

        //Create tweetObjects and add them to the mainPanel!
        for (Status status : timeline) {
            MainGUI.addToMainPanel(new TweetObject(status, Timeline.type));
        }
        //add load more
        LoadMore.addLoadMore(LoadMore.PanelType.TIMELINE);

        //update mainPanel
        MainGUI.repaintMainPanel(); //revalidates and repaints
    }

    /*
     * increments page and populates tweets
     */
    public static void loadMore() {
        Timeline.paging.setPage(Timeline.paging.getPage() + 1);
        populateTweets();
    }
}