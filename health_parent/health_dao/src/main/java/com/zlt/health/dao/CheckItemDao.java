package com.zlt.health.dao;

import com.github.pagehelper.Page;
import com.zlt.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 添加
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 使用插件分页查询
     * @return
     */
    Page<CheckItem> findByPage(@Param("queryString") String queryString);
}
