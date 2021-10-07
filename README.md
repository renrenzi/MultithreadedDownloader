# Multithreaded—Downloader
一个java实现多线程下载器小demo,目前只实现分片下载,断点续传还未完成.
# 控制台演示图
  <img src="https://github.com/Zinner2/MultithreadedDownloader/blob/master/code/src/com/jj/img/01.png" />  

# <a href="https://github.com/Zinner2/MultithreadedDownloader/tree/master/code/src/com/jj/core"/>  1.core (核心模块) </a>

    // 文件下载
    public void downLoadFile(String url)

    // 文件切割
    public void split(String url, ArrayList<Future> futureList)
    
    // 合成文件
    public boolean mergeFile(String fileName)
    
    // 清除临时文件
    public void cleanTemp(String fileName)
# <a href="https://github.com/Zinner2/MultithreadedDownloader/tree/master/code/src/com/jj/util"/>2.util (工具模块) </a>
   
    // 本地文件工具类
    public class FileUtils 
    
    // http连接工具类
    public class HttpUtils 
    
    // 日志工具类
    public class LogUtils 
# <a href="https://github.com/Zinner2/MultithreadedDownloader/tree/master/code/src/com/jj/constant"/>3.constant (常量模块) </a>
   
    // 常量类
    public class Constant

  
