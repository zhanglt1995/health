package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.service.MemberService;
import com.zlt.health.service.ReportService;
import com.zlt.health.service.SetmealService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.*;

/**
 * @author zhanglitao
 * @create 2020/8/23 21:17
 * @desc
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        // 要将过去的12个月的月份整理出来，格式是 年-月例如 2020-08
        LocalDate localDate = LocalDate.now();

        List<String> monthList = new ArrayList<>();
        for (Long i = 11L; i >=0 ; i--) {
            monthList.add(localDate.minusMonths(i).toString().substring(0, 7));
        }

        List<Integer> memberCountList = memberService.findMemberCountListByMonth(monthList);

        Map<String,Object> resultMap = new HashMap();
        resultMap.put("months",monthList);
        resultMap.put("memberCount",memberCountList);
        return new Result(true, MessageConstants.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }

    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        // 获取每个套餐的预约会员数量
        List<Map<String,Object>> setmealNameAndCountList = setmealService.findSetmealNameAndCountList();

        List<String> setmealNameList = null;
        // 从获取的结果中去取套餐名字的集合
        if (!CollectionUtils.isEmpty(setmealNameAndCountList)) {
            setmealNameList = new ArrayList<>();
            for (Map<String, Object> map : setmealNameAndCountList) {
                String setmealName = (String) map.get("name");
                setmealNameList.add(setmealName);
            }
        }

        Map<String,Object> resultMap = new HashMap<>(4);
        resultMap.put("setmealNames",setmealNameList);
        resultMap.put("setmealCount",setmealNameAndCountList);
        return new Result(true,MessageConstants.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String, Object> resultMap = reportService.getBusinessReportData();
        return new Result(true,MessageConstants.GET_BUSINESS_REPORT_SUCCESS,resultMap);
    }

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String template = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        // 创建工作簿(模板路径)
        try (// 写在try()里的对象，必须实现closable接口，try()cathc()中的finally
             OutputStream os = response.getOutputStream();
             XSSFWorkbook wk = new XSSFWorkbook(template);){
            // 获取工作表
            XSSFSheet sht = wk.getSheetAt(0);
            // 获取运营统计数据
            Map<String, Object> reportData = reportService.getBusinessReportData();
            // 日期 坐标 2,5
            sht.getRow(2).getCell(5).setCellValue(reportData.get("reportDate").toString());

            // 新增会员数 4,5
            sht.getRow(4).getCell(5).setCellValue((Integer)reportData.get("todayNewMember"));
            // 总会员数 4,7
            sht.getRow(4).getCell(7).setCellValue((Integer)reportData.get("totalMember"));
            // 本周新增会员数5,5
            sht.getRow(5).getCell(5).setCellValue((Integer)reportData.get("thisWeekNewMember"));
            // 本月新增会员数 5,7
            sht.getRow(5).getCell(7).setCellValue((Integer)reportData.get("thisMonthNewMember"));

            // 今天预约的数量
            sht.getRow(7).getCell(5).setCellValue((Integer)reportData.get("todayOrderNumber"));
            // 今天到诊数量
            sht.getRow(7).getCell(7).setCellValue((Integer)reportData.get("todayVisitsNumber"));
            // 本周预约数量
            sht.getRow(8).getCell(5).setCellValue((Integer)reportData.get("thisWeekOrderNumber"));
            // 本周到诊数量
            sht.getRow(8).getCell(7).setCellValue((Integer)reportData.get("thisWeekVisitsNumber"));
            // 本月预约数量
            sht.getRow(9).getCell(5).setCellValue((Integer)reportData.get("thisMonthOrderNumber"));
            // 本月到诊数量
            sht.getRow(9).getCell(7).setCellValue((Integer)reportData.get("thisMonthVisitsNumber"));

            // 热门套餐
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>> )reportData.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> setmealMap : hotSetmeal) {
                sht.getRow(row).getCell(4).setCellValue((String)setmealMap.get("name"));
                sht.getRow(row).getCell(5).setCellValue((Long)setmealMap.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) setmealMap.get("proportion");
                sht.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                sht.getRow(row).getCell(7).setCellValue((String)setmealMap.get("remark"));
                row++;
            }

            // 工作簿写给reponse输出流
            response.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            // 解决下载的文件名 中文乱码
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            response.setHeader("Content-Disposition","attachement;filename=" + filename);
            wk.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/exportBusinessReportPDF")
    public void exportBusinessReportPDF(HttpServletRequest request, HttpServletResponse response){
        try {
            // 指定模板地址
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template/health_business.jrxml");
            String jasperPath = request.getSession().getServletContext().getRealPath("template/health_business.jasper");

            // 编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

            // 获取数据
            Map<String, Object> reportData = reportService.getBusinessReportData();

            // 获取集合中的数据
            List<Map<String,Object>> list = (List<Map<String, Object>>) reportData.get("hotSetmeal");

            // 填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, reportData, new JRBeanCollectionDataSource(list));

            // 设置输出头
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            // 输出
            JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
