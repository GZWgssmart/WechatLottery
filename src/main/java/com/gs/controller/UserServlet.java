package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.gs.bean.User;
import com.gs.common.*;
import com.gs.common.bean.ControllerResult;
import com.gs.common.util.DateUtil;
import com.gs.common.util.DecimalUtil;
import com.gs.common.util.PhoneUtil;
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
        } else if (method.equals("choose_count")) {
            showChooseCountPage(request, response);
        }
    }

    private void showChooseCountPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/user/choose_count.jsp").forward(request, response);
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
            user.setHidePhone(PhoneUtil.hidePhone(phone));
            session.setAttribute(Constants.LOGINED_USER, user);
            response.setContentType("text/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(ControllerResult.getSuccessResult("成功绑定手机号")));
        } else {
            response.sendRedirect(request.getContextPath() + "/index");
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
            List<User> payedUsers = (ArrayList<User>) servletContext.getAttribute(Constants.PAYED_USERS);
            if (!payedUsers.contains(user)) {

                int count = Integer.valueOf(request.getParameter("count"));

                Vector<Integer> payMoney = PayMoney.getPayMoney();
                Collections.shuffle(payMoney);
                double[] moneyArray = new double[count];
                int total = 0;
                for (int i = 0; i < count; i++) {
                    int cent = payMoney.remove(i);
                    moneyArray[i] = DecimalUtil.centToYuan(cent);
                    total += cent;
                }

                user.setChooseCount(count);
                user.setPayedFee(total);
                session.setAttribute(Constants.LOGINED_USER, user);
                request.setAttribute("total_fee", total);
                request.setAttribute("total_fee_yuan", DecimalUtil.centToYuan(total));
                request.setAttribute("money_array", moneyArray);
                request.getRequestDispatcher("/WEB-INF/views/user/topay.jsp").forward(request, response);
            } else {
                request.setAttribute("total_fee_yuan", DecimalUtil.centToYuan(user.getPayedFee()));
                request.getRequestDispatcher("/WEB-INF/views/user/payed.jsp").forward(request, response);
                return;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

    private String gameStatus(HttpServletRequest request, HttpServletResponse response) {
        ServletContext servletContext = request.getServletContext();
        String beginTime = (String) servletContext.getAttribute(ConfigConstants.ACTIVITY_BEGIN_TIME);
        boolean gameOver = (Boolean) servletContext.getAttribute(ConfigConstants.GAME_OVER);

        if (gameOver) {
            return GameStatus.GAME_OVER;
        }

        Calendar beginTimeCal = DateUtil.stringToCalendar(beginTime);
        if (beginTimeCal != null && Calendar.getInstance().compareTo(beginTimeCal) < 0) {
            return GameStatus.NOT_START;
        }
        return GameStatus.GAMING;
    }

}
