package com.zlt.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.service.SetmealService;
import com.zlt.health.util.QiNiuUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/20 18:33
 * @desc
 */
@Component
public class CleanInvalidImg {
    @Reference
    private SetmealService setmealService;

    /**
     * 清理垃圾图片的执行方法
     */
    @Scheduled(cron = "0 0 4 * * ? ")
    public void cleanImg(){
        // 获取七牛云上的所有图片数据
        List<String> QiNiuImg = QiNiuUtils.listFile();
        // 获取数据库所有的图片
        List<String> DatabaseImg = setmealService.findImgList();
        // 将数据库中的移除，得到垃圾图片
        QiNiuImg.removeAll(DatabaseImg);

        // 将垃圾图片集合转成字符串数组
        String[] array = QiNiuImg.toArray(new String[]{});
        // 调用七牛删除
        QiNiuUtils.removeFiles(array);

    }
}
