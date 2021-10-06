package com.jj.core;

import com.jj.constant.Constant;
import com.jj.util.HttpUtils;
import com.jj.util.LogUtils;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 分片下载任务
 *
 * @author 张俊杰
 * @date 2021/10/6  - {TIME}
 */
public class DownLoaderTask implements Callable<Boolean> {

    private String url;

    private long startPos;

    private long endPos;

    private int part;

    private CountDownLatch countDownLatch;

    public DownLoaderTask(String url, long startPos, long endPos, int part,CountDownLatch countDownLatch) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.part = part;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public Boolean call() throws Exception {
        // 获取文件名
        String fileName = HttpUtils.getFileName(url);

        // 分块文件名
        fileName = fileName + Constant.TEMP_NAME + part;

        // 下载路径
        fileName = Constant.FILE_PATH + fileName;

        // 获取分块下载连接
        HttpURLConnection httpURLConnection = HttpUtils.getHttpURLConnection(url, startPos, endPos);

        try (
                InputStream inputStream = httpURLConnection.getInputStream();
                
                BufferedInputStream bis = new BufferedInputStream(inputStream);
               
                RandomAccessFile ras = new RandomAccessFile(fileName, "rw");
        ) {
            int len = -1;
            byte[] buffer = new byte[Constant.BYTE_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                    DownLoaderInfoThread.downSize.add(len);
                    ras.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            LogUtils.error("文件没有找到 ()", fileName);
            return false;
        } catch (Exception e) {
            LogUtils.error("下载失败");
            return false;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            countDownLatch.countDown();
        }
        return true;
    }
}
