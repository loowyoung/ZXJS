package cn.com.safeinfo.watchdog.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件操作类
 *
 * @author: ly
 * @date: 2020/3/10 8:41
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static final String watchPath = System.getProperty("user.dir") + File.separator + "watchdog-projects";//watchdog的jar包所在路径

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
        logger.debug("文件删除：{}", file.getAbsolutePath());
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
            logger.warn("文件重命名，原文件不存在：{}", jarPath);
        }
        File newfile = new File(jarPath + ".bak");
        file.renameTo(newfile);
        logger.debug("文件重命名：{}\n{}", file.getAbsolutePath(), newfile.getAbsolutePath());
        return newfile;//返回更改后的文件
    }

    /**
     * 上传文件到服务器
     *
     * @param file jar包
     * @return
     */
    public static File upLoadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        File dest = new File(watchPath + File.separator + fileName);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            logger.debug("文件上传：{}", dest);
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            logger.debug("写入文件位置：{}，写入内容：{}", file.getAbsolutePath(), content);
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
        logger.debug("读取文件位置：{}，文件内容：{}", file.getAbsolutePath(), result);
        return result.toString();
    }

}
