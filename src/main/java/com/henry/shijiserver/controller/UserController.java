package com.henry.shijiserver.controller;

import com.henry.shijiserver.common.ErrorMessage;
import com.henry.shijiserver.common.FatalMessage;
import com.henry.shijiserver.common.SuccessMessage;
import com.henry.shijiserver.common.WarningMessage;
import com.henry.shijiserver.domain.User;
import com.henry.shijiserver.service.impl.UserServiceImpl;
import com.henry.shijiserver.constant.Constants;
import com.henry.shijiserver.util.Tools;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Tag(name = "用户Controller")
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 这段代码是一个SpringBoot7的配置类，它实现了WebMvcConfigurer接口，并重写了其中的addResourceHandlers方法。
     * 这个方法是用来添加静态资源的映射路径的，也就是说，
     * 当访问路径为"/img/avatorImages/**"时，就会映射到Constants.AVATOR_IMAGES_PATH所指定的路径上。
     */
    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/avatorImages/**")
                    .addResourceLocations(Constants.AVATOR_IMAGES_PATH);
        }
    }

    /**
     *  用户注册
     */
    @Operation(summary = "用户注册")
    @Parameters({
            @Parameter(name = "请求", required = true)
    })
    // @ResponseBody注解表示该方法返回的结果直接写入HTTP response body中，一般在异步获取数据时使用，将数据直接输出到页面
    @ResponseBody
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public Object addUser(HttpServletRequest req){
        // trim()可以去除该字符串前面与后面跟着的所有空白
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        String avatar = "/img/avatarImages/user.jpg";

        if(userService.existUser(username)) {
            return new WarningMessage("用户名已注册").getMessage();
        }

        User user = new User();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();

        try{
            myBirth = dateFormat.parse(birth);
        } catch (Exception e){
            e.printStackTrace();
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setSex(Byte.valueOf(sex));
        if("".equals(phone_num)){
            user.setPhoneNum(null);
        }
        else{
            user.setPhoneNum(phone_num);
        }

        user.setPhoneNum(Tools.strProcess(phone_num));
        user.setEmail(Tools.strProcess(email));
        user.setBirth(myBirth);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setAvatar(avatar);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        try {
            boolean res = userService.addUser(user);
            if (res) {
                return new SuccessMessage<ObjectUtils.Null>("注册成功").getMessage();
            } else {
                return new ErrorMessage("注册失败").getMessage();
            }
        } catch (DuplicateKeyException e) {
            return new FatalMessage(e.getMessage()).getMessage();
        }
    }

    /**
     * 登录判断
     */
    @Operation(summary = "登录判断")
    @Parameters({
            @Parameter(name = "请求", required = true),
            @Parameter(name = "session", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "/user/login/status", method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean res = userService.verifyPwd(username, password);
        if (res) {
            session.setAttribute("username", username);
            return new SuccessMessage<List<User>>("登录成功", userService.loginStatus(username)).getMessage();
        } else {
            return new ErrorMessage("用户名或密码错误").getMessage();
        }
    }

    /**
     * 返回所有用户
     */
    @Operation(summary = "返回所有用户")
    @Parameters({
    })
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Object allUser() {
        System.out.println("All user");
        return new SuccessMessage<List<User>>(null, userService.allUser()).getMessage();
    }

    /**
     * 返回指定 ID 的用户
     */
    @Operation(summary = "返回指定 ID 的用户")
    @Parameters({
            @Parameter(name = "id", required = true)
    })
    @RequestMapping(value = "/user/detail", method = RequestMethod.GET)
//    public Object userOfId(HttpServletRequest req) {
//        String id = req.getParameter("id");
//
//        return new SuccessMessage<List<User>>(null, userService.userOfId(Integer.parseInt(id))).getMessage();
//    }
    public Object userOfId(String id) {
        return new SuccessMessage<List<User>>(null, userService.userOfId(Integer.parseInt(id))).getMessage();
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @Parameters({
            @Parameter(name = "请求", required = true)
    })
    @RequestMapping(value = "/user/delete", method = RequestMethod.GET)
    public Object deleteUser(HttpServletRequest req) {
        String id = req.getParameter("id");

        boolean res = userService.deleteUser(Integer.parseInt(id));
        if (res) {
            return new SuccessMessage<ObjectUtils.Null>("删除成功").getMessage();
        } else {
            return new ErrorMessage("删除失败").getMessage();
        }
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息")
    @Parameters({
            @Parameter(name = "请求", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public Object updateUserMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String sex = req.getParameter("sex").trim();
        String phone_num = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        // System.out.println(username);

        User user = new User();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setId(Integer.parseInt(id));
        user.setUsername(username);
        user.setSex(Byte.valueOf(sex));
        user.setPhoneNum(phone_num);
        user.setEmail(email);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setUpdateTime(new Date());
        user.setBirth(myBirth);

        boolean res = userService.updateUserMsg(user);
        if (res) {
            return new SuccessMessage<ObjectUtils.Null>("修改成功").getMessage();
        } else {
            return new ErrorMessage("修改失败").getMessage();
        }
    }

    /**
     * 更新用户密码
     */
    @Operation(summary = "更新用户密码")
    @Parameters({
            @Parameter(name = "请求", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String old_password = req.getParameter("old_password").trim();
        String password = req.getParameter("password").trim();

        boolean res = userService.verifyPwd(username, old_password);
        if (!res) {
            return new ErrorMessage("密码输入错误").getMessage();
        }

        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setPassword(password);

        boolean result = userService.updatePassword(user);
        if (result) {
            return new SuccessMessage<ObjectUtils.Null>("密码修改成功").getMessage();
        } else {
            return new ErrorMessage("密码修改失败").getMessage();
        }
    }

    /**
     * 更新用户头像
     */
    @Operation(summary = "更新用户头像")
    @Parameters({
            @Parameter(name = "文件", required = true),
            @Parameter(name = "id", required = true),
    })
    @ResponseBody
    @RequestMapping(value = "/user/avatar/update", method = RequestMethod.POST)
    public Object updateUserPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id") int id) {
        String fileName = System.currentTimeMillis() + avatarFile.getOriginalFilename();
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "avatarImages";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/avatarImages/" + fileName;
        try {
            avatarFile.transferTo(dest);
            User user = new User();
            user.setId(id);
            user.setAvatar(imgPath);
            boolean res = userService.updateUserAvatar(user);
            if (res) {
                return new SuccessMessage<String>("上传成功", imgPath).getMessage();
            } else {
                return new ErrorMessage("上传失败").getMessage();
            }
        } catch (IOException e) {
            return new FatalMessage("上传失败" + e.getMessage()).getMessage();
        }
    }
}
