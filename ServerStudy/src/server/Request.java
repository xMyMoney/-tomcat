package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

/**
 * 封装请求协议 获取method uri 请求参数
 */
public class Request {

    private String requestInfo;
    private String method;
    private String url;
    private String queryStr;

    private Map<String, List<String>> parameterMap;

    private final String CRLF = "\r\n";

    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }

    public Request(InputStream inputStream) {
        if (null != inputStream) {
            parameterMap = new HashMap<>();
            byte[] bytes = new byte[1024 * 1024];
            int len = 0;
            try {
                len = inputStream.read(bytes);
                requestInfo = new String(bytes, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            //分解字符串
            parseRequestInfo();
        }

    }

    private void parseRequestInfo() {
        //1.获取请求方式 开头到第一个斜杠/
        this.method = this.requestInfo.substring(0,this.requestInfo.indexOf("/")).toLowerCase(Locale.ROOT);
        this.method = this.method.trim();
        //2.获取url 第一个/到HTTP/
        int startIdx = this.requestInfo.indexOf("/") + 1;
        int endIdx = this.requestInfo.indexOf("HTTP/");
        this.url = this.requestInfo.substring(startIdx,endIdx).trim();
        //获取?的位置
        int queryIdx = this.url.indexOf("?");
        if (queryIdx >= 0) {
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0];
            queryStr = urlArray[1];
        }
        if (method.equals("post")) {
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            if (null == queryStr) {
                queryStr = qStr;
            }else {
                queryStr += '&'+qStr;
            }
        }
        queryStr = null == queryStr ? "":queryStr;
        //转成map fav=1&fav=2&uname=a&age=18&others=
        convertMap();
    }

    private void convertMap() {
        String[] keyValues = this.queryStr.split("&");
        for (String queryStr : keyValues) {
            //再次分割 uname=a
            String[] kv = queryStr.split("=");
            String key = kv[0];
            String value = null;
            if (kv.length == 2) {
                value = decode(kv[1],"UTF-8");
            }
            //String value = kv[1] == null?null:decode(kv[1],"UTF-8");
            //存储到map
            if(!parameterMap.containsKey(key)) {
                parameterMap.put(key,new ArrayList<>());
            }
            parameterMap.get(key).add(value);
        }
    }

    /**
     * 处理get请求中的中文
     * @param value
     * @param enc
     * @return
     */
    private String  decode(String value,String enc) {
        try {
            return URLDecoder.decode(value,enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过key获取对应多个值
     * @param key
     * @return
     */
    public String[] getParameterValues(String key) {
        List<String> values = this.parameterMap.get(key);
        if (null == values || values.size() < 1) {
            return null;
        }
        return values.toArray(new String[0]);
    }

    /**
     * 通过key获取对应一个值
     * @param key
     * @return
     */
    public String getParameter(String key) {
        String[] values = getParameterValues(key);
        return values == null ? null : values[0];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryStr() {
        return queryStr;
    }
}
