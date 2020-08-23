package com.zlt.health.service;

import com.zlt.health.pojo.User;

/**
 * @author zhanglitao
 * @create 2020/8/23 17:43
 * @desc
 */
public interface UserService {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);
}
