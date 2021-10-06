package com.jj;

import com.jj.core.DownLoader;
import com.jj.util.LogUtils;

import java.util.Scanner;

/**
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class Main {
    public static void main(String[] args) {
        String url = null;
        if (args.length == 0 || args == null){
            while (true ){
                LogUtils.info("请输入下载连接");
                Scanner scanner = new Scanner(System.in);
                url = scanner.next();
                if (url != null) {
                    break;
                }
            }
        }else {
            url = args[0];
        }
      new DownLoader().downLoadFile(url);
    }
}
