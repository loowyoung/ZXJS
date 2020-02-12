package com.anxin.jisuan.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 测试SMB2是否能从共享机器上读取文件
 *
 * @author: ly
 * @date: 2020/2/12 15:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Smb2FileUtilTest {
    @Autowired
    private Smb2FileUtil smb2FileUtil;

    @Test
    public void smb2Test() {
        String dst = "F:\\";
        assertTrue(smb2FileUtil.smbFileDowload(dst));
    }

}