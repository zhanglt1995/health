package com.zlt.health.dao;

import com.zlt.health.pojo.Member;

import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/22 20:55
 * @desc
 */
public interface MemberDao {
    /**
     * 根据电话号码查询用户
     * @param phoneNum
     * @return
     */
    Member getMemberByPhoneNum(String phoneNum);

    /**
     * 添加用户
     * @param orderInfo
     */
    void add(Map orderInfo);
}
