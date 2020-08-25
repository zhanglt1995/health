package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.dao.MemberDao;
import com.zlt.health.dao.OrderDao;
import com.zlt.health.dao.OrderSettingDao;
import com.zlt.health.exception.HealthException;
import com.zlt.health.pojo.Member;
import com.zlt.health.pojo.Order;
import com.zlt.health.pojo.OrderSetting;
import com.zlt.health.service.OrderService;
import com.zlt.health.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/22 18:08
 * @desc
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order submitOrder(Map orderInfo) {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDateStr = (String) orderInfo.get("orderDate");

        Date orderDate = null;
        try {
            orderDate = DateUtils.parseString2Date(orderDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if(orderSetting == null){
            throw new HealthException(MessageConstants.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        if(orderSetting.getNumber() <= orderSetting.getReservations()){
            throw new HealthException(MessageConstants.ORDER_FULL);
        }
        // 应该是先检查当前用户是否为会员，如果不是会员则注册成会员并进行预约，如果是会员，则判断是否在同一天预约了同一个套餐
        String phoneNum = (String) orderInfo.get("telephone");
        Member member = memberDao.getMemberByPhoneNum(phoneNum);
        if(member == null){
            member = new Member();
            member.setIdCard((String) orderInfo.get("idCard"));
            member.setPhoneNumber(phoneNum);
            member.setName((String) orderInfo.get("name"));
            member.setSex((String) orderInfo.get("sex"));
            member.setRegTime(new Date());
            // 注册用户
            memberDao.add(member);
        }
        Integer setmealId = Integer.valueOf((String) orderInfo.get("setmealId"));
        Order order = orderDao.getOrderByMemberIdAndMealId(member.getId(),orderDate,setmealId);

        if(order != null){
            throw new HealthException(MessageConstants.HAS_ORDERED);
        }
        // 设置order数据的值
        order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(orderDate);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setSetmealId(setmealId);
        // 添加预约信息
        orderDao.add(order);
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约

        //5、预约成功，更新当日的已预约人数
        orderSettingDao.updateReservations(orderSetting);
        return order;
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        return orderDao.findById(id);
    }
}
