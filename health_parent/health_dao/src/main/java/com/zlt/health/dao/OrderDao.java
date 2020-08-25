package com.zlt.health.dao;

import com.zlt.health.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.ast.Or;

import java.util.Date;
import java.util.List;
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
    Order getOrderByMemberIdAndMealId(@Param("memberId") Integer memberId,@Param("orderDate") Date orderDate,
                                      @Param("setmealId") Integer setmealId);

    /**
     * 添加预约信息
     * @param order
     */
    void add(Order order);

    /**
     * 获取今日预约数
     * @param reportDate
     * @return
     */
    Integer findOrderCountByDate(String reportDate);

    /**
     * 获取今日到诊数
     * @param reportDate
     * @return
     */
    Integer findVisitsCountByDate(String reportDate);

    /**
     * 获取时间段内的预约数
     * @param startDate
     * @param endDate
     * @return
     */
    Integer findOrderCountBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取时间段内的到诊数
     * @param startDate
     * @param endDate
     * @return
     */
    Integer findVisitsCountBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取热门套餐数据
     * @return
     */
    List<Map<String, Object>> findHotSetmeal();

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    Map<String, Object> findById(Integer id);
}
