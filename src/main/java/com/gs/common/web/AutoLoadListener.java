package com.gs.common.web;

import com.gs.common.ConfigConstants;
import com.gs.common.util.SingletonConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
        servletContext.setAttribute(ConfigConstants.ACTIVITY_BEGIN_TIME, config.getString(ConfigConstants.ACTIVITY_BEGIN_TIME));
        servletContext.setAttribute(ConfigConstants.ACTIVITY_MAX_USER, config.getInt(ConfigConstants.ACTIVITY_MAX_USER));
        servletContext.setAttribute(ConfigConstants.PRIZED_COUNT, config.getInt(ConfigConstants.PRIZED_COUNT));
        servletContext.setAttribute(ConfigConstants.PRIZED_USERS, config.getString(ConfigConstants.PRIZED_USERS));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("AutoLoadListener destroyed!");
    }
}
