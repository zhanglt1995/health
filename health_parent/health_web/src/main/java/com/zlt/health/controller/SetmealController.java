package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.VO.PageResult;
import com.zlt.health.VO.QueryPageBean;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.constant.RedisConstants;
import com.zlt.health.pojo.Setmeal;
import com.zlt.health.service.SetmealService;
import com.zlt.health.util.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhanglitao
 * @create 2020/8/19 20:59
 * @desc
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        // 获取前台传输的文件
        String originalFilename = imgFile.getOriginalFilename();
        // 获取图片的后缀名 .jpg
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成唯一的文件名
        String uniqueName = UUID.randomUUID().toString() + ext;

        try {
            // 上传的图片，调用QiNiuUtils把图片上传到七牛
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), uniqueName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstants.PIC_UPLOAD_FAIL);
        }
        Map<String,String> dataMap = new HashMap<String,String>();
        dataMap.put("imgName", uniqueName);
        dataMap.put("domain", QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstants.PIC_UPLOAD_SUCCESS,dataMap);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用业务服务添加
        setmealService.add(setmeal, checkgroupIds);

        // 响应结果
        return new Result(true, MessageConstants.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用业务服务修改
        setmealService.update(setmeal, checkgroupIds);

        // 响应结果
        return new Result(true, MessageConstants.EDIT_SETMEAL_SUCCESS);
    }

    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除
        setmealService.deleteById(id);
        return new Result(true, MessageConstants.DELETE_SETMEAL_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务分页查询
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstants.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询
        Setmeal setmeal = setmealService.findById(id);
        // 将数据封装到map集合中
        Map<String,Object> resultMap = new HashMap<>();
        // formData
        resultMap.put("setmeal", setmeal);
        // 前台的图片全路径
        resultMap.put("imageUrl", QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstants.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstants.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
    }
}
