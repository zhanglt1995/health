package com.zlt.health.service;

import com.zlt.health.exception.HealthException;
import com.zlt.health.pojo.Order;

import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/22 18:07
 * @desc
 */
public interface OrderService {
    /**
     * 添加预约信息
     * @param orderInfo
     * @return
     */
    Order submitOrder(Map orderInfo) throws HealthException;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Map<String, Object> findById(Integer id);
}
