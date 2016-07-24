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
                    System.out.println("准备接受消息");
                    Message msg = (Message) ois.readObject();
                    System.out.println("接受成功");
                    if(msg.getCode().equals("CLOSE")){
//                        close();
//                        isConnected = false;
//                        break;
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
                        }else{
                            System.out.println("未在线");
                            File file = new File("OfflineRecord/"+msg.getTargetID()+".txt");
                            if(!file.exists()){
                                file.createNewFile();
                                System.out.println("创建新文件");
                            }else{
//                            FileWriter fw = new FileWriter(file,false);
//                            fw.write(msg.getSourceID()+"#"+msg.getContent()+"\n");
                                BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
//                            bw.append(msg.getSourceID()+"#"+msg.getContent()+"\n");
                                bw.write(msg.getSourceID()+"#"+msg.getContent());
                                bw.newLine();
                                bw.flush();
                                System.out.println("写入记录成功");
                            }
                        }
                    }else if(msg.getCode().equals("LOGIN")){
                        //如果有离线消息,将消息发送给客户端
                        System.out.println("登陆信息接受到");
                        File record = new File("OfflineRecord/"+clientID+".txt");
                        if(record.exists()){
                            BufferedReader br = new BufferedReader(new FileReader(record));
                            String line = br.readLine();
                            while(line!=null){
                                Message message = new Message();
                                message.setCode("MSG");
                                message.setSourceID(line.split("#")[0]);
                                message.setContent(line.split("#")[1]);

                                oos.writeObject(message);
                                oos.flush();
                                line = br.readLine();
                            }

                            Message message = new Message();
                            message.setCode("END");

                            oos.writeObject(message);
                            oos.flush();

                            record.delete();
                        }else{
                            Message message = new Message();
                            message.setCode("END");

                            oos.writeObject(message);
                            oos.flush();
                        }
                        record = null;
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
                // TODO: handle exception
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
