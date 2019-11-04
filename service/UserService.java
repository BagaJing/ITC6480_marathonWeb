package com.jing.blogs.service;

import com.jing.blogs.domain.User;
import com.jing.blogs.domain.UserBasic;

import java.util.List;

public interface UserService {

    User checkUser(String username, String password);

    List<UserBasic> listUsersInfo();
}
