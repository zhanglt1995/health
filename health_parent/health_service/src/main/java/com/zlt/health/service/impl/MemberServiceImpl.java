package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zlt.health.dao.MemberDao;
import com.zlt.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/24 9:21
 * @desc
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public List<Integer> findMemberCountListByMonth(List<String> monthList) {
        List<Integer> monthCountList = null;
        if (!CollectionUtils.isEmpty(monthList)) {
            monthCountList = new ArrayList<>();
            for (String month : monthList) {
                Integer count = memberDao.findMemberCountByMonth(month+"-31");
                monthCountList.add(count);
            }
        }
        return monthCountList;
    }
}
