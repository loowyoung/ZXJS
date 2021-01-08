package com.liuyong.logreader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ly
 * @date 2021年 01月07日 16:56:37
 */
@Component
public class TailerTest {
    @Resource
    private WebSocketService webSocketService;
    private Thread thread;

    //public static void main(String []args) throws Exception{
    //    TailerTest tailerTest = new TailerTest();
    //    tailerTest.test();
    //    //boolean flag = true;
    //    //File file = new File("E:\\u01/1.txt");
    //    //
    //    //while(flag){
    //    //    Thread.sleep(1000);
    //    //    FileUtils.write(file,""+System.currentTimeMillis()+ IOUtils.LINE_SEPARATOR,true);
    //    //}
    //
    //}

    public void test() throws Exception {
        File file = new File("/u01/apps/hrmw_v2/01-target-simulator/nohup.out");
        FileUtils.touch(file);

        Tailer tailer = new Tailer(file, getListener("ly"), 4000, true);
        //tailer.run();
        thread = new Thread(tailer);
        thread.start();
        //tailer.stop();
        //return tailer;
    }

    public void stop() {
        try {
            System.out.println("关闭tail");
            //tailer.stop();
            thread.interrupt();
        } catch (Exception e) {
        }
    }

    private TailerListenerAdapter getListener(String name) {
        return new TailerListenerAdapter() {

            @Override
            public void fileNotFound() {  //文件没有找到
                System.out.println("文件没有找到");
                super.fileNotFound();
            }

            @Override
            public void fileRotated() {  //文件被外部的输入流改变
                System.out.println("文件rotated");
                super.fileRotated();
            }

            @Override
            public void handle(String line) { //增加的文件的内容
                System.out.println(line);
                webSocketService.sendInfo(name, line);
                super.handle(line);
            }

            @Override
            public void handle(Exception ex) {
                ex.printStackTrace();
                super.handle(ex);
            }

        };
    }

    /**
     * 读取文件最后几行
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        File file = new File("E:\\\\u01/1.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        long length = randomAccessFile.length();
        long numRead = 5;
        List<String> result = new ArrayList<>();
        long count = 0;//读取的行数，计数
        //开始位置
        //long pos = length;
        while (length > 0) {
            length--;//往前推偏移量
            randomAccessFile.seek(length); //设置偏移量
            if (randomAccessFile.readByte() == '\n') {//有换行符，则读取该行
                String line = new String(randomAccessFile.readLine().getBytes("ISO-8859-1"), "UTF-8");
                result.add(line);
                count++;
                if (count == numRead) {//满足指定行数，退出
                    break;
                }
            }
        }
        System.out.println(result);
        //if (fileRows > 10) {
        //    randomAccessFile.seek(fileRows - 10);
        //byte[] b = new byte[1024];
        //int hasRead = 0;
        ////循环读取文件
        //while ((hasRead = randomAccessFile.read(b)) > 0) {
        //    //输出文件读取的内容
        //    System.out.print(new String(b, 0, hasRead));
        //}
        //}

    }
}
