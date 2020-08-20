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

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        // 获取前台传输的文件
        String originalFilename = imgFile.getOriginalFilename();
        // 获取图片的后缀名 .jpg
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成唯一的文件名
        String uniqueName = UUID.randomUUID().toString() + ext;
        // 上传的图片，调用QiNiuUtils把图片上传到七牛
        Jedis jedis = jedisPool.getResource();
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), uniqueName);
            // 保存所有上传的图片到redis集合中
            jedis.sadd(RedisConstants.SETMEAL_PIC_RESOURCES, uniqueName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstants.PIC_UPLOAD_FAIL);
        } finally {
            if(null != jedis) {
                jedis.close(); // 返回jedis连接池
            }
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
        // 添加成功，要记录有用的图片到redis集合中
        Jedis jedis = jedisPool.getResource();
        jedis.sadd(RedisConstants.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        jedis.close();
        // 响应结果
        return new Result(true, MessageConstants.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        Jedis jedis = jedisPool.getResource();
        // 旧的套餐数据
        Setmeal oldSetmeal = setmealService.findById(setmeal.getId());
        // 调用业务服务修改
        setmealService.update(setmeal, checkgroupIds);
        // 先删除旧图片
        jedis.srem(RedisConstants.SETMEAL_PIC_DB_RESOURCES, oldSetmeal.getImg());
        // 修改时，有可能图片也被改，即没有改图片，set集合，也不会重复
        jedis.sadd(RedisConstants.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        jedis.close();
        // 响应结果
        return new Result(true, MessageConstants.EDIT_SETMEAL_SUCCESS);
    }

    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 查询要删除的套餐图片名称
        Setmeal setmeal = setmealService.findById(id);
        // 调用服务删除
        setmealService.deleteById(id);
        Jedis jedis = jedisPool.getResource();
        // 删除旧图片
        jedis.srem(RedisConstants.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
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
        // 前端要显示图片需要全路径
        // setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        // setmeal通过上面的语句，img代表全路径=> formData绑定， img也是全路径 => 提交过来的setmeal.img全路径, 截取字符串 获取图片的名称
        // 封装到map中，解决图片路径问题
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("setmeal", setmeal); // formData
        resultMap.put("imageUrl", QiNiuUtils.DOMAIN + setmeal.getImg()); // imageUrl
        return new Result(true, MessageConstants.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstants.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
    }
}
