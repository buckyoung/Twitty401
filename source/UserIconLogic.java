
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import twitter4j.User;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author BuckYoung
 */
public class UserIconLogic {

    public enum Size {

        MINI, NORMAL, BIG
    };

    /*
     * Big: 73x73
     * Normal: 48x48
     * Mini: 24x24
     * 
     * Input: User and Picture Size Requested
     * Output: User Picture
     */
    public static ImageIcon getIcon(User user, Size size) {
        URL url = null;
        ImageIcon result = null;
        try {
            if (size == Size.MINI) {
                url = new URL(user.getMiniProfileImageURL());
            } else if (size == Size.NORMAL) {
                url = new URL(user.getProfileImageURL());
            } else if (size == Size.BIG) {
                url = new URL(user.getBiggerProfileImageURL());
            }
            if (url != null) {
                result = new ImageIcon(url);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
