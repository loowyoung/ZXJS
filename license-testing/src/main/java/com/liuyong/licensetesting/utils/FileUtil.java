package com.liuyong.licensetesting.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 文件操作类
 *
 * @author: ly
 * @date: 2020/3/10 8:41
 */
@Slf4j
public class FileUtil {
    public static final String defaultPath = System.getProperty("user.dir");//jar包所在路径

    /**
     * 删除文件
     *
     * @param jarPath
     * @return
     */
    public static boolean deleteFile(String jarPath) {
        File file = new File(jarPath);
        if (!file.exists()) {//如果文件不存在
            return false;
        }
        file.delete();
        log.debug("文件删除：{}", file.getAbsolutePath());
        return true;
    }

    /**
     * 重命名文件
     *
     * @param jarPath
     * @return
     */
    public static File renameFile(String jarPath) {
        File file = new File(jarPath);
        if (!file.exists()) {//如果文件不存在
            log.warn("文件重命名，原文件不存在：{}", jarPath);
        }
        File newfile = new File(jarPath + ".bak");
        file.renameTo(newfile);
        log.debug("文件重命名：{}\n{}", file.getAbsolutePath(), newfile.getAbsolutePath());
        return newfile;//返回更改后的文件
    }

    /**
     * 文件内容写入
     *
     * @param file
     * @param content
     */
    public static void writeFile(File file, String content) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile()); //表示不追加
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            log.debug("写入文件位置：{}，写入内容：{}", file.getAbsolutePath(), content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件内容读取
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        StringBuilder result = new StringBuilder();//文件内容
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String data;
            while ((data = br.readLine()) != null) {
                result.append(data);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("读取文件位置：{}，文件内容：{}", file.getAbsolutePath(), result);
        return result.toString();
    }

}
