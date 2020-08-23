package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zlt.health.dao.UserDao;
import com.zlt.health.pojo.User;
import com.zlt.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhanglitao
 * @create 2020/8/23 17:43
 * @desc
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}
