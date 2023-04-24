package com.henry.shijiserver.dao;

import com.henry.shijiserver.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper {

    @Select("select * from user")
    public List<User> allUser();
}
