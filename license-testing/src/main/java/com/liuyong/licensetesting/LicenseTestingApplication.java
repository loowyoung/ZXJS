package com.liuyong.licensetesting;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LicenseTestingApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(LicenseTestingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("run方法");
    }
}
