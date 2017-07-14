package com.gs.common.web;

import com.gs.bean.User;
import com.gs.common.ConfigConstants;
import com.gs.common.Constants;
import com.gs.common.util.DateUtil;
import com.gs.common.util.SingletonConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by WangGenshen on 5/16/16.
 */
public class AutoLoadListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("AutoLoadListener init!");
        ServletContext servletContext = servletContextEvent.getServletContext();
        String autoloadConfig = servletContext.getInitParameter("autoloadConfigLocation");
        SingletonConfig config = SingletonConfig.getInstance();
        config.build(autoloadConfig);
        servletContext.setAttribute(ConfigConstants.PRODUCTION_ENV, config.getString(ConfigConstants.PRODUCTION_ENV));
        servletContext.setAttribute(ConfigConstants.ACTIVITY_BEGIN_TIME, DateUtil.dateToString(Calendar.getInstance().getTime()));
        servletContext.setAttribute(ConfigConstants.ACTIVITY_MAX_USER, 10000);
        servletContext.setAttribute(ConfigConstants.PRIZED_COUNT, 10);
        servletContext.setAttribute(ConfigConstants.PRIZED_USERS, "");
        servletContext.setAttribute(ConfigConstants.GAME_OVER, false);
        servletContext.setAttribute(ConfigConstants.ADMIN_PWD, "admin654321");

        servletContext.setAttribute(Constants.TOTAL_JOIN, 0);
        servletContext.setAttribute(Constants.ACTUAL_PAY, 0);
        servletContext.setAttribute(Constants.USER_MAP, new HashMap<Integer, User>());
        servletContext.setAttribute(Constants.USER_PAYED_MAP, new HashMap<String, String>());
        servletContext.setAttribute(Constants.UNPAYED_ORDER, new ArrayList<Integer>());
        servletContext.setAttribute(Constants.LOGO_IMG, "images/sing.jpg");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("AutoLoadListener destroyed!");
    }
}
