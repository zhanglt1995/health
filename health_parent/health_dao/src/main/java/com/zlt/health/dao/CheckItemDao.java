package com.zlt.health.dao;

import com.zlt.health.pojo.CheckItem;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/17 16:48
 * @desc
 */
public interface CheckItemDao {
    /**
     * 查询所有检测项
     * @return
     */
    List<CheckItem> findAll();
}
