package com.zlt.security;

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
 * @create 2020/8/29 17:25
 * @desc
 */
@Component
public class SecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名去查找用户信息，包括用户的角色和权限
        User user = userService.getUserByUsername(username);
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        // 如果user不为null，则证明查找到
        if(user != null){
            // 获取到所有的角色，将角色信息存入list的集合中
            Set<Role> roleSet = user.getRoles();
            if (roleSet != null) {
                GrantedAuthority authority = null;
                for (Role role : roleSet) {
                    authority = new SimpleGrantedAuthority(role.getKeyword());
                    grantedAuthorityList.add(authority);
                    // 将角色的所有权限取出来
                    Set<Permission> permissionSet = role.getPermissions();
                    if (permissionSet != null) {
                        for (Permission permission : permissionSet) {
                            authority = new SimpleGrantedAuthority(permission.getKeyword());
                            grantedAuthorityList.add(authority);
                        }
                    }
                }
            }

            return new org.springframework.security.core.userdetails.User(username,user.getPassword(),grantedAuthorityList);
        }
        return null;
    }
}
