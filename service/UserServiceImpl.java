package com.jing.blogs.service;

import com.jing.blogs.dao.UserRepository;
import com.jing.blogs.domain.User;
import com.jing.blogs.domain.UserBasic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username,password);
        return user;
    }

    @Override
    public List<UserBasic> listUsersInfo() {
        List<UserBasic> list = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users){
            UserBasic u = new UserBasic();
            u.setName(user.getNickname());
            u.setPicUrl(user.getAvatar());
            list.add(u);
        }
        return list;
    }
}
