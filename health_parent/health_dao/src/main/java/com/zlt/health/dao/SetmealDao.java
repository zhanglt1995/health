package com.zlt.health.dao;

import com.github.pagehelper.Page;
import com.zlt.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/19 23:13
 * @desc
 */
public interface SetmealDao {
    /**
     * 添加
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     *
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId,@Param("checkgroupId") Integer checkgroupId);

    /**
     * 使用插件条件分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(@Param("queryString") String queryString);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 更新
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除关系表
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 根据id查询检查组集合
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 根据套餐id查询订单
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 删除
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有
     * @return
     */
    List<Setmeal> findAll();
}
