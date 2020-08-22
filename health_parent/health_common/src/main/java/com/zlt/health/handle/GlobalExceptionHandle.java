package com.zlt.health.handle;


import com.zlt.health.VO.Result;
import com.zlt.health.exception.HealthException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhanglitao
 * @create 2020/8/17 17:46
 * @desc 全局异常处理
 */
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(HealthException.class)
    public Result processHealthException(HealthException ex){
        return new Result(false,ex.getErrorMsg());
    }

    @ExceptionHandler(Exception.class)
    public Result processException(Exception ex){
        if(ex instanceof RuntimeException){
            HealthException exception = (HealthException) ex;
            return new Result(false,exception.getErrorMsg());
        }

        return new Result(false, "服务器出了点问题，请稍后再试!!");
    }
}
