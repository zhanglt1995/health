package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.constant.RedisConstants;
import com.zlt.health.pojo.Member;
import com.zlt.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/26 19:34
 * @desc
 */
@RestController
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/login/check")
    public Result check(@RequestParam("loginInfo") Map<String, Object> loginInfo, HttpServletResponse response) {
        // 校验验证码是否是正确的
        Jedis jedis = jedisPool.getResource();
        // 获取前台传入的手机号和验证码
        String telephone = (String) loginInfo.get("telephone");
        String validateCode = (String) loginInfo.get("validateCode");

        // 获取redis缓存中的数据
        String code = jedis.get(RedisConstants.SENDTYPE_LOGIN + telephone);
        if (code == null) {
            return new Result(false, MessageConstants.TELEPHONE_VALIDATECODE_NOTNULL);
        }

        if(!code.equals(validateCode)){
            return new Result(false, MessageConstants.VALIDATECODE_ERROR);
        }
        // 验证通过后删除缓存的验证码，防止重复验证
        jedis.del(RedisConstants.SENDTYPE_LOGIN + telephone);

        // 通过手机号查询该用户是否存在，如果不存在，则注册
        Member member = memberService.findByTelephone(telephone);
        if(null == member){
            // 会员不存在，添加为新会员
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            member.setRemark("手机快速注册");
            memberService.add(member);
        }
        // 设置cookie信息，后面访问的可以获取到手机号
        Cookie cookie = new Cookie(MessageConstants.LOGIN_TELEPHONE_COOKIE,telephone);
        // 存1个月
        cookie.setMaxAge(30*24*60*60);
        // 访问的路径 根路径下时 网站的所有路径 都会发送这个cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        return new Result(true, MessageConstants.LOGIN_SUCCESS);
    }
}
