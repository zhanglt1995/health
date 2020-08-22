package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.pojo.Setmeal;
import com.zlt.health.service.SetmealService;
import com.zlt.health.util.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/21 19:24
 * @desc
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        // 查询所有的套餐
        List<Setmeal> list = setmealService.findAll();
        // 套餐里有图片有全路径吗? 拼接全路径
        list.forEach(s->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        return new Result(true, MessageConstants.GET_SETMEAL_LIST_SUCCESS,list);
    }

    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findDetailById(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstants.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
