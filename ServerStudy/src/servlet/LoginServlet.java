package servlet;

import server.Request;
import server.Response;
import server.Servlet;

import java.io.IOException;

public class LoginServlet implements Servlet {

    @Override
    public void service(Request request, Response response) {
        String uname = request.getParameter("uname");
        response.print("<meta charset=\"UTF-8\">");
        response.print("你好"+uname);
        response.pushToBrowser(200);
    }
}
