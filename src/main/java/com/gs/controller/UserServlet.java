package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.gs.bean.User;
import com.gs.common.ConfigConstants;
import com.gs.common.Constants;
import com.gs.common.PayStatus;
import com.gs.common.WebUtil;
import com.gs.common.bean.ControllerResult;
import com.gs.common.util.DateUtil;
import com.gs.common.util.DecimalUtil;
import com.gs.service.UserService;
import com.gs.service.impl.UserServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wang Genshen on 2017-06-29.
 */
@WebServlet(name = "UserServlet", urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 8257053251107159204L;
    private UserService userService;

    public UserServlet() {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = WebUtil.getReqMethod(request);
        if(method.equals("home")) {
            home(request, response);
        } else if (method.equals("phone")) {
            phone(request, response);
        } else if (method.equals("update_phone")) {
            updatePhone(request, response);
        } else if (method.equals("topay")) {
            toPay(request, response);
        }
    }

    private void home(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession();
        String beginTime = (String) servletContext.getAttribute(ConfigConstants.ACTIVITY_BEGIN_TIME);
        int maxUser = (Integer) servletContext.getAttribute(ConfigConstants.ACTIVITY_MAX_USER);
        Object actualPayObj = servletContext.getAttribute(Constants.ACTUAL_PAY);
        int actualPay = 0;
        if (actualPayObj != null) {
            actualPay = (Integer) actualPayObj;
        }
        Calendar beginTimeCal = DateUtil.stringToCalendar(beginTime);
        if (beginTimeCal != null && Calendar.getInstance().compareTo(beginTimeCal) >= 0) {
            request.setAttribute("activity_started", true);
        } else {
            request.setAttribute("activity_started", false);
        }
        if (actualPay < maxUser) {
            request.setAttribute("user_limited", false);
        } else {
            request.setAttribute("user_limited", true);
        }
        request.getRequestDispatcher("/WEB-INF/views/user/home.jsp").forward(request, response);
    }

    private void phone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/user/phone.jsp").forward(request, response);
    }

    private void updatePhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Object obj = session.getAttribute(Constants.LOGINED_USER);
        if (obj != null) {
            User user = (User) obj;
            String openId = user.getOpenId();
            String phone = request.getParameter("phone");
            userService.updatePhone(openId, phone);
            user.setPhone(phone);
            session.setAttribute(Constants.LOGINED_USER, user);
            response.setContentType("text/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(ControllerResult.getSuccessResult("成功绑定手机号")));
        } else {
            response.sendRedirect("/index");
        }
    }

    private void toPay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(Constants.LOGINED_USER);
        if (userObj != null) {
            User user = (User) userObj;
            ServletContext servletContext = request.getServletContext();
            Object totalJoinObj = servletContext.getAttribute(Constants.TOTAL_JOIN);
            Object actualPayObj = servletContext.getAttribute(Constants.ACTUAL_PAY);
            Object userMapObj = servletContext.getAttribute(Constants.USER_MAP);
            Object userPayedMapObj = servletContext.getAttribute(Constants.USER_PAYED_MAP);

            synchronized (Object.class) {
                // 用户与支付状态的对应关系
                if (userPayedMapObj == null) {
                    Map<String, String> userPayedMap = new HashMap<String, String>();
                    userPayedMap.put(user.getOpenId(), PayStatus.NOT_PAYED);
                    servletContext.setAttribute(Constants.USER_PAYED_MAP, userPayedMap);
                } else {
                    Map<String, String> userPayedMap = (HashMap<String, String>) userPayedMapObj;
                    Object payedObj = userPayedMap.get(user.getOpenId());
                    if (payedObj != null) {
                        String payStatus = (String) payedObj;
                        if (payStatus.equals(PayStatus.SUCCESS)) {
                            request.getRequestDispatcher("/WEB-INF/views/user/payed.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        userPayedMap.put(user.getOpenId(), PayStatus.NOT_PAYED);
                        servletContext.setAttribute(Constants.USER_PAYED_MAP, userPayedMap);
                    }
                }
                // 参与总人数
                int totalJoin = 1;
                if (totalJoinObj == null) {
                    servletContext.setAttribute(Constants.TOTAL_JOIN, totalJoin);
                } else {
                    totalJoin = (Integer) totalJoinObj;
                    totalJoin += 1;
                    servletContext.setAttribute(Constants.TOTAL_JOIN, totalJoin);
                }
                // 实际支付人数
                if (actualPayObj == null) {
                    int actualPayInt = 0;
                    servletContext.setAttribute(Constants.ACTUAL_PAY, actualPayInt);
                }
                // 支付顺序与用户的对应关系
                if (userMapObj == null) {
                    Map<Integer, User> userMap = new HashMap<Integer, User>();
                    userMap.put(totalJoin, user);
                    servletContext.setAttribute(Constants.USER_MAP, userMap);
                } else {
                    Map<Integer, User> userMap = (HashMap<Integer, User>) userMapObj;
                    userMap.put(totalJoin, user);
                    servletContext.setAttribute(Constants.USER_MAP, userMap);
                }

                user.setPayedFee(totalJoin);
                user.setPayedOrder(totalJoin);
                request.setAttribute("total_fee", totalJoin);
                request.setAttribute("total_fee_yuan", DecimalUtil.centToYuan(totalJoin));
            }
            request.getRequestDispatcher("/WEB-INF/views/user/topay.jsp").forward(request, response);
        } else {
            response.sendRedirect("/index");
        }
    }


}
