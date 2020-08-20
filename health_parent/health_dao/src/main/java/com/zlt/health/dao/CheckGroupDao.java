package com.zlt.health.dao;

import com.github.pagehelper.Page;
import com.zlt.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/18 19:32
 * @desc
 */
public interface CheckGroupDao {
    /**
     * 插件分页查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByPage(@Param("queryString") String queryString);

    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 插入关系表
     * @param checkitemIds
     * @param checkGroupId
     */
    void addCheckGroupAndCheckItem(@Param("checkitemIds") List<Integer> checkitemIds, @Param("checkGroupId") Integer checkGroupId);

    /**
     * 根基id查询
     * @param id
     * @return
     */
    CheckGroup findById(@Param("id") Integer id);

    /**
     * 更新
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     * 根据检查组id删除检查组和检查项的关系表
     * @param groupId
     */
    void deleteGroupAndItemByGroupId(@Param("groupId") Integer groupId);

    /**
     * 根据id删除检查组
     * @param id
     */
    void deleteById(@Param("id") Integer id);

    /**
     * 查询所有
     * @return
     */
    List<CheckGroup> findAll();
}
