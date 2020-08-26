package com.zlt.health.service;

import com.zlt.health.pojo.Member;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/24 9:21
 * @desc
 */
public interface MemberService {
    /**
     * 获取最近12个月的会员数量集合
     * @param monthList
     * @return
     */
    List<Integer> findMemberCountListByMonth(List<String> monthList);

    /**
     * 根据电话号码查找用户
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member
     */
    void add(Member member);
}
