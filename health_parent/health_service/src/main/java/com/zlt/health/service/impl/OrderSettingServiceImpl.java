package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zlt.health.dao.OrderSettingDao;
import com.zlt.health.exception.HealthException;
import com.zlt.health.pojo.OrderSetting;
import com.zlt.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/20 20:05
 * @desc
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
            // 判断是否存在, 通过日期来查询, 注意：日期里是否有时分秒，数据库里的日期是没有时分秒的
            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if(null != osInDB){
                // 数据库存在这个预约设置, 已预约数量不能大于可预约数量
                if(osInDB.getReservations() > orderSetting.getNumber()){
                    throw new HealthException(orderSetting.getOrderDate() + " 中已预约数量不能大于可预约数量");
                }
                orderSettingDao.updateNumber(orderSetting);
            }else{
                // 不存在
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        // 拼接上一个月的起始天数
        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        // 2.查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        // 3.将List<OrderSetting>，组织成List<Map>
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap(8);
            //获得日期（几号）
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            //可预约人数
            orderSettingMap.put("number",orderSetting.getNumber());
            //已预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());
            data.add(orderSettingMap);
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //- 存在：
        if(null != os) {
            // 判断已经预约的人数是否大于要更新的最大可预约人数， reverations > 传进来的number数量，则不能更新，要报错
            if(orderSetting.getNumber() < os.getReservations()){
                // 已经预约的人数高于最大预约人数，不允许
                throw new HealthException("最大预约人数不能小已预约人数");
            }
            // reverations <= 传进来的number数量，则要更新最大可预约数量
            orderSettingDao.updateNumber(orderSetting);
        }else {
            //- 不存在：
            //  - 添加预约设置信息
            orderSettingDao.add(orderSetting);
        }
    }
}
