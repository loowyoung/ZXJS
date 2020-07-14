package com.liuyong.licensetesting.service;

import com.alibaba.fastjson.JSON;
import com.liuyong.licensetesting.model.LicenseDto;
import com.liuyong.licensetesting.utils.FileUtil;
import com.liuyong.licensetesting.utils.RSAEncryptUtil;
import com.liuyong.licensetesting.utils.SystemInfoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

/**
 * 授权工具类
 *
 * @author ly
 * @date 2020年 07月13日 19:41:29
 */
@Service
public class LicenseService {

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
     * 生成授权码
     *
     * @return
     */
    public void createResponseCode() throws Exception {
        System.out.print("请输入申请码：");
        Scanner inp1 = new Scanner(System.in);
        String requestCode = inp1.next();

        System.out.print("请输入私钥：");
        Scanner inp2 = new Scanner(System.in);
        String privateKey = inp2.next();

        String decryptCode = RSAEncryptUtil.decrypt(requestCode, privateKey);
        System.out.println(decryptCode);
        LicenseDto request = JSON.parseObject(decryptCode, LicenseDto.class);

        if (StringUtils.isEmpty(request.getCpuId()) || StringUtils.isEmpty(request.getMainboardNum())) {
            throw new IllegalArgumentException("无效的申请码：CPU或主板不能为空！");
        }

        System.out.print("请输入授权期限（默认180天）：");
        Scanner inp3 = new Scanner(System.in);
        String limitStr = inp3.next();
        if (StringUtils.isEmpty(limitStr)) {
            limitStr = "180";
        }
        LocalDateTime limitDate = LocalDateTime.now().plusDays(Long.valueOf(limitStr));
        request.setLimitDate(limitDate);

        System.out.print("请输入被授权公司名称：");
        Scanner inp4 = new Scanner(System.in);
        String customerName = inp4.next();
        request.setCustomerName(customerName);

        String info = JSON.toJSONString(request);
        System.out.println("授权码：" + RSAEncryptUtil.encrypt(info, privateKey));
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

    public void createSecretKey() throws Exception {
        Map<Integer, String> keyMap = RSAEncryptUtil.genKeyPair();
        System.out.println("公钥为(向外提供):" + keyMap.get(0));
        System.out.println("私钥为(妥善保存):" + keyMap.get(1));
    }
}
