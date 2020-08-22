package com.zlt.health.service;

import com.zlt.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/20 20:04
 * @desc
 */
public interface OrderSettingService {
    /**
     * 批量导入
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);

    /**
     * 根据月份获取预约信息
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 更改数量
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);
}
