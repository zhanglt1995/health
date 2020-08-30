package com.zlt.health.dao;

import com.zlt.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/29 20:24
 * @desc
 */
public interface MenuDao {
    /**
     * 根据用户id查询所有菜单
     * @param userId
     * @return
     */
    List<Menu> findMenuListByUserId(@Param("userId") Integer userId);

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> findAll();
}
