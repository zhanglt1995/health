package com.zlt.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.constant.RedisConstants;
import com.zlt.health.util.SMSUtils;
import com.zlt.health.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author zhanglitao
 * @create 2020/8/22 17:51
 * @desc
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送手机验证码
     *
     * @param telephone
     * @return
     */
    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 生成验证码之前要检查一下是否发送过了, 通过redis获取key为手机号码，看是否存在
        Jedis jedis = jedisPool.getResource();
        String key = RedisConstants.SENDTYPE_ORDER + "_" + telephone;
        // redis中的验证码
        String codeInRedis = jedis.get(key);
        if (null == codeInRedis) {
            // 不存在，没发送，生成验证码，调用SMSUtils.发送验证码，把验证码存入redis(5,10,15分钟有效期)，value:验证码, key:手机号码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                // 发送验证码
                // SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                // 存入redis，有效时间为15分钟
                jedis.setex(key, 5*60, code + "");
                // 返回成功
                return new Result(true, MessageConstants.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                // 发送失败
                return new Result(false, MessageConstants.SEND_VALIDATECODE_FAIL);
            }
        }
        // 存在：验证码已经发送了，请注意查收
        return new Result(false, MessageConstants.SENT_VALIDATECODE);
    }

    @GetMapping("/send4Login")
    public Result send4Login(String telephone){
        //- 生成验证码之前要检查一下是否发送过了, 通过redis获取key为手机号码，看是否存在
        Jedis jedis = jedisPool.getResource();
        String key = RedisConstants.SENDTYPE_LOGIN + "_" + telephone;
        // redis中的验证码
        String codeInRedis = jedis.get(key);
        if (null == codeInRedis) {
            //- 不存在，没发送，生成验证码，调用SMSUtils.发送验证码，把验证码存入redis(5,10,15分钟有效期)，value:验证码, key:手机号码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                // 发送验证码
                //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                // 存入redis，有效时间为15分钟
                jedis.setex(key, 15*60, code + "");
                // 返回成功
                return new Result(true, MessageConstants.SEND_VALIDATECODE_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                // 发送失败
                return new Result(false, MessageConstants.SEND_VALIDATECODE_FAIL);
            }
        }
        // 存在：验证码已经发送了，请注意查收
        return new Result(false, MessageConstants.SENT_VALIDATECODE);
    }
}
