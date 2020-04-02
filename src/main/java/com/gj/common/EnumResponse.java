package com.gj.common;

public class EnumResponse {

    public static final String REQUEST_DATA_ERROR = "请求参数异常";
    public static final String REQUEST_PASSWORD_ERROR = "密码不一致";
    public static final String PASSWORD_LENGTH_ERROR2 = "密码长度不能大于20位";
    public static final String PASSWORD_LENGTH_ERROR3 = "密码长度不能小于6位";
    public static final String NAME_LENGTH_ERROR = "用户名长度不能大于20";
    public static final String USERNAME_ERROR = "用户已存在";
    public static final String SUCCESS = "操作成功";
    public static final String LOGIN_ERROR = "用户名或密码不正确";

    public static final String PASSWORD_USERNAME_NOTNULL = "用户名或者密码不能为空";
    public static final Integer USER_REDIS_TOKEN_TIME = 60*30;
    public static final String USER_REDIS_TOKEN = "user_token";
    public static final String TIME_TYPE = "yyyyMMddHHmmss";

    //小程序
    public static final String APPID = "wx3b5566c4a18e727b";
    public static final String ATTACH = "深圳分店";
    public static final String BODY = "心理医生入驻";
    public static final String MCH_ID = "1482533172";
    public static final String KEY = "c20bd51d92f033c93a6c1ad7c7729d18";

    //HTTP请求
    public static final String HTTP_POST = "POST";


}
