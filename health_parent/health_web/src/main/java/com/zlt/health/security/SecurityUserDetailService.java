package com.zlt.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.pojo.Permission;
import com.zlt.health.pojo.Role;
import com.zlt.health.pojo.User;
import com.zlt.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhanglitao
 * @create 2020/8/23 17:26
 * @desc
 */
@Component
public class SecurityUserDetailService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 通过用户名去查找用户
        User user = userService.getUserByUsername(username);
        if(user != null){
            // 根据查询获取密码
            String password = user.getPassword();
            // 创建一个GrantedAuthority的集合
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            // 获取角色
            Set<Role> roleSet = user.getRoles();
            SimpleGrantedAuthority sga = null;
            if (roleSet != null) {
                for (Role role : roleSet) {
                    // 角色用关键字, 授予角色
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    grantedAuthorityList.add(sga);
                    // 权限, 角色下的所有权限
                    Set<Permission> permissionSet = role.getPermissions();
                    if(null != permissionSet){
                        for (Permission permission : permissionSet) {
                            // 授予权限
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            grantedAuthorityList.add(sga);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(username,password,grantedAuthorityList);
        }
        return null;
    }
}
