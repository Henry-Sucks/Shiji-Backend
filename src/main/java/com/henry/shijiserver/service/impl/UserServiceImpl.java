package com.henry.shijiserver.service.impl;

import com.henry.shijiserver.dao.UserMapper;
import com.henry.shijiserver.domain.User;
import com.henry.shijiserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean addUser(User user) {
        return userMapper.insertSelective(user) > 0;
    }

    @Override
    public boolean updateUserMsg(User user) {
        return userMapper.updateUserMsg(user) > 0;
    }

    @Override
    public boolean updatePassword(User user) {
        return userMapper.updatePassword(user) > 0;
    }

    @Override
    public boolean updateUserAvatar(User user) {
        return userMapper.updateUserAvatar(user) > 0;
    }

    @Override
    public boolean existUser(String username) {
        return userMapper.existUsername(username) > 0;
    }

    @Override
    public boolean verifyPwd(String username, String password) {
        return userMapper.verifyPassword(username, password) > 0;
    }

    @Override
    public boolean deleteUser(Integer id) {
        return userMapper.deleteUser(id) > 0;
    }

    @Override
    public List<User> allUser() {
        return userMapper.allUser();
    }

    @Override
    public List<User> userOfId(Integer id) {
        return userMapper.userOfId(id);
    }

    @Override
    public List<User> loginStatus(String username) {
        return userMapper.loginStatus(username);
    }
}
