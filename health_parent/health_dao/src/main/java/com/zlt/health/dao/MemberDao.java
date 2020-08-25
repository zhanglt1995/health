package com.zlt.health.dao;

import com.zlt.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
     * @param member
     */
    void add(Member member);

    /**
     * 根据月份获取会员数量
     * @param month
     * @return
     */
    Integer findMemberCountByMonth(@Param("month") String month);

    /**
     * 根据条件查询会员数量
     * @param date
     * @return
     */
    Integer findMemberTotalCountByCondition(@Param("date") String date);
}
