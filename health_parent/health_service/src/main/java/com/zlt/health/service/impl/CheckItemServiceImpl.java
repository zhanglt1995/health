package com.zlt.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zlt.health.VO.PageResult;
import com.zlt.health.VO.QueryPageBean;
import com.zlt.health.dao.CheckItemDao;
import com.zlt.health.pojo.CheckItem;
import com.zlt.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/17 16:45
 * @desc
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 先不考虑条件查询

        // 如果是QueryString不为null或者空字符串，加上条件进行模糊查询
        String queryString = null;
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryString = "%" + queryPageBean.getQueryString() + "%";
        }
        Page<CheckItem> page = checkItemDao.findByPage(queryString);
        long total = page.getTotal();
        List<CheckItem> checkItemList = page.getResult();

        return new PageResult(total,checkItemList);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public void deleteById(Integer id) {
        checkItemDao.deleteById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkGroupId) {
        List<Integer> checkItemIds = checkItemDao.findByGroupId(checkGroupId);
        return checkItemIds;
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }
}
