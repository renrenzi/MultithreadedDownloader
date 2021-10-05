package com.jj.util;

import java.io.File;

/**
 *  文件工具类
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class FileUtils {

    /**
     * 获取本地文件大小
     * @param path
     * @return
     */
    public static long getFileContentLength(String path){
        File file = new File(path);
        return file.exists() && file.isFile() ? file.length() : 0;
    }
}
