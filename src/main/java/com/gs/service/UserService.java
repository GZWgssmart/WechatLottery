package com.gs.service;

import com.gs.bean.User;

import java.util.List;

/**
 * Created by Wang Genshen on 2017-07-04.
 */
public interface UserService extends BaseService<Integer, User> {
    public List<User> queryAllPrized();
}
