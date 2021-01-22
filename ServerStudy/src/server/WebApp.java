package server;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WebApp {
    private static WebContext webContext = null;
    static {
        try {
            //1.获取解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //2.从解析工厂获取解析器
            SAXParser parser = factory.newSAXParser();
            //3.编写处理器
            //3.加载文档Document注册处理器
            WebHandle handle = new WebHandle();
            parser.parse(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream("./web.xml")
                    , handle);

            webContext = new WebContext(handle.getEntityList(), handle.getMappingList());
        }catch (Exception e) {
            System.out.println("解析错误");
        }
    }

    /**
     * 通过url获取配置文件的servlet
     * @param url
     * @return
     */
    public static Servlet getServletFromUrl(String url) {
        String className = webContext.getClz("/"+url);
        Class aClass = null;
        try {
            aClass = Class.forName(className);
            Servlet servlet = (Servlet) aClass.getConstructor().newInstance();
            return servlet;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }
}
