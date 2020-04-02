package com.gj.common;

/**
 * Created by wanghy on 17/6/26.
 */
public class ResponseApiUtil {

  public static final String SUCCESS_CODE = "200";//操作对象成功

  public static final int FAIL_CODE = 300;//操作对象失败

  public static final int SERVER_CODE = 400; //系统服务异常

  public static final int FAIL_UNAUTHORIZED_CODE = 401; //无操作权限

  public static final String  UNFOUND_FAIL_CODE = "500";//逻辑异常，找不到对象

  public static final String SUCCESS_MSG = "ok";

  public static final String FAIL_UNAUTHORIZED_MSG = "unauthorized";

  public static final String FAIL_MSG = "fail";

  public static final String UNFOUND_FAIL_MSG = "unfound obj fail";

  public static final String UNFOUND_PARENT_FAIL_MSG = "unfound parent obj fail";

  public static final String UNIQUE_FAIL_MSG = "已存在相同的名称或者服务更新异常";

  public static final String SISSON = "user-redis-sesson";

  //短信
  public static final String SMS_LENGTH_ERROR = "手机号码不正确";
  public static final String SMS_SEND_ERROR = "发送失败，请联系客服";

}
