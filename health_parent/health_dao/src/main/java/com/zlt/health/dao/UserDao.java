package com.zlt.health.dao;

import com.zlt.health.pojo.Permission;
import com.zlt.health.pojo.Role;
import com.zlt.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author zhanglitao
 * @create 2020/8/23 17:46
 * @desc
 */
public interface UserDao {
    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    User getUserByUsername(@Param("username") String username);

    /**
     * 根据用户id获取用户的所有角色
     * @param userId
     * @return
     */
    Set<Role> findRolesByUserId(@Param("userId") Integer userId);

    Set<Permission> findPermissionsByRoleId(@Param("roleId") Integer roleId);
}
