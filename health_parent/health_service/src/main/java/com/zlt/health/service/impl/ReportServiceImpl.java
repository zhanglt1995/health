package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zlt.health.dao.MemberDao;
import com.zlt.health.dao.OrderDao;
import com.zlt.health.service.ReportService;
import com.zlt.health.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/24 11:19
 * @desc 将需要的数据全部封装到map中
 *
 * reportDate:null,
 * todayNewMember :0,
 * totalMember :0,
 * thisWeekNewMember :0,
 * thisMonthNewMember :0,
 * todayOrderNumber :0,
 * todayVisitsNumber :0,
 * thisWeekOrderNumber :0,
 * thisWeekVisitsNumber :0,
 * thisMonthOrderNumber :0,
 * thisMonthVisitsNumber :0,
 * hotSetmeal :[
 *     {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
 *     {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
 * ]
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() {

        // 星期一
        String monday = DateUtils.getFirstDayOfWeek();
        // 星期天
        String sunday = DateUtils.getLastDayOfWeek();
        // 1号
        String firstDayOfThisMonth = DateUtils.getFirstDay4ThisMonth();
        // 本月最后一天
        String lastDayOfThisMonth = DateUtils.getLastDayOfThisMonth();
        // 获取当前的日期
        String reportDate = DateUtils.getLocalDate();

        // 获取会员数量
        // 获取今日新增的会员数
        Integer todayNewMember = memberDao.findMemberTotalCountByCondition(reportDate);
        // 获取总的会员数
        Integer totalMember = memberDao.findMemberTotalCountByCondition(null);
        // 获取本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberTotalCountByCondition(monday);
        // 获取本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberTotalCountByCondition(firstDayOfThisMonth);

        // 订单数量的统计
        // 获取今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        // 获取今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        // 获取本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday, sunday);
        // 获取本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountBetweenDate(monday, sunday);
        // 获取本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        // 获取本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);

        // 获取热门套餐的数据
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();


        Map<String, Object> resutlMap = new HashMap<>(16);
        resutlMap.put("reportDate",reportDate);
        resutlMap.put("todayNewMember",todayNewMember);
        resutlMap.put("totalMember",totalMember);
        resutlMap.put("thisWeekNewMember",thisWeekNewMember);
        resutlMap.put("thisMonthNewMember",thisMonthNewMember);

        resutlMap.put("todayOrderNumber",todayOrderNumber);
        resutlMap.put("todayVisitsNumber",todayVisitsNumber);
        resutlMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resutlMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resutlMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resutlMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);

        resutlMap.put("hotSetmeal",hotSetmeal);
        return resutlMap;
    }
}
