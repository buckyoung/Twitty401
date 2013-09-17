/*
 * This is Ugly! #cleanItUp!
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import twitter4j.TwitterException;

public class MainGUI {

    public enum Tab {

        TWEET, HOME, MENTIONS, TRENDS, SEARCH, NONE
    };
    public Tab selectedTab;
    private static JPanel buttonPanel, mainPanel;
    private static JButton tweetButton, homeButton, mentionsButton, trendsButton, searchButton;
    private static JFrame theWindow;
    private static JScrollPane scrollPanel;
    private final ImageIcon unSelectedTweetIcon = new ImageIcon("res/00tweet.png");
    private final ImageIcon unSelectedHomeIcon = new ImageIcon("res/10home.png");
    private final ImageIcon unSelectedMentionsIcon = new ImageIcon("res/20mentions.png");
    private final ImageIcon unSelectedTrendsIcon = new ImageIcon("res/30trends.png");
    private final ImageIcon unSelectedSearchIcon = new ImageIcon("res/40search.png");
    private final ImageIcon SelectedTweetIcon = new ImageIcon("res/01tweet.png");
    private final ImageIcon SelectedHomeIcon = new ImageIcon("res/11home.png");
    private final ImageIcon SelectedMentionsIcon = new ImageIcon("res/21mentions.png");
    private final ImageIcon SelectedTrendsIcon = new ImageIcon("res/31trends.png");
    private final ImageIcon SelectedSearchIcon = new ImageIcon("res/41search.png");
    //Listens for mouse click on scroll panel to make DestroyPopUp call
    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent me) {
        }

        @Override
        public void mousePressed(MouseEvent me) {
            PopUp.destroyPopup();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }
    };

    public MainGUI(Point windowLocation) {
        initializePanels(); //sets buttonPanel, mainPanel, scrollPanel, buttonIcons, buttonHandler
        createWindow(windowLocation); //Create Window w/ Authenticating-User's Name
        Timeline.createTimeline(Timeline.Type.HOME, null, null); //Automatically Pulls Up the HomeTimeLine
        selectedTab = Tab.HOME;
        //init popup and deref immediately (fake singleton)
        PopUp popup = new PopUp();
        popup = null;
        //
        determineTabColors();
        theWindow.setVisible(true); //Gives us something to look at
    }

    /*
     * Initializes buttonPanel, mainPanel, scrollPanel; Sets buttonIcons and buttonHandler
     */
    private void initializePanels() {
        //Button Panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 5));
        buttonPanel.setPreferredSize(new Dimension(350, 70));
        buttonPanel.setBackground(Color.blue);

        //Main panel lives inside scroll panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(160, 200, 240));

        //Scroll panel with Mouse Listener to destroyPopUp()
        scrollPanel = new JScrollPane(mainPanel);
        scrollPanel.setPreferredSize(new Dimension(350, 400));
        scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.addMouseListener(mouseListener);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        //SET Button Icons 

        tweetButton = new JButton(unSelectedTweetIcon);
        buttonPanel.add(tweetButton);

        homeButton = new JButton(unSelectedHomeIcon);
        buttonPanel.add(homeButton);

        mentionsButton = new JButton(unSelectedMentionsIcon);
        buttonPanel.add(mentionsButton);

        trendsButton = new JButton(unSelectedTrendsIcon);
        buttonPanel.add(trendsButton);

        searchButton = new JButton(unSelectedSearchIcon);
        buttonPanel.add(searchButton);

        //setButtonHandler
        ButtonHandler bhandler = new ButtonHandler();
        tweetButton.addActionListener(bhandler);
        homeButton.addActionListener(bhandler);
        mentionsButton.addActionListener(bhandler);
        trendsButton.addActionListener(bhandler);
        searchButton.addActionListener(bhandler);

    }

    /*
     * Attempts to create a new window -- with a name and username
     * Sets Window properties
     * Creates a Container?
     * Sets Border Layout
     * Catches but doesnt do much with the exceptions -- What if the window is never made? what if cant get username? -- Perhaps make a window without the username!
     */
    private void createWindow(Point windowLocation) {

        try {
            theWindow = new JFrame("Twitty401 -- " + Twitty401.twitter.getScreenName());
            //set properties
            theWindow.setSize(350, 480);
            theWindow.setResizable(false);
            theWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            theWindow.setLocation(windowLocation);

            //create container..........................?
            Container c = theWindow.getContentPane();
            c.setLayout(new BorderLayout());
            c.add(buttonPanel, BorderLayout.NORTH);
            c.add(scrollPanel, BorderLayout.CENTER);
            c.add(StatusPanel.newStatusPanel(), BorderLayout.SOUTH);
        } catch (TwitterException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     * Fires events for the five buttons at the top of the screen
     */
    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PopUp.destroyPopup();//good measure
            //TWEET
            if (e.getSource() == tweetButton) {
                selectedTab = Tab.TWEET;
                TweetPanel.createTweetPanel();
            }
            //HOME
            if (e.getSource() == homeButton) {
                selectedTab = Tab.HOME;
                Timeline.createTimeline(Timeline.Type.HOME, null, null);
            }
            //MENTIONS
            if (e.getSource() == mentionsButton) {
                selectedTab = Tab.MENTIONS;
                Timeline.createTimeline(Timeline.Type.MENTIONS, null, null);
            }
            //TRENDS
            if (e.getSource() == trendsButton) {
                selectedTab = Tab.TRENDS;
                TrendPanel.createTrendPanel();
            }
            //SEARCH
            if (e.getSource() == searchButton) {
                selectedTab = Tab.SEARCH;
                SearchPanel.createSearchPanel();
            }
            determineTabColors();
        }
    }

    private void determineTabColors() {
        setAllUnselected();
        switch (selectedTab) {
            case TWEET:
                tweetButton.setIcon(SelectedTweetIcon);
                break;
            case HOME:
                homeButton.setIcon(SelectedHomeIcon);
                break;
            case MENTIONS:
                mentionsButton.setIcon(SelectedMentionsIcon);
                break;
            case TRENDS:
                trendsButton.setIcon(SelectedTrendsIcon);
                break;
            case SEARCH:
                searchButton.setIcon(SelectedSearchIcon);
                break;
            default:
            //none: all are unselected
            }

    }

    public void setAllUnselected() {

        tweetButton.setIcon(unSelectedTweetIcon);
        homeButton.setIcon(unSelectedHomeIcon);
        mentionsButton.setIcon(unSelectedMentionsIcon);
        trendsButton.setIcon(unSelectedTrendsIcon);
        searchButton.setIcon(unSelectedSearchIcon);
    }

    /*
     * Removes old components and gets MainPanel ready for new ones
     * Call BEFORE adding new elements to mainPanel
     */
    public static void resetMainPanel() {
        mainPanel.removeAll();
        mainPanel.setLayout(new GridLayout(0, 1)); //I just switched this from boxLayout, switch back if any problems arise!
    }

    /*
     * Revalidates and Repaints MainPanel
     * Call AFTER adding new elements to mainPanel
     */
    public static void repaintMainPanel() {

        mainPanel.revalidate(); //needed after any remove
        mainPanel.repaint(); //needed to get junk off the screen
    }

    /*
     * Adds component to main panel
     */
    public static synchronized void addToMainPanel(Component component) {
        mainPanel.add(component);
    }

    /*
     * Sets the cursor to spinner
     */
    public static void setWaiting() {
        theWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    public static void setDoneWaiting() {
        theWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /*
     * Reset scroll panel to top
     */
    static void setScrollToTop() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scrollPanel.getVerticalScrollBar().setValue(0);
            }
        });
    }
}
