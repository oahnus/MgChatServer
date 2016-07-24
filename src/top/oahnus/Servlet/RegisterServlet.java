package top.oahnus.Servlet;

import top.oahnus.Bean.User;
import top.oahnus.Dao.ManagerDao;
import top.oahnus.Dao.UserDao;
import top.oahnus.Util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by oahnus on 2016/7/24.
 */
public class registerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        //获取注册表单中的参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email    = req.getParameter("email");
        String born     = req.getParameter("date");
        String sex      = req.getParameter("sex");
        String prov = req.getParameter("prov");

        ManagerDao managerDao = new ManagerDao();

        //将数据封装在User中
        User user = new User();
        String userID = String.valueOf(managerDao.getLastUserID()+1);
        user.setUserID(userID);
        user.setUsername(username);
        user.setPassword(MD5Util.encode2hex(password));
        user.setEmail(email);
        user.setBorn(new Date(born));
        user.setSex(sex);
        user.setAddress(prov);

        UserDao userDao = new UserDao();
        userDao.addUser(user);

        req.setAttribute("userID",userID);

        req.getRequestDispatcher("/WEB-INF/result/result.jsp").forward(req,resp);
    }
}
