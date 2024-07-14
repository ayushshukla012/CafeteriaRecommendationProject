package com.cafeteriamanagement.service;

import com.cafeteriamanagement.dao.UserDao;
import com.cafeteriamanagement.dto.User;

public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User authenticate(int loginId, String password, String roleName) {
        return userDao.authenticate(loginId, password, roleName);
    }
}