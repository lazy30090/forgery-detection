package com.juntong.forgerydetection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juntong.forgerydetection.entity.User;
import com.juntong.forgerydetection.mapper.UserMapper;
import com.juntong.forgerydetection.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}