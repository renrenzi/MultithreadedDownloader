package com.jj.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *  日志工具类
 * @author 张俊杰
 * @date 2021/10/5  - {TIME}
 */
public class LogUtils {

    /**
     * 输出 INFO 级别信息
     * @param message
     * @param args
     */
    public static void info(String message,Object... args){
        print(message,"-INFO-",args);
    }

    /**
     * 输出 ERROR 级别信息
     * @param message
     * @param args
     */
    public static void error(String message,Object... args){
        print(message,"-ERROR-",args);
    }

    /**
     * 输出当前信息
     * @param msg
     * @param level
     * @param args
     */
    private static void print(String msg,String level,Object ... args){
        if (args != null && args.length != 0){
            msg = String.format(msg.replace("()","%s"),args);
        }
        String name = Thread.currentThread().getName();
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) +" "+ name + " " + msg  );
    }
}
