package com.zlt.test;

import com.zlt.health.exception.HealthException;
import com.zlt.health.pojo.OrderSetting;
import com.zlt.health.service.impl.OrderSettingServiceImpl;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhanglitao
 * @create 2020/8/22 8:45
 * @desc
 */
public class ExceptionTest {
    @Test
    public void test01(){
        throw new HealthException("自定义异常");
    }

    @Test
    public void test02(){
        try {
            test01();
        }catch (HealthException e) {
            e.printStackTrace();
            System.out.println("自定义的");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("进入了Exception的异常");
        }
    }

    private OrderSettingServiceImpl orderSettingService = new OrderSettingServiceImpl();

    @Test
    public void test03() {
        try {
            OrderSetting orderSetting = new OrderSetting();
            orderSetting.setNumber(10);
            orderSetting.setOrderDate(new Date());
            orderSettingService.editNumberByDate(orderSetting);
        } catch (HealthException e) {
            System.out.println(e.getErrorMsg());
            e.printStackTrace();
        }
    }

    @Test
    public void test04(){
        BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(cryptPasswordEncoder.encode("admin"));
    }
}
