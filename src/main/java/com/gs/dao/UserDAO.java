package com.gs.dao;

import com.gs.bean.User;

import java.util.List;

/**
 * Created by Wang Genshen on 2017-07-04.
 */
public interface UserDAO extends BaseDAO<Integer, User> {

    public List<User> queryAllPrized();

    public void updatePhone(String openid, String phone);

}
