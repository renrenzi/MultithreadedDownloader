package com.jj;

import com.jj.util.HttpUtil;

import java.util.Scanner;

/**
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class Main {
    public static void main(String[] args) {
        String url = null;
        if (args.length == 0 || args == null){
            while (true){
                System.out.println("请输入下载连接");
                Scanner scanner = new Scanner(System.in);
                url = scanner.next();
                if (url != null) {
                    break;
                }
            }
        }else {
            url = args[0];
        }

        System.out.println(HttpUtil.getFileName(url));
    }
}
