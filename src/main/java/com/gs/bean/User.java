package com.gs.bean;

import java.util.Date;

/**
 * Created by Wang Genshen on 2017-06-29.
 */
public class User {

    private int id;
    private String accessToken;
    private String accessToken1;
    private String openId;
    private String unionId;
    private String wechatNickname;
    private String wechatNo;
    private String gender;
    private String phone;
    private double payedFee;
    private Date payedTime;
    private int payedOrder;
    private int prized;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken1() {
        return accessToken1;
    }

    public void setAccessToken1(String accessToken1) {
        this.accessToken1 = accessToken1;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getWechatNickname() {
        return wechatNickname;
    }

    public void setWechatNickname(String wechatNickname) {
        this.wechatNickname = wechatNickname;
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getPayedFee() {
        return payedFee;
    }

    public void setPayedFee(double payedFee) {
        this.payedFee = payedFee;
    }

    public Date getPayedTime() {
        return payedTime;
    }

    public void setPayedTime(Date payedTime) {
        this.payedTime = payedTime;
    }

    public int getPayedOrder() {
        return payedOrder;
    }

    public void setPayedOrder(int payedOrder) {
        this.payedOrder = payedOrder;
    }

    public int getPrized() {
        return prized;
    }

    public void setPrized(int prized) {
        this.prized = prized;
    }
}
