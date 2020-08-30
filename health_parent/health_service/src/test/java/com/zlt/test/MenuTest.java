package com.zlt.test;

import com.zlt.health.dao.MenuDao;
import com.zlt.health.pojo.Menu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/29 20:47
 * @desc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-service.xml")
public class MenuTest {
    @Autowired
    private MenuDao menuDao;

    @Test
    public void test01(){
        List<Menu> menuList = menuDao.findMenuListByUserId(1);
        System.out.println(menuList);
    }
}
