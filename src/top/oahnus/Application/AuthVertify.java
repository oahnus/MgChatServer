package top.oahnus.Application;

import top.oahnus.Bean.User;
import top.oahnus.Dao.UserDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by oahnus on 2016/7/4.
 */
public class AuthVertify implements Runnable{

    private ServerSocket serverSocket;
    private Socket socket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean started = false;

    public AuthVertify(){

    }

    public void init(){
        try {
            serverSocket = new ServerSocket(8887);

            started = true;
        } catch(BindException e){
            e.printStackTrace();
System.out.println("端口已被占用");
        } catch (IOException e) {
            e.printStackTrace();
System.out.println("socket error");
        }
    }

    public User vertify(User user){
        UserDao userDao = new UserDao();
        return userDao.getUserFromDB(user);
    }

    public void runApp(){
        while(started){
            try {
                socket = serverSocket.accept();

                in = new ObjectInputStream(socket.getInputStream());
                User user = (User) in.readObject();

System.out.println(user.getUserID());
System.out.println(user.getPassword());

                user = vertify(user);

                if(user!=null) {
                    out = new ObjectOutputStream(socket.getOutputStream());

System.out.println("输出");

                    out.writeObject(user);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(in!=null) {
                        in.close();
                    }
                    if(out!=null) {
                        out.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        init();
        runApp();
    }
    public static void main(String[] args){
        AuthVertify authVertify = new AuthVertify();

//测试
//        User user = new User();
//        user.setUserID("10000");
//        user.setPassword("b86d51d4ce12cdf48d69ed20063bfd40");
//
//        User ret = authVertify.vertify(user);
//
//        System.out.println(ret.getFigureImage());
//
//        System.out.println(ret.getUsername());
//        System.out.println(ret.getUserID());
//        System.out.println(ret.getPassword());
//        System.out.println(ret.getInfo());
//        System.out.println(ret.getBorn());
//        System.out.println(ret.getAddress());
//        System.out.println(ret.getSex());
//        System.out.println(ret.getFigure());
//
//        List<User> list = ret.getFriendsList();
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i).getUsername());
//        }
    }

}
