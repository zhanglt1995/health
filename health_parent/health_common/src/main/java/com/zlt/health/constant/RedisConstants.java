package com.zlt.health.constant;

/**
 * @author zhanglitao
 * @create 2020/8/19 23:21
 * @desc
 */
public interface RedisConstants {
    //套餐图片所有图片名称
    static final String SETMEAL_PIC_RESOURCES = "setmealPicResources";
    //套餐图片保存在数据库中的图片名称
    static final String SETMEAL_PIC_DB_RESOURCES = "setmealPicDbResources";

    static final String SENDTYPE_ORDER = "001";//用于缓存体检预约时发送的验证码
    static final String SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
    static final String SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码
}
