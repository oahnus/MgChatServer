package top.oahnus.Servlet;

import top.oahnus.Dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by oahnus on 2016/7/23.
 */
public class MailVertifyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        Writer writer = resp.getWriter();
        String email = req.getParameter("email");

        UserDao userDao = new UserDao();
        String retVal = userDao.vertifyMail(email);

        if((retVal==null||retVal.equals(""))){
            writer.write("result({\"msg\":\" \"})");
        }else{
            writer.write("result({\"msg\":\"此邮箱已被注册\"})");
        }
    }
}
