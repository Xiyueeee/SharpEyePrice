package com.example.sharpeyeprice.service;

import com.example.sharpeyeprice.model.User;
import com.example.sharpeyeprice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    @Autowired   //注入userMapper,否则为空
    private UserMapper userMapper;

    //登录服务
    public String login(String account) {
        return userMapper.findPasswordByAccount(account);
    }

    //校验账户是否存在
    public boolean isAccountExist(String account) {
        String password = userMapper.findPasswordByAccount(account);
        return password != null;   //如果返回的密码为null,说明账户不存在
    }

    //校验密码是否正确
    public boolean checkPassword(String account, String password) {
        String storedPassword = userMapper.findPasswordByAccount(account);
        return storedPassword != null && storedPassword.equals(password);   //比较密码
    }

    //注册服务
    public String check_account(String account) {
        return userMapper.findAccountByAccount(account);
    }

    public User find_user_by_account(String account) {
        return userMapper.findUserByAccount(account);
    }

    public String check_email(String email) {
        return userMapper.findEmailByEmail(email);
    }

    public void register(User user) {
        userMapper.insertNewUser(user);
    }

    //重置密码
    public User check_user_by_email(String email) {
        return userMapper.findUserByEmail(email);
    }

    public void update_password_by_account(String account, String password) {
        userMapper.UpdatePasswordByAccount(account, password);
    }
}
