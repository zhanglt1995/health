package com.zlt.health.dao;

import com.zlt.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author zhanglitao
 * @create 2020/8/30 10:33
 * @desc
 */
public interface RoleDao {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();

    /**
     * 根据用户id获取用户的所有角色
     * @param userId
     * @return
     */
    Set<Role> findRolesByUserId(@Param("userId") Integer userId);

    /**
     * 看是否有用户使用该角色
     * @param roleId
     * @return
     */
    Integer getUserRoleCountByRoleId(@Param("roleId") Integer roleId);

}
