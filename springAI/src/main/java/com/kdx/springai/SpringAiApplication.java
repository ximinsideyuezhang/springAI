package com.kdx.springai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAiApplication {

    public static void main(String[] args) {
        System.setProperty("proxyType", "4");   //类型
        System.setProperty("proxyPort", "7890");    //端口
        System.setProperty("proxyHost", "127.0.0.1");   //ip
        System.setProperty("proxySet", "true");
        SpringApplication.run(SpringAiApplication.class, args);
    }

}
