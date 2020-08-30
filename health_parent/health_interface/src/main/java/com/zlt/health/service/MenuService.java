package com.zlt.health.service;

import com.zlt.health.pojo.Menu;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/29 20:53
 * @desc
 */
public interface MenuService {
    List<Menu> findMenuListByUserId(Integer userId);
}
