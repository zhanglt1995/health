package com.zlt.health.service;

import com.zlt.health.VO.PageResult;
import com.zlt.health.VO.QueryPageBean;
import com.zlt.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/18 19:30
 * @desc
 */
public interface CheckGroupService {
    /**
     * 条件分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 添加检查组
     * @param checkitemIds
     * @param checkGroup
     */
    void addCheckGroup(List<Integer> checkitemIds, CheckGroup checkGroup);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 更新
     * @param checkitemIds
     * @param checkGroup
     */
    void update(List<Integer> checkitemIds, CheckGroup checkGroup);

    /**
     * 删除方法
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 查询所有
     * @return
     */
    List<CheckGroup> findAll();
}
