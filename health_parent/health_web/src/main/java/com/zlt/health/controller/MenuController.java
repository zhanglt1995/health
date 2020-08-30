package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.pojo.Menu;
import com.zlt.health.service.MenuService;
import com.zlt.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/30 9:06
 * @desc
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private UserService userService;

    @Reference
    private MenuService menuService;

    @GetMapping("/findMenuList")
    public Result findMenuList(){
        // 获取session中的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 查询数据库中的数据
        com.zlt.health.pojo.User userInDb = userService.getUserByUsername(username);
        // 根据用户id查询菜单列表
        List<Menu> menuList = menuService.findMenuListByUserId(userInDb.getId());
        return new Result(true, MessageConstants.GET_MENU_SUCCESS,menuList);
    }
}
