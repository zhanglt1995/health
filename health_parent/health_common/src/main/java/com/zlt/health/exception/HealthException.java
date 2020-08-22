package com.zlt.health.exception;

/**
 * @author zhanglitao
 * @create 2020/8/20 20:18
 * @desc
 */
public class HealthException extends RuntimeException {
    private static final long serialVersionUID = 194906846739586856L;
    private String errorMsg;

    public HealthException() {
        super();
    }

    public HealthException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
