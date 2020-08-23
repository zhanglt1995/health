package com.zlt.health.dao;

import com.zlt.health.pojo.Order;

import java.util.Date;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/22 18:09
 * @desc
 */
public interface OrderDao {
    /**
     * 查看一个用户在用一天是否预约过同一个套餐
     * @param id
     * @param orderDate
     * @param setmealId
     * @return
     */
    Order getOrderByMemberIdAndMealId(Integer id, Date orderDate, Integer setmealId);

    /**
     * 添加预约信息
     * @param orderInfo
     */
    void add(Map orderInfo);
}
