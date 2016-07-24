package top.oahnus.Dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by oahnus on 2016/7/24.
 */
public class ManagerDao {
    public long getLastUserID(){
        SqlSession sqlSession = null;
        long sum = 0;

        try {
            Reader reader = Resources.getResourceAsReader("top/oahnus/Config/Configuration.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            sqlSession = sqlSessionFactory.openSession();

            IManager iManager = sqlSession.getMapper(IManager.class);
            sum = iManager.getLastUserID();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sum;
    }
}
