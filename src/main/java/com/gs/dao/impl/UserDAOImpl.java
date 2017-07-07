package com.gs.dao.impl;

import com.gs.bean.User;
import com.gs.common.bean.Pager;
import com.gs.dao.AbstractBaseDAO;
import com.gs.dao.UserDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Wang Genshen on 2017-07-04.
 */
public class UserDAOImpl extends AbstractBaseDAO implements UserDAO {
    @Override
    public void add(User user) {
        getConnection();
        String sql = "insert into t_user(access_token, openid, wechat_nickname, gender) values(?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getAccessToken());
            preparedStatement.setString(2, user.getOpenId());
            preparedStatement.setString(3, user.getWechatNickname());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
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
        getConnection();
        String sql = "select * from t_user where openid = ?";
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, openId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setAccessToken(resultSet.getString("access_token"));
                user.setAccessToken1(resultSet.getString("access_token1"));
                user.setOpenId(openId);
                user.setWechatNickname(resultSet.getString("wechat_nickname"));
                user.setGender(resultSet.getString("gender"));
                user.setPhone(resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return user;
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
        getConnection();
        String sql = "update t_user set phone = ? where openid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, openid);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }
}
