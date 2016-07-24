package top.oahnus.ChatServer;

import top.oahnus.Bean.User;
import top.oahnus.Dao.UserDao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oahnus on 2016/7/13.
 */

/**
 * 留待后用
 */

/**
 * 创建一个客户端子类，没链接进一个新的客户端，创建一个新的线程，并监听客户端状态
 * 客户端登陆时，将客户端通过流发送的ip地址保存在map中
 * 之后将客户端id所对应的所有的好友的ip地址封装在一个map中，传回给客户端
 * 当客户端关闭时，发送一个关闭信息，当接受关闭信息时，将此id所对应的ip地址从map中remove
 */

public class MonitorServer implements Runnable{

    private ServerSocket serverSocket = null;
    private Map<String,String> ipMap  = new HashMap<>();
    private boolean isRunning         = false;

    public MonitorServer(Map<String,String> ipMap){
        this.ipMap = ipMap;
    }

    public void init(){
        try {
            serverSocket = new ServerSocket(8889);
        }
        catch (BindException e){
System.out.println("端口被占用");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = true;
    }

    public void runMonitor(){
        while(isRunning) {
            try {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    Client client = new Client(socket);
                    Thread thread = new Thread(client);
                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        init();
        runMonitor();
    }

    public static void main(String[] args){
        Map<String,String> ips = new HashMap<>();
        ips.put("10001","192.168.1.1");
        MonitorServer monitorServer = new MonitorServer(ips);
        monitorServer.init();
        monitorServer.runMonitor();
    }

    class Client implements Runnable{
        private Socket socket             = null;
        private boolean isRunning         = false;
        private DataInputStream dis       = null;
        private DataOutputStream dos      = null;

        Client(Socket socket){
            try {
                this.socket = socket;
                dis         = new DataInputStream(socket.getInputStream());
                dos         = new DataOutputStream(socket.getOutputStream());
                isRunning   = true;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String clientIP   = "";
            String clientID   = "";
            String clientInfo = "";
            String opCode     = "";
            Map<String,String> retIDs = new HashMap<>();

            try {
                clientInfo = dis.readUTF();
                String[] infos = clientInfo.split("#");

                if(infos[0].equals("PUTIP")) {
                    clientID = infos[1];
                    clientIP = infos[2];
                    if (!ipMap.containsKey(clientID)) {
                        ipMap.put(clientID, clientIP);
                    } else {

                    }
                }
                if(infos[1].equals("GETIP")){
                    clientID = infos[1];

                    if(ipMap.containsKey(clientID)){
                        String ip = ipMap.get(clientID);
                        dos.writeUTF(ip);
                        dos.flush();
                    }else{
                        dos.writeUTF("NULL");
                        dos.flush();
                    }
                }
//                //从数据库中读取对应ID的所有好友，再将所有好友的ip地址放入一个map中传回给客户端
//                User user = new User();
//                user.setUserID(clientID);
//
//                UserDao userDao = new UserDao();
//                List<String> ids = userDao.getFriendsID(clientID);
//
//                for(String id: ids){
//                    if(ipMap.containsKey(id)){
//                        retIDs.put(id,ipMap.get(id));
//                    }
//                }
//
//                oos.writeObject(retIDs);
//                oos.flush();
//
//                Thread.sleep(3000);
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                close();
            }
        }

        private void close(){
            try {
                if(dis!=null) {
                    dis.close();
                }
                if(dos!=null){
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
