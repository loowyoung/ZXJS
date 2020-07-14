package com.liuyong.licensetesting.utils;

import com.alibaba.fastjson.JSON;
import com.liuyong.licensetesting.model.LicenseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;

/**
 * 授权工具类
 *
 * @author ly
 * @date 2020年 07月13日 19:41:29
 */
@Component
public class LicenseUtil {

    @Value("${license.publicKey}")
    private String publicKey;

    //授权文件名
    private static final String licenseName = "license.infor";

    /**
     * 检查是否有授权码
     *
     * @return
     */
    public Boolean isAuthor() {
        File licenseFile = new File(FileUtil.defaultPath + File.separator + licenseName);
        if (!licenseFile.exists()) {
            return false;
        }
        String encryptContent = FileUtil.readFile(licenseFile);
        String decryptContent = null;
        try {
            decryptContent = RSAEncryptUtil.decrypt(encryptContent, publicKey);
        } catch (Exception e) {
            return false;
        }
        LicenseDto param = JSON.parseObject(decryptContent, LicenseDto.class);
        if (SystemInfoUtil.getCpuId().equalsIgnoreCase(param.getCpuId())
                && SystemInfoUtil.getMainBoardNum().equalsIgnoreCase(param.getMainboardNum())
                && LocalDateTime.now().isBefore(param.getLimitDate())) {
            System.out.println("授权期限至：" + param.getLimitDate());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成申请码
     *
     * @return
     */
    public String createRequestCode() throws Exception {
        LicenseDto licenseDto = new LicenseDto();
        licenseDto.setCpuId(SystemInfoUtil.getCpuId());
        licenseDto.setMainboardNum(SystemInfoUtil.getMainBoardNum());
        licenseDto.setApplicant(SystemInfoUtil.getUserName());
        licenseDto.setIp(SystemInfoUtil.getIp());
        licenseDto.setUserDir(SystemInfoUtil.getJarPath());
        String info = JSON.toJSONString(licenseDto);
        return RSAEncryptUtil.encrypt(info, publicKey);
    }

    /**
     * 保存授权码
     *
     * @param authorCode
     */
    public void saveCode(String authorCode) {
        File licenseFile = new File(FileUtil.defaultPath + File.separator + licenseName);
        FileUtil.writeFile(licenseFile, authorCode);
    }
}
