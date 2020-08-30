package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zlt.health.dao.MenuDao;
import com.zlt.health.pojo.Menu;
import com.zlt.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/29 20:53
 * @desc
 */
@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> findMenuListByUserId(Integer userId) {
        return menuDao.findMenuListByUserId(userId);
    }
}
