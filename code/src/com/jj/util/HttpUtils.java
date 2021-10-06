package com.jj.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  http 工具类
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class HttpUtils {

    /**
     * 获取下载文件大小
     * @param url
     * @return
     * @throws IOException
     */
    public static long getHttpFileContentLength(String url) throws IOException {
       long contentLength;
       HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = getHttpURLConnection(url);
            contentLength = httpURLConnection.getContentLength();
        } finally {
            httpURLConnection.disconnect();
        }
        return contentLength;
    }

    /**
     * 分块下载
     * @param url
     * @param startPos
     * @param endPos
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String url,long startPos,long endPos) throws IOException{

        HttpURLConnection httpURLConnection = getHttpURLConnection(url);

        LogUtils.info("下载的区间是 () ---> ()",startPos,endPos);

        if (endPos != 0){
            httpURLConnection.setRequestProperty("RANGE","bytes=" +startPos +"-" +endPos);
        }else {
            httpURLConnection.setRequestProperty("RANGE","bytes=" +startPos +"-");
        }
        return httpURLConnection;
    }

    /**
     * 获取 HttpURLConnection 对象
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        return httpURLConnection;
    }

    /**
     * 获取文件名
     * @param url
     * @return
     */
    public static String getFileName(String url){
        int index = url.lastIndexOf("/");
        return url.substring(index+1);
    }
}
