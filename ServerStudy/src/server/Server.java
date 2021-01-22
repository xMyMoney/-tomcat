package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private boolean isRunning;
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(8888);
            isRunning = true;
            receive();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    public void receive() {
       while (isRunning) {
           try {
               Socket client = serverSocket.accept();
               System.out.println("一个客户端建立连接");
               //多线程处理
               new Thread(new Dispatcher(client)).start();
           } catch (IOException e) {
               e.printStackTrace();
               this.stop();
           }
       }
    }

    public void stop() {
        isRunning = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
