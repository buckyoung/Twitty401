
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.CursorSupport;
import twitter4j.PagableResponseList;
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
public class FollowPanel extends javax.swing.JPanel {

    private enum Type {

        FOLLOWING, FOLLOWERS
    };
    /**
     * Creates new form FollowPanel
     */
    private static FollowPanel singlePanel = new FollowPanel();
    private static User user;
    private static Type panelType;
    private static long page;
    private static PagableResponseList<User> userList = null;

    private FollowPanel() {
        initComponents();
    }

    public static void addFollowPanel(User user) {
        FollowPanel.user = user;
        MainGUI.addToMainPanel(singlePanel);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        followingButton = new javax.swing.JButton();
        favoritesButton = new javax.swing.JButton();
        tweetAtButton = new javax.swing.JButton();
        followersButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(160, 200, 240));
        setPreferredSize(new java.awt.Dimension(330, 48));

        jPanel1.setBackground(new java.awt.Color(200, 240, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setSize(new java.awt.Dimension(330, 100));

        followingButton.setText("Following");
        followingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                followingButtonActionPerformed(evt);
            }
        });

        favoritesButton.setText("Favorites");
        favoritesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                favoritesButtonActionPerformed(evt);
            }
        });

        tweetAtButton.setText("Tweet @");
        tweetAtButton.setToolTipText("");
        tweetAtButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tweetAtButtonActionPerformed(evt);
            }
        });

        followersButton.setText("Followers");
        followersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                followersButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(followingButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(followersButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(favoritesButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(tweetAtButton)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {favoritesButton, followersButton, followingButton, tweetAtButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(followingButton)
                    .add(followersButton))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(favoritesButton)
                    .add(tweetAtButton))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {favoritesButton, followersButton, followingButton, tweetAtButton}, org.jdesktop.layout.GroupLayout.VERTICAL);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void followingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_followingButtonActionPerformed

        FollowPanel.panelType = Type.FOLLOWING;
        createFirstFollowPage();
        
    }//GEN-LAST:event_followingButtonActionPerformed

    private void followersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_followersButtonActionPerformed

        FollowPanel.panelType = Type.FOLLOWERS;
        createFirstFollowPage();

    }//GEN-LAST:event_followersButtonActionPerformed

    private void favoritesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_favoritesButtonActionPerformed
        Timeline.createTimeline(Timeline.Type.FAVORITES, user, null);
        PopUp.destroyPopup();
    }//GEN-LAST:event_favoritesButtonActionPerformed

    private void tweetAtButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tweetAtButtonActionPerformed
        TweetPanel.createTweetPanel(user);
        PopUp.destroyPopup();
    }//GEN-LAST:event_tweetAtButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton favoritesButton;
    private javax.swing.JButton followersButton;
    private javax.swing.JButton followingButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton tweetAtButton;
    // End of variables declaration//GEN-END:variables

    private static void populateFollow() {

        //get a userlist
        if (FollowPanel.panelType == FollowPanel.Type.FOLLOWING) {
            try {
                userList = Twitty401.twitter.getFriendsList(user.getId(), page);
            } catch (TwitterException ex) {
                Logger.getLogger(FollowPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (FollowPanel.panelType == FollowPanel.Type.FOLLOWERS) {
            try {
                userList = Twitty401.twitter.getFollowersList(user.getId(), page);
            } catch (TwitterException ex) {
                Logger.getLogger(FollowPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //add founduser items to the list
        if (userList != null) {
            UserList.createUserList(userList);
            //repaint main
            MainGUI.repaintMainPanel();
            //add loadmore button!
            LoadMore.addLoadMore(LoadMore.PanelType.FOLLOW);
        }


    }
    
    //loads more!
    public static void loadMore() {
        page = userList.getNextCursor();
        populateFollow();
    }

    /*
     * creates first page
     */
    private void createFirstFollowPage() {
        PopUp.destroyPopup();
        MainGUI.setWaiting();
        MainGUI.resetMainPanel();
        UserPanel.addUserPanel(user);
        page = CursorSupport.START;
        populateFollow();
        MainGUI.setDoneWaiting();
    }
}