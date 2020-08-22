package com.zlt.health.job;

import com.zlt.health.constant.RedisConstants;
import com.zlt.health.util.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author zhanglitao
 * @create 2020/8/20 18:33
 * @desc
 */
@Component
public class ClearInvalidImg {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理垃圾图片的执行方法
     */
    @Scheduled(cron = "0 0 4 * * ? ")
    public void clearImg(){
        // 获取 redis的连接
        Jedis jedis = jedisPool.getResource();
        // 计算2个set集合的差集 所有七牛图片-保存到数据库
        // 需要删除的图片
        Set<String> need2Delete = jedis.sdiff(RedisConstants.SETMEAL_PIC_RESOURCES, RedisConstants.SETMEAL_PIC_DB_RESOURCES);
        // 调用七牛删除
        QiNiuUtils.removeFiles(need2Delete.toArray(new String[]{}));
        // 删除redis上的图片, 两边的图片已经同步了
        jedis.del(RedisConstants.SETMEAL_PIC_RESOURCES, RedisConstants.SETMEAL_PIC_DB_RESOURCES);
    }
}
