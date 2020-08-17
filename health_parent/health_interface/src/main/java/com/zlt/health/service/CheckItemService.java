package com.zlt.health.service;

import com.zlt.health.pojo.CheckItem;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/17 16:44
 * @desc
 */
public interface CheckItemService {
    /**
     * 查询所有检测项
     * @return
     */
    List<CheckItem> findAll();
}