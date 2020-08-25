package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.constant.RedisConstants;
import com.zlt.health.pojo.CheckGroup;
import com.zlt.health.pojo.CheckItem;
import com.zlt.health.pojo.Order;
import com.zlt.health.pojo.Setmeal;
import com.zlt.health.service.OrderService;
import com.zlt.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/22 18:05
 * @desc
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map orderInfo){
        // 获取手机号，先从redis中取出验证码
        String telephone = (String) orderInfo.get("telephone");
        String telKey = RedisConstants.SENDTYPE_ORDER+"_"+telephone;

        // 获取一个jedis
        Jedis jedis = jedisPool.getResource();
        // 从redis中获取
        String telCode = jedis.get(telKey);

        if(null == telCode){
            // 失效或没有发送
            return new Result(false, MessageConstants.TELEPHONE_VALIDATECODE_NOTNULL);
        }
        if(!telCode.equals(orderInfo.get("validateCode"))){
            return new Result(false, MessageConstants.VALIDATECODE_ERROR);
        }
        jedis.del(telKey);// 清除验证码，已经使用过了
        // 设置预约类型
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        // 调用服务预约
        Order order = orderService.submitOrder(orderInfo);
        return new Result(true, MessageConstants.ORDER_SUCCESS,order);
    }

    @GetMapping("/findById")
    public Result findById(Integer id){
        Map<String,Object> resultMap = orderService.findById(id);
        return new Result(true,MessageConstants.ORDER_SUCCESS,resultMap);
    }

    @GetMapping("/exportSetmealInfo")
    public void exportSetmealInfo(Integer id, HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        // 使用订单ID，查询订单信息（需要包括套餐ID）
        Map<String,Object> map = orderService.findById(id);
        // 获取套餐ID
        Integer setmealId = (Integer)map.get("setmealId");
        // 使用套餐ID，查询套餐信息
        Setmeal setmeal = setmealService.findDetailById(setmealId);
        // 下载导出
        // 设置头信息
        response.setContentType("application/pdf");
        String filename = "exportPDF.pdf";

        // 设置以附件的形式导出
        response.setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        // 生成PDF文件
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 设置表格字体
        BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",false);
        Font font = new Font(cn, 10, Font.NORMAL, Color.GRAY);

        // 写PDF数据
        // 输出订单和套餐信息
        // 体检人
        document.add(new Paragraph("体检人："+(String)map.get("member"), font));
        // 体检套餐
        document.add(new Paragraph("体检套餐："+(String)map.get("setmeal"), font));
        // 体检日期
        document.add(new Paragraph("体检日期："+map.get("orderDate"), font));
        // 预约类型
        document.add(new Paragraph("预约类型："+(String)map.get("orderType"), font));

        // 向document 生成pdf表格
        Table table = new Table(3);//创建3列的表格

        // 写表头
        table.addCell(buildCell("项目名称", font));
        table.addCell(buildCell("项目内容", font));
        table.addCell(buildCell("项目解读", font));
        // 写数据
        if(setmeal.getCheckGroups() != null){
            for (CheckGroup checkGroup : setmeal.getCheckGroups()) {
                table.addCell(buildCell(checkGroup.getName(), font));
                // 组织检查项集合
                StringBuffer checkItems = new StringBuffer();
                for (CheckItem checkItem : checkGroup.getCheckItems()) {
                    checkItems.append(checkItem.getName()+"  ");
                }
                table.addCell(buildCell(checkItems.toString(), font));
                table.addCell(buildCell(checkGroup.getRemark(), font));
            }
        }
        // 将表格加入文档
        document.add(table);
        document.close();
    }

    // 传递内容和字体样式，生成单元格
    private Cell buildCell(String content, Font font)
            throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
