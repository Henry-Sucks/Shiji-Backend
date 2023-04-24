package com.henry.shijiserver.controller;

import com.henry.shijiserver.common.SuccessMessage;
import com.henry.shijiserver.dao.TestMapper;
import com.henry.shijiserver.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class TestController {
    @Autowired
    private TestMapper testMapper;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Object allUser() {
        System.out.println("All user");
        return new SuccessMessage<List<User>>(null, testMapper.allUser()).getMessage();
    }
}
