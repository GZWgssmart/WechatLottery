package com.gs.service.impl;

import com.gs.bean.User;
import com.gs.common.bean.Pager;
import com.gs.dao.UserDAO;
import com.gs.dao.impl.UserDAOImpl;
import com.gs.service.UserService;

import java.util.List;

/**
 * Created by Wang Genshen on 2017-07-04.
 */
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public void add(User user) {
        userDAO.add(user);
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void valid(Integer integer, String status) {

    }

    @Override
    public User queryById(Integer integer) {
        return null;
    }

    @Override
    public User queryByPhone(String phone) {
        return null;
    }

    @Override
    public User queryByOpenId(String openId) {
        return userDAO.queryByOpenId(openId);
    }

    @Override
    public List<User> queryAll() {
        return null;
    }

    @Override
    public List<User> queryByPager(Pager<User> pager) {
        return null;
    }

    @Override
    public List<User> queryAllPrized() {
        return null;
    }

    public void updatePhone(String openid, String phone) {
        userDAO.updatePhone(openid, phone);
    }
}