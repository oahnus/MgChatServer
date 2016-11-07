package top.oahnus.Application;

import top.oahnus.Bean.User;
import top.oahnus.Dao.ManagerDao;
import top.oahnus.Dao.UserDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oahnus on 2016/7/4.
 */
public class AuthVerify implements Runnable{

    private ServerSocket serverSocket;
    private Socket socket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean started = false;

    public AuthVerify(){
        try {
            serverSocket = new ServerSocket(7887);

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

    @Override
    public void run() {
        while(started){
            try {
                socket = serverSocket.accept();

                Client client = new Client(socket);
                Thread thread = new Thread(client);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Client implements Runnable{

        private Socket cSocket              = null;
        private ObjectInputStream ois       = null;
        private ObjectOutputStream oos      = null;

        Client(Socket socket){
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                this.cSocket = socket;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            User user = null;
            Map<String,Object> map;
            try {
                map = (Map<String, Object>) ois.readObject();
                if(map.containsKey("verify")) {
                    user = (User) map.get("verify");
                    user = vertify(user);

                    oos.writeObject(user);
                    oos.flush();
                }
                else if(map.containsKey("findFriend")){
                    user = (User) map.get("findFriend");
                    ManagerDao managerDao = new ManagerDao();

                    List<User> list = managerDao.findUser(user);
                    oos.writeObject(list);
                    oos.flush();
                }
                else if(map.containsKey("addFriend")){
                    Map<String,String> param = new HashMap<>();
                    param.put("userid",map.get("userid").toString());
                    param.put("friendid",map.get("friendid").toString());

                    ManagerDao managerDao = new ManagerDao();
                    managerDao.addFriend(param);

                    param.put("userid",map.get("friendid").toString());
                    param.put("friendid",map.get("userid").toString());
                    managerDao.addFriend(param);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
