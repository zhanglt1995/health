package com.zlt.health.handle;


import com.zlt.health.VO.Result;
import com.zlt.health.exception.HealthException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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

        return new Result(false, "服务器出了点问题，请稍后再试!!");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e){
        return new Result(false, "权限不足");
    }

}
