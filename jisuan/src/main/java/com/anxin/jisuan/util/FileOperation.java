package com.anxin.jisuan.util;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取文档并把其中的数据转化为二维数组
 *
 * @author: ly
 * @date: 2018/12/7 14:43
 */
@Service
public class FileOperation {

    //将文档中的内容转换为二维数组
    public static String[][] getFileArray(String pathName) throws Exception {
        java.io.File file = new java.io.File(pathName);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在!");
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str = "";
        List<String[]> list = new ArrayList<>();
        while ((str = br.readLine()) != null) {
            int j = 0;
            String[] arr = str.split(" ");
            list.add(arr);
        }
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i).length) {
                max = list.get(i).length;
            }
        }
        String[][] result = new String[list.size()][max];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                result[i][j] = list.get(i)[j];
            }
        }
        return result;
    }

}
