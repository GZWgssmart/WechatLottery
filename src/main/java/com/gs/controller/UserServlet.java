package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.gs.bean.User;
import com.gs.common.*;
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
import java.util.*;

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
        request.setAttribute("game_status", gameStatus(request, response));
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
        String gameStatus = gameStatus(request, response);
        if (gameStatus.equals(GameStatus.NOT_START) || gameStatus.equals(GameStatus.GAME_OVER)) {
            response.sendRedirect("home");
            return;
        }
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(Constants.LOGINED_USER);
        if (userObj != null) {
            User user = (User) userObj;
            ServletContext servletContext = request.getServletContext();
            int totalJoin = (Integer) servletContext.getAttribute(Constants.TOTAL_JOIN);
            Map<Integer, User> userMap = (HashMap<Integer, User>) servletContext.getAttribute(Constants.USER_MAP);
            Map<String, String> userPayedMap = (HashMap<String, String>) servletContext.getAttribute(Constants.USER_PAYED_MAP);
            List<Integer> unpayedOrder = (ArrayList<Integer>) servletContext.getAttribute(Constants.UNPAYED_ORDER);

            int order = 0;
            synchronized (Object.class) {
                Object payedObj = userPayedMap.get(user.getOpenId());
                if (payedObj != null) {
                    String payStatus = (String) payedObj;
                    if (payStatus.equals(PayStatus.SUCCESS)) {
                        request.setAttribute("total_fee_yuan", DecimalUtil.centToYuan(user.getPayedOrder()));
                        request.getRequestDispatcher("/WEB-INF/views/user/payed.jsp").forward(request, response);
                        return;
                    }
                }
                if (unpayedOrder.size() <= 0) {
                    order = totalJoin + 1;
                    servletContext.setAttribute(Constants.TOTAL_JOIN, order);
                    userMap.put(order, user);
                    servletContext.setAttribute(Constants.USER_MAP, userMap);

                } else {
                    // 有未支付的顺序，则应该先把未支付的顺序使用完
                    Collections.sort(unpayedOrder);
                    order = unpayedOrder.get(0);
                    userMap.put(order, user);
                    servletContext.setAttribute(Constants.USER_MAP, userMap);
                }
            }
            user.setPayedFee(order);
            user.setPayedOrder(order);
            request.setAttribute("total_fee", order);
            request.setAttribute("total_fee_yuan", DecimalUtil.centToYuan(order));
            request.getRequestDispatcher("/WEB-INF/views/user/topay.jsp").forward(request, response);
        } else {
            response.sendRedirect("/index");
        }
    }

    private String gameStatus(HttpServletRequest request, HttpServletResponse response) {
        ServletContext servletContext = request.getServletContext();
        String beginTime = (String) servletContext.getAttribute(ConfigConstants.ACTIVITY_BEGIN_TIME);
        int maxUser = (Integer) servletContext.getAttribute(ConfigConstants.ACTIVITY_MAX_USER);
        int totalJoin = (Integer) servletContext.getAttribute(Constants.TOTAL_JOIN);
        List<Integer> unpayedOrder = (ArrayList<Integer>) servletContext.getAttribute(Constants.UNPAYED_ORDER);

        boolean gameStarted = false;
        boolean userLimited = false;
        boolean hasOrder = false;

        Calendar beginTimeCal = DateUtil.stringToCalendar(beginTime);
        if (beginTimeCal != null && Calendar.getInstance().compareTo(beginTimeCal) >= 0) {
            gameStarted = true;
        }
        if ((totalJoin >= maxUser)) {
            userLimited = true;
        }
        if (unpayedOrder.size() > 0) {
            hasOrder = true;
        }
        if (!gameStarted) {
            return GameStatus.NOT_START;
        } else if (!userLimited) {
            return GameStatus.GAMING;
        } else if (hasOrder) {
            return GameStatus.GAMING;
        } else {
            return GameStatus.GAME_OVER;
        }
    }

}
