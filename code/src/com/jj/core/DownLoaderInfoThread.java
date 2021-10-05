package com.jj.core;

import com.jj.constant.Constant;

/**
 * 展示下载信息
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class DownLoaderInfoThread implements Runnable {

    /**
     * 下载文件总大小
     */
    private long httpFileContentLength;

    /**
     * 本地已下载大小
     */
    public double finishedSize;

    /**
     *  本次累积下载大小
     */
    public double downSize;

    /**
     *  前一次累积下载大小
     */
    public double preSize;

    public DownLoaderInfoThread(long httpFileContentLength) {
        this.httpFileContentLength = httpFileContentLength;
    }

    @Override
    public void run() {
        //计算文件总大小 单位：mb
        String fileSize = String.format("%.2f",httpFileContentLength / Constant.MB);

        //计算每秒下载速度 kb
        int downLoadSpeed = (int) ((downSize - preSize) / 1024d);
        preSize = downSize;

        //剩余文件的大小
        double remainSize = httpFileContentLength - finishedSize - downSize;

        //计算剩余时间
        String remainTime = String.format("%.1f", remainSize / 1024d / downLoadSpeed);
        if ("identity".equalsIgnoreCase(remainTime)){
            remainTime = "---";
        }
        //已下载大小
        String correntSize = String.format("%.2f", (downSize - finishedSize) / Constant.MB);

        String downInfo = String.format("已下载 %smb/%smb, 下载速度 %skb/s , 剩余时间 %ss",
                                        correntSize, fileSize, downLoadSpeed, remainTime);
        System.out.print("\r");

        System.out.print(downInfo);
    }
}
