package com.anxin.jisuan.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * smbj支持SMB2和SMB3
 *
 * @author: ly
 * @date: 2020/2/12 13:41
 */
@Component
public class Smb2FileUtil {
    @Value("${share.ip}")
    private String IP;
    @Value("${share.username}")
    private String SHARE_USER;
    @Value("${share.password}")
    private String SHARE_PASSWORD;
    @Value("${share.dir}")
    private String SHARE_DIR;
    @Value("${share.filename}")
    private String SHARE_FILENAME;

    public boolean smbFileDowload(String dstRoot) {
        boolean result = false;
        AuthenticationContext ac = new AuthenticationContext(SHARE_USER, SHARE_PASSWORD.toCharArray(), "");
        // 设置超时时间(可选)
        SmbConfig config = SmbConfig.builder().withTimeout(120, TimeUnit.SECONDS)
                .withTimeout(120, TimeUnit.SECONDS) // 超时设置读，写和Transact超时（默认为60秒）
                .withSoTimeout(180, TimeUnit.SECONDS) // Socket超时（默认为0秒）
                .build();
        SMBClient client = new SMBClient(config);

        try {
            Connection connection = client.connect(IP);
            Session session = connection.authenticate(ac);
            // 连接共享文件夹
            DiskShare share = (DiskShare) session.connectShare(SHARE_DIR);
            File f = share.openFile(SHARE_FILENAME,
                    new HashSet(Arrays.asList(AccessMask.GENERIC_ALL)),
                    new HashSet(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)),
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN,
                    new HashSet(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE))
            );
            String dstPath = dstRoot + SHARE_FILENAME;
            FileOutputStream fos = new FileOutputStream(dstPath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            if (share.fileExists(SHARE_FILENAME)) {
                System.out.println("正在下载文件:" + f.getFileName());
                InputStream in = f.getInputStream();
                byte[] buffer = new byte[4096];
                int len = 0;
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                bos.close();
                System.out.println("文件下载成功");
                result = true;
            } else {
                System.out.println("文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return result;//下载成功为true，否则false
    }

}
