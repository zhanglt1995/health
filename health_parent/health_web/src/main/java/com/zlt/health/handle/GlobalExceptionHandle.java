package com.zlt.health.handle;

import com.zlt.health.VO.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanglitao
 * @create 2020/8/17 17:46
 * @desc 全局异常处理
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(Exception.class)
    public Result processException(Exception ex){
        return new Result(false,"系统出了点问题，请稍后再试!!");
    }
}
