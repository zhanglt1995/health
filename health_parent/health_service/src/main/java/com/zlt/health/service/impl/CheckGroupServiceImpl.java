package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlt.health.VO.PageResult;
import com.zlt.health.VO.QueryPageBean;
import com.zlt.health.dao.CheckGroupDao;
import com.zlt.health.pojo.CheckGroup;
import com.zlt.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/18 19:31
 * @desc
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        // 如果是QueryString不为null或者空字符串，加上条件进行模糊查询
        String queryString = null;
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryString = "%" + queryPageBean.getQueryString() + "%";
        }
        Page<CheckGroup> page = checkGroupDao.findByPage(queryString);
        long total = page.getTotal();
        List<CheckGroup> checkGroupList = page.getResult();

        return new PageResult(total,checkGroupList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCheckGroup(List<Integer> checkitemIds, CheckGroup checkGroup) {
        // 第一步将数据插入检查组，并返回id
        checkGroupDao.add(checkGroup);
        // 第二步插入关系表
        if(!CollectionUtils.isEmpty(checkitemIds)){
            checkGroupDao.addCheckGroupAndCheckItem(checkitemIds,checkGroup.getId());
        }
    }

    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(List<Integer> checkitemIds, CheckGroup checkGroup) {
        // 执行更新方法
        checkGroupDao.update(checkGroup);
        // 删除之前的关系
        checkGroupDao.deleteGroupAndItemByGroupId(checkGroup.getId());
        // 将新的关联关系插入
        if(!CollectionUtils.isEmpty(checkitemIds)){
            checkGroupDao.addCheckGroupAndCheckItem(checkitemIds,checkGroup.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        // 先删除关系表，不然后有删除的主键冲突，这里还可以使用级联删除
        checkGroupDao.deleteGroupAndItemByGroupId(id);

        // 删除检查组
        checkGroupDao.deleteById(id);
    }
}
