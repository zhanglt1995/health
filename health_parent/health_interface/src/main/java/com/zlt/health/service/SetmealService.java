package com.zlt.health.service;

import com.zlt.health.VO.PageResult;
import com.zlt.health.VO.QueryPageBean;
import com.zlt.health.exception.HealthException;
import com.zlt.health.pojo.Setmeal;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/19 23:09
 * @desc
 */
public interface SetmealService {
    /**
     * 添加
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 更新
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 删除
     * @param id
     */
    void deleteById(int id) throws HealthException;

    /**
     * 查询检查组集合
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 查询所有
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);
}
