package top.oahnus.ChatServer;

import top.oahnus.Bean.Message;
import top.oahnus.Bean.User;
import top.oahnus.Dao.UserDao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
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
 * Created by oahnus on 2016/7/13.
 */

/**
 * 处理登陆时客户端发来的查询离线信息请求
 */

public class MonitorServer implements Runnable{

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private boolean isRunning         = false;

    public MonitorServer(){
        try {
            serverSocket = new ServerSocket(8885);
        }
        catch (BindException e){
System.out.println("端口被占用");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runMonitor(){

    }

    public void run() {
System.out.println("开始监听8885");
        while(true) {
            try {
                socket = serverSocket.accept();

                Client client = new Client(socket);
                Thread thread = new Thread(client);
                thread.start();
System.out.println("创建CLIENT");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Client implements Runnable{
        private Socket socket             = null;
        private ObjectInputStream ois       = null;
        private ObjectOutputStream oos      = null;

        Client(Socket socket){
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                this.socket = socket;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
System.out.println("新链接介入");
            try{
                Message message = (Message) ois.readObject();
                File record = new File("OfflineRecord/"+"10001"+".txt");
System.out.println("查找离线记录");

                if(record.exists()){
                    BufferedReader br = new BufferedReader(new FileReader(record));
                    String line = br.readLine();
                    while(line!=null){
                        Message offMsg = new Message();
                        offMsg.setCode("MSG");
                        offMsg.setSourceID(line.split("#")[0]);
                        offMsg.setContent(line.split("#")[1]);

                        oos.writeObject(offMsg);
                        oos.flush();
                        line = br.readLine();
System.out.println("发送消息");
                    }

                    Message endMsg = new Message();
                    endMsg.setCode("END");

                    oos.writeObject(endMsg);
                    oos.flush();

                    record.delete();
System.out.println("发送结束");
                }else{
                    Message endMsg = new Message();
                    endMsg.setCode("END");

                    oos.writeObject(endMsg);
                    oos.flush();
                }
                record = null;
            }catch (IOException e) {
                // TODO: handle exception
            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
