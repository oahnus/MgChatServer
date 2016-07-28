package top.oahnus.Dao;

import top.oahnus.Bean.User;

import java.util.List;
import java.util.Map;

/**
 * Created by oahnus on 2016/7/24.
 */
public interface IManager {
    long getLastUserID();
    List<User> findUser(User param);
    void addFriend(Map<String,String> map);
}
