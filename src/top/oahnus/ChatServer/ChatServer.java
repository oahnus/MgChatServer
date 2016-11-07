package top.oahnus.ChatServer;

import top.oahnus.Bean.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oahnus on 2016/7/13.
 */
public class ChatServer implements Runnable{

    private ServerSocket serverSocket = null;
    private Socket socket = null;

    private boolean isRunning = false;

    private Map<String,Client> clients = new HashMap<>();

    private BufferedWriter bw;

    private Map<String,ObjectOutputStream> onlineClientMap = new HashMap<>();

    public ChatServer(){

    }

    public void run(){
        try {
            serverSocket = new ServerSocket(7888);
            isRunning = true;
System.out.println("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
System.out.println("服务器启动失败");
        }
        while(isRunning){
            try {
                socket = serverSocket.accept();
//                Client client = new Client(1,socket,onlineClientMap,clients);
                Client client = new Client(socket);
                Thread thread = new Thread(client);
                thread.start();
System.out.println("创建Client成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setOnlineClient(Map<String,ObjectOutputStream> onlineClientMap) {
        this.onlineClientMap = onlineClientMap;
    }

    class Client implements Runnable{
        private Socket cSocket = null;
        private ObjectInputStream ois = null;
        private ObjectOutputStream oos = null;
        private boolean isConnected = false;

        private String clientID;

        public Client(Socket socket) {
            isConnected = true;
            try {
                this.cSocket = socket;
                ois = new ObjectInputStream(cSocket.getInputStream());
                oos = new ObjectOutputStream(cSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (isConnected) {
System.out.println("准备接受消息");
                    Message msg = (Message) ois.readObject();
System.out.println("接受成功");
                    if(msg.getCode().equals("CLOSE")){
System.out.println("接收到关闭信息");
                        closeClient();
System.out.println("客户端数量"+clients.size());
                        break;
                    }else if(msg.getCode().equals("CHATIN")){
                        clientID = msg.getContent();
                        clients.put(msg.getContent(),this);
                    }else if(msg.getCode().equals("MSG")){
                        String target = msg.getTargetID();
System.out.println("获取目标id，发送消息");
                        if(clients.containsKey(target)){
                            clients.get(target).sendMsg(msg);
System.out.println("发送成功");
                        } else if(onlineClientMap.containsKey(msg.getTargetID())){
System.out.println(msg.getTargetID()+"在线，未打开聊天室");
                            // 之前map中保存的键值对为String，socket，通过获取socket后封装成ObjectOutputStream
                            // 运行后客户端出错
                            // java.io.StreamCorruptedException: invalid type code: AC
                            // 原因是对socket.getOutputStream进行了两次封装，此处为一处，MonitorServer中一处
                            ObjectOutputStream oos = onlineClientMap.get(msg.getTargetID());

                            oos.writeObject(msg);
                            oos.flush();
                        } else{
System.out.println("未在线");
                            File file = new File("OfflineRecord/"+msg.getTargetID()+".txt");
                            if(!file.exists()){
                                file.createNewFile();
                                BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
                                bw.write(msg.getSourceID()+"#"+msg.getContent());
                                bw.newLine();
                                bw.flush();
                                bw.close();
System.out.println("创建新文件");
                            }else{
                                BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
                                bw.write(msg.getSourceID()+"#"+msg.getContent());
                                bw.newLine();
                                bw.flush();
                                bw.close();
System.out.println("写入记录成功");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
System.out.println("发送过程中出错");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void closeClient(){
            System.out.println(clientID+"关闭");
            try {
                if(oos!=null){
                    oos.close();
                }
                if(ois!=null){
                    ois.close();
                }
                if(socket!=null){
                    socket.close();
                }
                clients.remove(clientID);
            } catch (IOException e) {
            }
        }

        public void sendMsg(Message msg){
            try {
                oos.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
System.out.println("消息发送失败");
            }
        }

        public String getClientID() {
            return clientID;
        }

        public void setClientID(String clientID) {
            this.clientID = clientID;
        }
    }
}