package top.oahnus.Application;

import top.oahnus.ChatServer.ChatServer;
import top.oahnus.ChatServer.MonitorServer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oahnus on 2016/7/13.
 */
//服务器主程序
public class MgChatServerApplication {
    public static void main(String[] args){
        Map<String, String> ipMap = new HashMap<>();

        AuthVertify authVertify = new AuthVertify();
        Thread vertifyThread = new Thread(authVertify);
        vertifyThread.start();

//        MonitorServer monitorServer = new MonitorServer(ipMap);
//        Thread monitorThread = new Thread(monitorServer);
//        monitorThread.start();

        ChatServer chatServer = new ChatServer();
        Thread chatServerThread = new Thread(chatServer);
        chatServerThread.start();
    }
}
