package servlet;

import server.Request;
import server.Response;
import server.Servlet;

public class RegisterServlet implements Servlet {

    @Override
    public void service(Request request, Response response) {
        response.print("<html lang=\"en\">");
        response.print("<head>");
        response.print("<meta charset=\"UTF-8\">");
        response.print("<title>");
        response.print("服务器响应成功");
        response.print("</title>");
        response.print("</head>");
        response.print("<body>");
        response.print("server回来了注册");
        response.print("</body>");
        response.print("</html>");
    }
}
