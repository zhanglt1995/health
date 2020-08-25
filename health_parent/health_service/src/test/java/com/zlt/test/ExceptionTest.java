package com.zlt.test;

import com.zlt.health.exception.HealthException;
import com.zlt.health.pojo.OrderSetting;
import com.zlt.health.service.impl.OrderSettingServiceImpl;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @Test
    public void test05(){
        List<String> months = new ArrayList<String>();
        // 使用java中的calendar来操作日期, 日历对象
        Calendar calendar = Calendar.getInstance();
        // 设置过去一年的时间  年-月-日 时:分:秒, 减去12个月
        calendar.add(Calendar.MONTH, -12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 构建12个月的数据
        for (int i = 0; i < 12; i++) {
            // 每次增加一个月就
            calendar.add(Calendar.MONTH, 1);
            // 过去的日期, 设置好的日期
            Date date = calendar.getTime();
            // 2020-06 前端只展示年-月
            months.add(sdf.format(date));
        }
        System.out.println(months);
    }
}
