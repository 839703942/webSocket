package com.gj.mapper;

import com.gj.model.CrUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    //根据用户名判断用户是否存在
    Boolean BolUser(String userName);
    //插入用户
    Integer InsUser(CrUser crUser);
    //根据用户名密码查找用户
    CrUser namePassUser(String userName,String password);
}
