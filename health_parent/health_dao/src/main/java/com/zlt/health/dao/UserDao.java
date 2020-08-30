package com.zlt.health.dao;

import com.github.pagehelper.Page;
import com.zlt.health.pojo.Permission;
import com.zlt.health.pojo.Role;
import com.zlt.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
     * 使用插件分页查询
     * @param queryString
     * @return
     */
    Page<User> findByPage(@Param("queryString") String queryString);

    /**
     * 添加用户
     * @param user
     */
    void add(User user);

    /**
     * 更新用户
     * @param user
     */
    void update(User user);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteUserById(@Param("id") Integer id);

    /**
     * 根据用户id删除用户和角色的关系表
     * @param userId
     */
    void deleteUserRoleByUserId(@Param("userId") Integer userId);

    /**
     * 添加用户和角色的关系
     * @param userId
     * @param roleIds
     */
    void addUserRole(@Param("userId") Integer userId,@Param("roleIds") List<Integer> roleIds);
}
