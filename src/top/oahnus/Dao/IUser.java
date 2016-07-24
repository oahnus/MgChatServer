package top.oahnus.Dao;

import top.oahnus.Bean.User;

import java.util.List;

/**
 * Created by oahnus on 2016/7/4.
 */
public interface IUser {
    User vertifyUser(User user);
    List<User> getFriendsList(String id);
    List<String> getFriendsID(String id);
    String vertifyMail(String mail);
    void addUser(User user);
}
