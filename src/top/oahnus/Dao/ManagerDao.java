package top.oahnus.Dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import top.oahnus.Bean.User;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oahnus on 2016/7/24.
 */
public class ManagerDao {
    public long getLastUserID() {
        SqlSession sqlSession = null;
        long sum = 0;

        try {
            Reader reader = Resources.getResourceAsReader("top/oahnus/Config/Configuration.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = sqlSessionFactory.openSession();

            IManager iManager = sqlSession.getMapper(IManager.class);
            sum = iManager.getLastUserID();
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sum;
    }

    public List<User> findUser(User param) {
        SqlSession sqlSession = null;

        List<User> retUsers = new ArrayList<>();
        try {
            Reader reader = Resources.getResourceAsReader("top/oahnus/Config/Configuration.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = sqlSessionFactory.openSession();

            IManager iManager = sqlSession.getMapper(IManager.class);
            retUsers = iManager.findUser(param);
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retUsers;
    }

    public void addFriend(Map<String, String> map) {
        SqlSession sqlSession = null;

        try {
            Reader reader = Resources.getResourceAsReader("top/oahnus/Config/Configuration.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = sqlSessionFactory.openSession();

            IManager iManager = sqlSession.getMapper(IManager.class);
            iManager.addFriend(map);
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        User user = new User();
        user.setAddress("江苏镇江");
        List<User> list = new ManagerDao().findUser(user);

        System.out.println(list.size());
    }
}
