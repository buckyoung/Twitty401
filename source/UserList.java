
import twitter4j.ResponseList;
import twitter4j.User;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author BuckYoung
 */
public class UserList {

    /*
     * adds found users to main panel
     */
    public static void createUserList(ResponseList<User> userList){
        for (User user : userList) {
            FoundUser fUser = new FoundUser(user);
            MainGUI.addToMainPanel(fUser);
        }
    }
}
