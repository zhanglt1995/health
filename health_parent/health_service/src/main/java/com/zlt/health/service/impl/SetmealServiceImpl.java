package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlt.health.VO.PageResult;
import com.zlt.health.VO.QueryPageBean;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.dao.SetmealDao;
import com.zlt.health.exception.HealthException;
import com.zlt.health.pojo.Setmeal;
import com.zlt.health.service.SetmealService;
import com.zlt.health.util.QiNiuUtils;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglitao
 * @create 2020/8/19 23:11
 * @desc
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 添加套餐信息
        setmealDao.add(setmeal);
        // 获取套餐的id
        Integer setmealId = setmeal.getId();
        // 添加套餐与检查组的关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 如果查询条件不为空，则拼接上%
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 插件条件分页查询
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 先更新套餐信息
        setmealDao.update(setmeal);
        // 删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 添加新关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
        // 修改套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    @Override
    public void deleteById(int id) {
        // 统计使用了这个套餐的订单个数，判断套餐是否被使用了
        int count = setmealDao.findOrderCountBySetmealId(id);
        if(count > 0){
            // 被使用了
            throw new HealthException(MessageConstants.SETMEAL_IN_USE);
        }
        // 没有使用
        // 先删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 删除套餐数据
        setmealDao.deleteById(id);

        //删除套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealNameAndCountList() {
        return setmealDao.findSetmealNameAndCountList();
    }

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String out_put_path;

    private void generateMobileStaticHtml(){
        try {
            // 套餐列表静态页面
            generateSetmealListHtml();
            // 套餐详情静态页面 生成单就行了，为了测试方便，生成所有的
            generateSetmealDetailHtml();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generateSetmealDetailHtml() throws Exception {
        // 把所有套餐都生成详情页面 方便测试
        List<Setmeal> setmealList = setmealDao.findAll();
        // setmealList中的套餐是没有详情信息，即没有检查组也没有检查项的信息，要查询一遍
        for (Setmeal setmeal : setmealList) {
            // 获取套餐详情
            Setmeal setmealDetail = setmealDao.findDetailById(setmeal.getId());
            // 设置套餐的图片路径
            setmealDetail.setImg(QiNiuUtils.DOMAIN + setmealDetail.getImg());
            // 生成详情页面
            generateDetailHtml(setmealDetail);
        }
    }
    private void generateDetailHtml(Setmeal setmealDetail) throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal_detail.ftl");
        Map<String, Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmeal", setmealDetail);
        File setmealDetailFile = new File(out_put_path, "setmeal_" + setmealDetail.getId() + ".html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealDetailFile),"utf-8")));
    }
    private void generateSetmealListHtml() throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal.ftl");
        // 获取数据模型
        List<Setmeal> setmealList = setmealDao.findAll();
        // 图片地址
        setmealList.forEach(s->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        Map<String, Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmealList", setmealList);
        // 给模板填充数据 new OutputStreamWriter要指定编码格式，否则中文乱码
        // 生成的文件 c:/sz89/health_parent/health_mobile/src/main/webapp/pages/m_setmeal.html
        File setmealListFile = new File(out_put_path, "m_setmeal.html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealListFile),"utf-8")));
    }
}
