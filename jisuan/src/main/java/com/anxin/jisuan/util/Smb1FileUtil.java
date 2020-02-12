package com.anxin.jisuan.util;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import java.io.*;

/**
 * jcifs只支持SMB1，win10已经淘汰；
 * smbj或smbj支持SMB2和SMB3,建议升级；
 * win10打开SMB1协议，须在程序和功能中开启；
 *
 * @author: ly
 * @date: 2020/2/12 10:21
 */
public class Smb1FileUtil {

    public static void main(String[] args) {
        //此处采用优先验证登录用户信息的方法访问
        String url = "smb://127.0.0.1/logs";
        String ip = "127.0.0.1";
        String name = "yan";
        String password = "123";
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(ip, name, password);
        // 下载文件的smb url地址、下载存放地址、用户认证信息
        smbFileDowload(url + "/modelInfos.log", "F:\\", auth);
    }

    //从共享目录下载文件
    public static void smbFileDowload(String remoteUrl, String localDir, NtlmPasswordAuthentication auth) {
        InputStream in = null;
        OutputStream out = null;
        try {
            SmbFile remoteFile = new SmbFile(remoteUrl, auth);
            if (remoteFile == null) {
                System.out.println("共享文件不存在");
                return;
            }
            String fileName = remoteFile.getName();
            File localFile = new File(localDir + File.separator + fileName);
            in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
            out = new BufferedOutputStream(new FileOutputStream(localFile));
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
