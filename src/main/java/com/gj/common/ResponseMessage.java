package com.gj.common;

/**
 * Created by hy.wang on 17/6/26.
 */
public class ResponseMessage {

  public String code = "200";

  public String msg = "";

  public Object data = null;

  public ResponseMessage() {
  }

  public ResponseMessage(final String code, final String msg, final Object data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public ResponseMessage(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static ResponseMessage Success(Object data) {
    return new ResponseMessage(ResponseApiUtil.SUCCESS_CODE, ResponseApiUtil.SUCCESS_MSG, data);
  }

  public static ResponseMessage Success() {
    return new ResponseMessage(ResponseApiUtil.SUCCESS_CODE, ResponseApiUtil.SUCCESS_MSG, "ok");
  }

//  public static ResponseMessage Fail(Object data) {
//    return new ResponseMessage(ResponseApiUtil.FAIL_CODE, ResponseApiUtil.FAIL_MSG, data);
//  }

  public static ResponseMessage Fail(String msg) {
      return new ResponseMessage(ResponseApiUtil.UNFOUND_FAIL_CODE,msg);
  }
  public static ResponseMessage Object(String code,String msg,Object object) {
    return new ResponseMessage(code,msg,object);
  }
  public static ResponseMessage Object(String code,String msg) {
    return new ResponseMessage(code,msg);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    return "ResponseMessage{" +
      "code=" + code +
      ", msg='" + msg + '\'' +
      ", data=" + data +
      '}';
  }
}
