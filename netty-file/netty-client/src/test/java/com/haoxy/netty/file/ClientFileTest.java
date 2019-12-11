package com.haoxy.netty.file;

import common.FileUploadFile;
import client.work.FileUploadClient;
import org.junit.Test;


import java.io.File;

/**
 * Created by haoxy on 2018/11/15.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class ClientFileTest {
    private static final int FILE_PORT = 9991;
    @Test
    public void testFile(){
        try {
            FileUploadFile uploadFile = new FileUploadFile();
            File file = new File("D:\\gitFile\\应急指挥文档\\01 开发域\\验收材料\\中国石化应急指挥系统验收报告.docx");//
            String fileMd5 = file.getName();// 文件名
            uploadFile.setFile(file);
            uploadFile.setFile_md5(fileMd5);
            uploadFile.setStarPos(0);// 文件开始位置
            new FileUploadClient().connect(FILE_PORT, "127.0.0.1", uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
