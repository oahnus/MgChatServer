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

    public ChatServer(){

    }

    public void run(){
        try {
            serverSocket = new ServerSocket(8888);
            isRunning = true;
System.out.println("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
        while(isRunning){
            try {
                socket = serverSocket.accept();
                Client client = new Client(socket);
                Thread thread = new Thread(client);
                thread.start();
System.out.println("创建Client成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class Client implements Runnable{
        private Socket socket = null;
        private ObjectInputStream ois = null;
        private ObjectOutputStream oos = null;
        private boolean isConnected = false;

        private String clientID;

        public Client(Socket socket) {
            isConnected = true;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                this.socket = socket;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Client c = null;
            try {
                while (isConnected) {
System.out.println("开始接收信息");
                    Message msg = (Message) ois.readObject();
System.out.println("消息接受成功");
                    if(msg.getCode().equals("CLOSE")){
                        close();
                        isConnected = false;
                        break;
                    }else if(msg.getCode().equals("LOGIN")){
                        clients.put(msg.getContent(),this);
                    }else if(msg.getCode().equals("MSG")){
                        String target = msg.getTargetID();
System.out.println("将消息发送到客户端");
                        if(clients.containsKey(target)){
                            clients.get(target).sendMsg(msg);
System.out.println("发送成功");
                        }else{
                            System.out.println("未在线");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
System.out.println("消息接收失败");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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

        public void close(){
            try {
                if(ois!=null){
                    ois.close();
                }
                if(oos!=null){
                    oos.close();
                }
                if(socket!=null){
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
