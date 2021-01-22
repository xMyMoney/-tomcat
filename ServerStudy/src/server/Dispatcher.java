package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatcher implements Runnable{
    private Socket client;
    private Request request;
    private Response response;

    public Dispatcher(Socket client) {
        this.client = client;
        try {
            //获取请求和响应
            request = new Request(client);
            response = new Response(client);
        } catch (IOException e) {
            e.printStackTrace();
            this.release();
        }
    }

    @Override
    public void run() {
        try {
            if (null == request.getUrl() || request.getUrl().equals("")) {
                //InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
                //byte[] bytes = new byte[is.available()];
                //is.read(bytes);
               // response.print( new String(bytes));

                /*byte[] bytes = new byte[1024 * 1024];
                int len = 0;
                try {
                    len = is.read(bytes);
                    response.print(new String(bytes, 0, len));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }*/
                response.print("<h1>main</h1>");

                response.pushToBrowser(200);
                //is.close();
                return;
            }
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            if (null != servlet) {
                servlet.service(request, response);
                response.pushToBrowser(200);
            } else {
                //没有找到servlet
                /*InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html");
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                response.print(new String(bytes));*/
                response.print("<h1>404</h1>");
                response.pushToBrowser(404);
                //is.close();
            }
        }catch (Exception e) {
            response.pushToBrowser(505);
        }
        this.release();
    }

    /**
     * 释放资源
     */
    private void release() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
