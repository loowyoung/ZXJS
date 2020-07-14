package com.liuyong.licensetesting.service;

import com.liuyong.licensetesting.utils.LicenseUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * springboot监听器
 *
 * @author ly
 * @date 2020年 07月13日 19:38:12
 */
@Service
public class StartAddDataListener implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private LicenseUtil licenseUtil;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        while (!licenseUtil.isAuthor()) {
            try {
                System.out.println("【无效授权码，请向应用开发商索取授权码！】");
                //TODO 生成申请码
                String requestCode = licenseUtil.createRequestCode();
                System.out.println("申请码：" + requestCode);

                System.out.print("请输入授权码：");
                Scanner inp = new Scanner(System.in);
                String authorCode = inp.next();
                //TODO 保存授权码至授权文件
                licenseUtil.saveCode(authorCode);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(401);
            }

        }
    }

}