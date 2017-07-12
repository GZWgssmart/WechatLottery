package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gs.bean.User;
import com.gs.common.Constants;
import com.gs.common.WebUtil;
import com.gs.common.WechatAPI;
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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Wang Genshen on 2017-07-07.
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 4377945567005428997L;
    private UserService userService;

    public LoginServlet() {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = WebUtil.getReqMethod(request);
        if (method.equals("login")) {
            login(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 微信授权登录API
        HttpGet httpGet = new HttpGet(WechatAPI.GET_ACCESS_TOKEN_URL.replace("{CODE}", code));
        String accessor = httpclient.execute(httpGet, responseHandler);
        if (accessor != null) {
            JSONObject accessorJSON = JSON.parseObject(accessor);
            String accessToken = accessorJSON.getString("access_token");
            if (accessToken != null) {
                // 如果授权登录成功，则通过access_token和openid去获取用户信息
                HttpSession session = request.getSession();
                String openid = accessorJSON.getString("openid");
                User u = userService.queryByOpenId(openid);
                if (u != null) { // 数据库中已经有用户数据
                    session.setAttribute(Constants.LOGINED_USER, u);
                    if (u.getPhone() != null && !u.getPhone().equals("")) {
                        response.sendRedirect("/user/home");
                    } else {
                        response.sendRedirect("/user/phone");
                    }
                } else { // 数据库没有用户数据，则通过微信接口去获取用户数据后保存到数据库中
                    httpGet = new HttpGet(WechatAPI.GET_USER_INFO.replace("{ACCESS_TOKEN}", accessToken).replace("{OPENID}", openid));
                    String userInfo = httpclient.execute(httpGet, responseHandler);
                    httpclient.close();
                    userInfo = new String(userInfo.getBytes(Constants.ISO_ENCODING), Constants.DEFAULT_ENCODING); // 转码
                    JSONObject userInfoJSON = JSON.parseObject(userInfo);
                    User user = new User();
                    user.setOpenId(openid);
                    user.setWechatNickname(userInfoJSON.getString("nickname"));
                    user.setAccessToken(accessToken);
                    int sex = userInfoJSON.getInteger("sex");
                    if (sex == 1) {
                        user.setGender("男");
                    } else if (sex == 2) {
                        user.setGender("女");
                    } else {
                        user.setGender("无");
                    }
                    user.setUnionId(userInfoJSON.getString("unionid"));
                    userService.add(user);
                    session.setAttribute(Constants.LOGINED_USER, user);
                    response.sendRedirect("/user/phone");
                }
            } else {
                response.sendRedirect("/index");
            }
        } else {
            response.sendRedirect("/index");
        }
    }


    private ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        @Override
        public String handleResponse(final HttpResponse response) throws IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    };

}
