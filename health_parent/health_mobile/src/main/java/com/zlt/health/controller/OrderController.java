package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.constant.RedisConstants;
import com.zlt.health.pojo.Order;
import com.zlt.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/22 18:05
 * @desc
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map orderInfo){
        // 获取手机号，先从redis中取出验证码
        String telephone = (String) orderInfo.get("telephone");
        String telKey = RedisConstants.SENDTYPE_ORDER+"_"+telephone;

        // 获取一个jedis
        Jedis jedis = jedisPool.getResource();
        // 从redis中获取
        String telCode = jedis.get(telKey);

        if(null == telCode){
            // 失效或没有发送
            return new Result(false, MessageConstants.TELEPHONE_VALIDATECODE_NOTNULL);
        }
        if(!telCode.equals(orderInfo.get("validateCode"))){
            return new Result(false, MessageConstants.VALIDATECODE_ERROR);
        }
        jedis.del(telKey);// 清除验证码，已经使用过了
        // 设置预约类型
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        // 调用服务预约
        Order order = orderService.submitOrder(orderInfo);
        return new Result(true, MessageConstants.ORDER_SUCCESS,order);
    }
}
