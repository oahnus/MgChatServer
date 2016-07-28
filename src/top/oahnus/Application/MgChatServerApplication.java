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

        //登陆验证 8887
        AuthVertify authVertify = new AuthVertify();
        Thread vertifyThread = new Thread(authVertify);
        vertifyThread.start();

        //离线消息查询 8885
        MonitorServer monitorServer = new MonitorServer();
        Thread monitorThread = new Thread(monitorServer);
        monitorThread.start();

        //聊天服务器 8888
        ChatServer chatServer = new ChatServer();
        chatServer.setOnlineClient(monitorServer.getOnlineClient());
        Thread chatServerThread = new Thread(chatServer);
        chatServerThread.start();
    }
}
