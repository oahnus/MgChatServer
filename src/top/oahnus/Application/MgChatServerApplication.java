package top.oahnus.Application;

import top.oahnus.ChatServer.ChatServer;
import top.oahnus.ChatServer.MonitorServer;

/**
 * Created by oahnus on 2016/7/13.
 */
//服务器主程序
public class MgChatServerApplication {
    public static void main(String[] args){

        //登陆验证 7887
        AuthVerify authVerify = new AuthVerify();
        Thread verifyThread = new Thread(authVerify);
        verifyThread.start();

        //离线消息查询 7885
        MonitorServer monitorServer = new MonitorServer();
        Thread monitorThread = new Thread(monitorServer);
        monitorThread.start();

        //聊天服务器 7888
        ChatServer chatServer = new ChatServer();
        chatServer.setOnlineClient(monitorServer.getOnlineClient());
        Thread chatServerThread = new Thread(chatServer);
        chatServerThread.start();
    }
}
