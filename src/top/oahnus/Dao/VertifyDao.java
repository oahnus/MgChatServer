package top.oahnus.Dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import top.oahnus.Bean.User;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by oahnus on 2016/7/4.
 */
public class VertifyDao {
    public User vertify(User user){
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

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }

        return retUser;
    }
}
