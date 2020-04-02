package com.gj.service.impl;

import com.gj.common.RedisOperator;
import com.gj.mapper.UserMapper;
import com.gj.model.CrUser;
import com.gj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean BolUser(String userName) {
        return userMapper.BolUser(userName);

    }

    @Override
    @Transactional
    public Integer InsUser(CrUser crUser) {
        return userMapper.InsUser(crUser);
    }

    @Override
    public CrUser namePassUser(String userName, String password) {
        return userMapper.namePassUser(userName,password);
    }
}
