package com.haoxy.netty.file;

import server.work.FileUploadServer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by haoxy on 2018/11/15.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class ServerFileTest {

    private static final int FILE_PORT = 9991;

    @Test
    public void testServerFile() {
        try {
            new FileUploadServer().bind(FILE_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testaddFileLast() throws IOException {
        String filePath = "F:\\测试.txt";
        RandomAccessFile raf = null;
        File file = null;
        try {
            file = new File(filePath);
            // 以读写的方式打开一个RandomAccessFile对象
            raf = new RandomAccessFile(file, "rw");
            //将记录指针移动到该文件的最后
            raf.seek(raf.length());
            //向文件末尾追加内容
            raf.writeUTF("这是追加内容。。");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            raf.close();
        }
    }


}
