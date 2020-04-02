package com.gj.model;

import lombok.Data;

@Data
public class CrUser {

  private long id;
  private String userName;
  private String password;
  private String confirmPassword;
  private long age;
  private String gender;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;
}
