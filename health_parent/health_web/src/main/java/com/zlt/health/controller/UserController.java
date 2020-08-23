package com.zlt.health.controller;

import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanglitao
 * @create 2020/8/23 19:28
 * @desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getUsername")
    public Result getUserName(){
        // 获取登录用户的信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        return new Result(true, MessageConstants.GET_USERNAME_SUCCESS,username);
    }
}
