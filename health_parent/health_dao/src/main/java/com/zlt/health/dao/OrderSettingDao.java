package com.zlt.health.dao;

import com.zlt.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/20 20:06
 * @desc
 */
public interface OrderSettingDao {
    /**
     * 查找当天是否有数据
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(@Param("orderDate") Date orderDate);

    /**
     * 更新数量
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 添加
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 查询一个月的预约信息
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map map);
}
