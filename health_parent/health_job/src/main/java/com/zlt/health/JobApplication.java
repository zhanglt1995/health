package com.zlt.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author zhanglitao
 * @create 2020/8/20 18:36
 * @desc
 */
public class JobApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:application-*.xml");
        System.in.read();
    }
}
