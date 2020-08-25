package com.zlt.health.service;

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
}
