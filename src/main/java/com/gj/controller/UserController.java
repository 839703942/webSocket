package com.gj.controller;

import com.alibaba.fastjson.JSON;
import com.gj.common.MD5Utils;
import com.gj.common.RedisOperator;
import com.gj.common.ResponseMessage;
import com.gj.model.CrUser;
import com.gj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.UUID;

import static com.gj.common.EnumResponse.*;

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redis;
    /**
     * 用户注册
     * @param crUser
     * @return
     */
    @PostMapping(value = "/registered")
    @ResponseBody
    public ResponseMessage registered(CrUser crUser,@RequestParam(name = "age1")String age1){
        if(StringUtils.isEmpty(crUser.getUserName())||StringUtils.isEmpty(crUser.getPassword())||StringUtils.isEmpty(crUser.getConfirmPassword())
        ||StringUtils.isEmpty(crUser.getAge())||StringUtils.isEmpty(crUser.getGender())){
            return ResponseMessage.Fail(REQUEST_DATA_ERROR);
        }
        crUser.setAge(Long.parseLong(age1));
        //确认密码是否正确
        if(!crUser.getPassword().equals(crUser.getConfirmPassword())){
            return ResponseMessage.Fail(REQUEST_PASSWORD_ERROR);
        }
        //确认密码是否小于等于二十位
        if(crUser.getPassword().length()>20){
            return ResponseMessage.Fail(PASSWORD_LENGTH_ERROR2);
        }
        //密码不能小于六位
        if(crUser.getPassword().length()<6){
            return ResponseMessage.Fail(PASSWORD_LENGTH_ERROR3);
        }
        //密码修改成MD5
        crUser.setPassword(MD5Utils.getPwd(crUser.getPassword()));

        //用户名称是否大于20
        if(crUser.getUserName().length()>20){
            return ResponseMessage.Fail(NAME_LENGTH_ERROR);
        }

        //判断是否存在用户
        if(userService.BolUser(crUser.getUserName())){
            return ResponseMessage.Fail(USERNAME_ERROR);
        }

        //存入数据库
        crUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Integer integer = userService.InsUser(crUser);
        log.info(integer.toString());
        return ResponseMessage.Success(SUCCESS);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseMessage login(
            @RequestParam(name = "userName")String userName,
            @RequestParam(name = "password")String password,
            HttpServletResponse response){
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            return ResponseMessage.Fail(PASSWORD_USERNAME_NOTNULL);
        }
        //根据用户名密码查找用户
        CrUser crUser = userService.namePassUser(userName,  MD5Utils.getPwd(password));
        if(crUser==null){
            return ResponseMessage.Fail(LOGIN_ERROR);
        }

        //存入redis
        String uuid = UUID.randomUUID().toString();
        redis.set(USER_REDIS_TOKEN+":"+crUser.getId(),uuid,USER_REDIS_TOKEN_TIME);
        redis.set(uuid, JSON.toJSONString(crUser),USER_REDIS_TOKEN_TIME);
        response.setHeader(USER_REDIS_TOKEN+crUser.getId(),uuid);
        return ResponseMessage.Success(crUser);
    }
}
