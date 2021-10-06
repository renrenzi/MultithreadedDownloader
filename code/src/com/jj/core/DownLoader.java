package com.jj.core;

import com.jj.constant.Constant;
import com.jj.util.FileUtils;
import com.jj.util.HttpUtils;
import com.jj.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * 实现下载
 *
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class DownLoader {

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM, Constant.THREAD_NUM,
            0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(Constant.THREAD_NUM), Executors.defaultThreadFactory());

    private CountDownLatch countDownLatch = new CountDownLatch(Constant.THREAD_NUM);

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

            ArrayList<Future> futureList = new ArrayList<>();

            // 分割文件
            split(url, futureList);

            countDownLatch.await();

            // 合并文件
            if (mergeFile(path)){
                // 清除临时文件
                cleanTemp(path);
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

            threadPoolExecutor.shutdown();
        }

    }

    /**
     * 文件切割
     *
     * @param url
     * @param futureList
     */
    public void split(String url, ArrayList<Future> futureList) {
        try {
            // 文件大小
            long contentLength = HttpUtils.getHttpFileContentLength(url);

            // 每片大小
            long size = contentLength / Constant.THREAD_NUM;

            for (int i = 0; i < Constant.THREAD_NUM; i++) {

                // 每片起始位置
                long startPos = i * size;

                // 每片结束位置
                long endPos;

                // 是最后一片
                if (i == Constant.THREAD_NUM - 1) {
                    endPos = 0;
                } else {
                    endPos = startPos + size;
                }
                // 不是第一片
                if (i != 0) {
                    startPos++;
                }

                DownLoaderTask downLoaderTask = new DownLoaderTask(url, startPos, endPos, i,countDownLatch);

                // 提交任务
                Future<Boolean> future = threadPoolExecutor.submit(downLoaderTask);

                futureList.add(future);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合成文件
     *
     * @param fileName
     * @return
     */
    public boolean mergeFile(String fileName) {

        LogUtils.info("开始合成");
        int len = -1;
        byte[] buffer = new byte[Constant.BYTE_SIZE];
        try (RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw");) {
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName + Constant.TEMP_NAME + i));){
                    while ((len = bis.read(buffer)) != -1){
                            accessFile.write(buffer,0,len);
                    }
                }
            }
            LogUtils.info("合成成功");
        } catch (Exception e) {
            LogUtils.error("合成文件失败 ()", fileName);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 清除临时文件
     * @param fileName
     */
    public void cleanTemp(String fileName){
        for (int i = 0; i < Constant.THREAD_NUM; i++) {
            File file = new File(fileName + Constant.TEMP_NAME + i);
            file.delete();
        }
    }
}
