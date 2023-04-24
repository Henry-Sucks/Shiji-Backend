package com.henry.shijiserver.service;

import com.henry.shijiserver.domain.User;

import java.util.List;

public interface UserService {

    boolean addUser(User user);

    boolean updateUserMsg(User user);

    boolean updateUserAvatar(User user);

    boolean updatePassword(User user);

    boolean existUser(String username);

    boolean verifyPwd(String username, String password);

    boolean deleteUser(Integer id);

    List<User> allUser();

    List<User> userOfId(Integer id);

    List<User> loginStatus(String username);
}
