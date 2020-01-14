package com.anxin.accidentsimulation.util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 读取信息
     *
     * @param file 文件输入流
     * @return
     */
    public static List<String> read(File file) {
        if (!file.exists()) {
            return null;
        }
        String temp = null;
        FileInputStream fipt = null;
        InputStreamReader read = null;
        BufferedReader reader = null;
        try {
            fipt = new FileInputStream(file);
            read = new InputStreamReader(fipt, "GBK");
            List<String> retList = new ArrayList<String>();
            reader = new BufferedReader(read);
            while ((temp = reader.readLine()) != null) {
                retList.add(temp.trim());
            }
            return retList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fipt != null) {
                try {
                    fipt.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 写入数据
     *
     * @param obj
     * @return
     */
    public static boolean write(File file, List<String> obj) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (obj == null || obj.size() < 1) {
            return false;
        }
        FileOutputStream outSTr = null;
        BufferedOutputStream buff = null;
        try {
            outSTr = new FileOutputStream(file);
            buff = new BufferedOutputStream(outSTr);
            for (String _str : obj) {
                buff.write((_str + "\r\n").getBytes("UTF-8"));
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buff.flush();
                if (buff != null) {
                    buff.close();
                }
                if (outSTr != null) {
                    outSTr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static String getJarFilesPath() {
        String jarPath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        if(!jarPath.endsWith(".jar")){
//            return jarPath;
//        }
        try {
            jarPath = java.net.URLDecoder.decode(jarPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int firstIndex = jarPath.lastIndexOf("/");

        return jarPath.substring(0, firstIndex);
    }


    public static String getResourcePath(String fileName) {
        URL url = FileUtil.class.getResource(fileName);
        if (url != null) {
            return url.getPath();
        } else
            return null;

    }
}
