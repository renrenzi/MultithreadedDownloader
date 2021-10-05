package com.jj.core;

import com.jj.constant.Constant;
import com.jj.util.FileUtils;
import com.jj.util.HttpUtils;
import com.jj.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 实现下载
 *
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class DownLoader {

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public void downLoadFile(String url) {

        // 获取文件名
        String fileName = HttpUtils.getFileName(url);

        // 文件下载路径
        String path = Constant.FILE_PATH + fileName;

        // 获取本地文件大小
        long fileContentLength = FileUtils.getFileContentLength(path);

        HttpURLConnection httpURLConnection = null;

        DownLoaderInfoThread downLoaderInfoThread = null;
        try {
            // 获取连接对象
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
            // 获取下载文件大小
            int contentLength = httpURLConnection.getContentLength();

            if (fileContentLength >= contentLength) {
                LogUtils.info("()文件已经下载,无需重新下载", fileName);
                return;
            }

            downLoaderInfoThread = new DownLoaderInfoThread(contentLength);

            // 每秒提交一次任务
            scheduledExecutorService.scheduleAtFixedRate(downLoaderInfoThread, 1, 1, TimeUnit.SECONDS);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                FileOutputStream fos = new FileOutputStream(path);
                BufferedOutputStream bos = new BufferedOutputStream(fos);) {
            int len = -1;
            byte[] buffer = new byte[Constant.BYTE_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                downLoaderInfoThread.downSize += len;
                bos.write(buffer,0,len);
            }
        } catch (FileNotFoundException e) {
            LogUtils.error("文件没有找到 ()", url);
        } catch (Exception e) {
            LogUtils.error("下载失败");
        } finally {
            System.out.print("\n");
            System.out.print("下载完成");
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            // 关闭线程池
            scheduledExecutorService.shutdownNow();
        }

    }
}
