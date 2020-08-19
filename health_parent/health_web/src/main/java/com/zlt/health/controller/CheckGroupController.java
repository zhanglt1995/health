package com.zlt.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zlt.health.VO.PageResult;
import com.zlt.health.VO.QueryPageBean;
import com.zlt.health.VO.Result;
import com.zlt.health.constant.MessageConstants;
import com.zlt.health.pojo.CheckGroup;
import com.zlt.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhanglitao
 * @create 2020/8/18 14:25
 * @desc
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);
        return new Result(true, MessageConstants.QUERY_CHECKITEM_SUCCESS,pageResult);
    }

    @RequestMapping("/add")
    public Result add(@RequestParam List<Integer> checkitemIds,@RequestBody CheckGroup checkGroup){
        checkGroupService.addCheckGroup(checkitemIds,checkGroup);
        return new Result(true,MessageConstants.ADD_CHECKGROUP_SUCCESS);
    }

    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable("id") Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true,MessageConstants.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    @RequestMapping("/update")
    public Result update(@RequestParam List<Integer> checkitemIds,@RequestBody CheckGroup checkGroup){
        checkGroupService.update(checkitemIds,checkGroup);
        return new Result(true,MessageConstants.ADD_CHECKGROUP_SUCCESS);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id") Integer id){
        checkGroupService.deleteById(id);
        return new Result(true,MessageConstants.DELETE_CHECKITEM_SUCCESS);
    }

}
