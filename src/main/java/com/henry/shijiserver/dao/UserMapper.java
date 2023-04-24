package com.henry.shijiserver.dao;

import com.henry.shijiserver.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int verifyPassword(String username, String password);

    int existUsername(String username);

    int addUser(User user);

    int updateUserMsg(User record);

    int updateUserAvatar(User record);

    int updatePassword(User record);

    int deleteUser(Integer id);

    List<User> allUser();

    List<User> userOfId(Integer id);

    List<User> loginStatus(String username);
}
