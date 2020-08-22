package com.zlt.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author zhanglitao
 * @create 2020/8/20 20:34
 * @desc
 */
public class ServiceApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:application-service.xml");
        System.in.read();
    }
}
