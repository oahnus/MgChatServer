package top.oahnus.Dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import top.oahnus.Bean.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.List;

/**
 * Created by oahnus on 2016/7/4.
 */
public class UserDao {

    private String basePath = "";

    public UserDao(){
        basePath = System.getProperty("user.dir");
//        basePath = basePath.substring(1,basePath.l);
    }

    public User getUserFromDB(User user){
        SqlSession sqlSession = null;
        User retUser = null;
        try {
            //读取配置信息
            Reader reader = Resources.getResourceAsReader("top/oahnus/Config/Configuration.xml");
            //通过配置信息构建sqlsessionfactory
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = sqlSessionFactory.openSession();

            IUser iUser = sqlSession.getMapper(IUser.class);
            retUser = iUser.vertifyUser(user);
            retUser.setFriendsList(iUser.getFriendsList(user.getUserID()));

//System.out.println(basePath+"\\"+retUser.getFigure());
//            Image image = ImageIO.read(new File(basePath+"\\"+retUser.getFigure()));
//            image = image.getScaledInstance(40,40,Image.SCALE_DEFAULT);
//
//            retUser.setFigureImage(image);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(sqlSession!=null) {
                sqlSession.close();
            }
        }

        return retUser;
    }

    public List<String> getFriendsID(String userID){
        SqlSession sqlSession = null;
        List<String> idList   = null;

        try {
            Reader reader = Resources.getResourceAsReader("top/oahnus/Config/Configuration.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = sqlSessionFactory.openSession();

            IUser iUser = sqlSession.getMapper(IUser.class);
            idList = iUser.getFriendsID(userID);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return idList;
    }
}
