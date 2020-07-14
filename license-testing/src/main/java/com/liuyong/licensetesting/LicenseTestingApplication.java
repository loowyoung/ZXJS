package com.liuyong.licensetesting;

import com.liuyong.licensetesting.service.LicenseService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.Scanner;

@SpringBootApplication
public class LicenseTestingApplication implements ApplicationRunner {
    @Resource
    private LicenseService licenseService;

    public static void main(String[] args) {
        SpringApplication.run(LicenseTestingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("1.生成密钥对");
        System.out.println("2.生成授权码");
        System.out.print("请输入数字：");
        Scanner inp = new Scanner(System.in);
        String code = inp.next();
        switch (code) {
            case "1":
                licenseService.createSecretKey();
                break;
            case "2":
                licenseService.createResponseCode();
                break;
            default:
                System.out.println("【无此功能！】");
                break;
        }
        System.exit(1);
    }
}
