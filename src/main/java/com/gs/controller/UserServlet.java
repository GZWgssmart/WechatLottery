package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gs.bean.User;
import com.gs.common.ConfigConstants;
import com.gs.common.Constants;
import com.gs.common.WebUtil;
import com.gs.common.WechatAPI;
import com.gs.common.bean.ControllerResult;
import com.gs.common.util.DateUtil;
import com.gs.service.UserService;
import com.gs.service.impl.UserServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
        Calendar beginTimeCal = DateUtil.stringToCalendar(beginTime);
        if (beginTimeCal != null && Calendar.getInstance().compareTo(beginTimeCal) >= 0) {
            request.setAttribute("activity_started", true);
        } else {
            request.setAttribute("activity_started", false);
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
        }
    }

    private void toPay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/user/topay.jsp").forward(request, response);
    }


}
