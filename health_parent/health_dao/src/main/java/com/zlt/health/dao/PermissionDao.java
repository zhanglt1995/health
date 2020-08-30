package com.zlt.health.dao;

import com.github.pagehelper.Page;
import com.zlt.health.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author zhanglitao
 * @create 2020/8/30 10:50
 * @desc
 */
public interface PermissionDao {
    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

    /**
     * 根据角色id获取权限
     * @param roleId
     * @return
     */
    Set<Permission> findPermissionsByRoleId(@Param("roleId") Integer roleId);

    /**
     * 插件使用分页查询
     * @param queryString
     * @return
     */
    Page<Permission> findByPage(@Param("queryString") String queryString);

    /**
     * 根据权限id查询是否有角色用过该权限
     * @param permissionId
     * @return
     */
    Integer getRolePermissionCountByPermissionId(@Param("permissionId") Integer permissionId);

    /**
     * 添加权限
     * @param permission
     */
    void add(Permission permission);

    /**
     * 更新操作
     * @param permission
     */
    void update(Permission permission);

    /**
     * 根据id删除权限
     * @param id
     */
    void deleteById(@Param("id") Integer id);
}
