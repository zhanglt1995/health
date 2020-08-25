package com.zlt.health.service;

import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/24 11:19
 * @desc
 */
public interface ReportService {
    /**
     * 获取报表的结果
     * @return
     */
    Map<String, Object> getBusinessReportData();
}
