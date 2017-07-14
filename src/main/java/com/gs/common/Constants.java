package com.gs.common;

/**
 * Created by Wang Genshen on 2017-06-29.
 */
public final class Constants {

    public static final String ISO_ENCODING = "ISO8859-1";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String LOGINED_USER = "user";
    public static final String LOGINED_ADMIN = "admin";

    public static final String LOGO_IMG = "logo_img";

    /**
     * 共多少人已经参与
     */
    public static final String TOTAL_JOIN = "total_join";

    /**
     * 实际已经有多少人支付
     */
    public static final String ACTUAL_PAY = "actual_pay";

    /**
     * 支付顺序与用户的对应关系
     */
    public static final String USER_MAP = "user_map";

    /**
     * 用户openid与支付状态的对应关系
     */
    public static final String USER_PAYED_MAP = "user_payed_map";

    /**
     * 用于记录哪些顺序未成功支付
     */
    public static final String UNPAYED_ORDER = "unpayed_order";

}
