package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.gs.bean.User;
import com.gs.common.*;
import com.gs.common.bean.ControllerResult;
import com.gs.common.util.PhoneUtil;
import com.gs.common.wechat.WechatAPI;
import com.gs.common.wechat.WechatUtil;
import com.gs.service.UserService;
import com.gs.service.impl.UserServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Wang Genshen on 2017-07-05.
 */
@WebServlet(name = "PayServlet", urlPatterns = "/pay/*")
public class PayServlet extends HttpServlet {
    private static final long serialVersionUID = -7542686547304843900L;

    private UserService userService;

    public PayServlet() {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = WebUtil.getReqMethod(req);
        if (method.equals("pay")) {
            pay(req, resp);
        } else if (method.equals("result")) {
            result(req, resp);
        } else if (method.equals("pay_result")) {
            // 由项目传递回来的支付结果
            payResult(req, resp);
        } else if (method.equals("all_payed")) {
            allPayed(req, resp);
        } else if (method.equals("lottery")) {
            lottery(req, resp);
        } else if (method.equals("confirm")) {
            confirm(req, resp);
        } else if (method.equals("prized_users")) {
            prizedUsers(req, resp);
        }
    }

    private void prizedUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.queryAllPrized();
        req.setAttribute("prized_users", users);
        req.getRequestDispatcher("/WEB-INF/views/user/prized_users.jsp").forward(req, resp);
    }

    private void confirm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext servletContext = req.getServletContext();
        Map<Integer, User> userMap = (HashMap<Integer, User>) servletContext.getAttribute(Constants.USER_MAP);
        Set<Map.Entry<Integer, User>> entrySet = userMap.entrySet();
        Iterator<Map.Entry<Integer, User>> entryIterator = entrySet.iterator();
        List<User> users = new ArrayList<User>();
        int i = 0;
        while (entryIterator.hasNext()) {
            Map.Entry<Integer, User> entry = entryIterator.next();
            users.add(entry.getValue());
        }
        userService.batchUpdate(users);
        resp.sendRedirect("/pay/prized_users");
    }

    private void lottery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        int prizedCount = (Integer) servletContext.getAttribute(ConfigConstants.PRIZED_COUNT);
        int actualPay = (Integer) servletContext.getAttribute(Constants.ACTUAL_PAY);
        Map<Integer, User> userMap = (HashMap<Integer, User>) servletContext.getAttribute(Constants.USER_MAP);
        String prizedUsers = (String) servletContext.getAttribute(ConfigConstants.PRIZED_USERS);
        List<User> prizedUser = new ArrayList<User>();
        Set<Integer> keySet = userMap.keySet();
        Iterator<Integer> keySetIte = keySet.iterator();
        List<Integer> orders = new ArrayList<Integer>();
        if (prizedCount > actualPay) {
            prizedCount = actualPay;
        }
        while (keySetIte.hasNext()) {
            int order = keySetIte.next();
            User u = userMap.get(order);
            if (u.getTranId() != null && !u.getTranId().equals("")) {
                orders.add(order);
            }
        }
        Collections.shuffle(orders);
        String[] prizedUserArray = null;
        if (prizedUsers != null && !prizedUsers.trim().equals("")) {
            prizedUserArray = prizedUsers.split(",");
            prizedCount = prizedCount - prizedUserArray.length;
            for (int i = 0, len = prizedUserArray.length; i < len; i++) {
                User u = new User();
                String[] nameAndPhone = prizedUserArray[i].split(":");
                u.setWechatNickname(nameAndPhone[0]);
                u.setPhone(nameAndPhone[1]);
                u.setHidePhone(PhoneUtil.hidePhone(nameAndPhone[1]));
                prizedUser.add(u);
            }
        }
        List<Integer> prized = orders.subList(0, prizedCount);
        for (int i = 0, size = prized.size(); i < size; i++) {
            User user = userMap.get(prized.get(i));
            user.setPrized(1);
            prizedUser.add(user);
        }
        servletContext.setAttribute(Constants.USER_MAP, userMap);
        req.setAttribute("prized_users", prizedUser);
        req.setAttribute("lottery", "y");
        req.getRequestDispatcher("/WEB-INF/views/user/prized_users.jsp").forward(req, resp);
    }

    private void allPayed(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/admin/all_payed.jsp").forward(req, resp);
    }

    private void pay(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String orderStr = req.getParameter("order");
        int order = -1;
        if (orderStr != null) {
            order = Integer.valueOf(orderStr);
        }
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(Constants.LOGINED_USER);
        WechatUtil wechatUtil = new WechatUtil();
        Map<String, String> prepayResult = wechatUtil.prepayResult(user.getOpenId(), req.getRemoteAddr(), "抽奖付款", order);
        // 正式付款需要提交的数据
        Map<String, String> payData = wechatUtil.payData(prepayResult);
        req.setAttribute("appId", WechatAPI.APP_ID);
        req.setAttribute("timeStamp", payData.get("timeStamp"));
        req.setAttribute("nonceStr", payData.get("nonceStr"));
        req.setAttribute("packages", payData.get("package"));
        req.setAttribute("paySign", payData.get("paySign"));
        req.getRequestDispatcher("/WEB-INF/views/user/pay.jsp").forward(req, resp); // 预支付数据转发到页面，调用js支付
    }

    private void result(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("***********************notify_url*************************");
        WechatUtil wechatUtil = new WechatUtil();
        Map<String, String> resultMap = wechatUtil.payResult(req);
        try {
            String resultCode = resultMap.get("result_code");
            if (resultCode != null && resultCode.equals("SUCCESS")) {
                String totalFee = resultMap.get("total_fee"); // 支付金额
                Integer payOrder = Integer.valueOf(totalFee); // 支付顺序

                ServletContext servletContext = req.getServletContext();
                // 实际支付数
                Integer actualPay = (Integer) servletContext.getAttribute(Constants.ACTUAL_PAY);
                // 支付顺序与用户的对应关系
                Map<Integer, User> userMap = (HashMap<Integer, User>) servletContext.getAttribute(Constants.USER_MAP);
                // 用户openid与支付状态的对应关系
                Map<String, String> userPayedMap = (HashMap<String, String>) servletContext.getAttribute(Constants.USER_PAYED_MAP);
                List<Integer> unpayedOrder = (ArrayList<Integer>) servletContext.getAttribute(Constants.UNPAYED_ORDER);

                servletContext.setAttribute(Constants.ACTUAL_PAY, actualPay + 1); // 支付成功，则actual_pay + 1
                User user = userMap.get(payOrder);
                user.setPayedTime(Calendar.getInstance().getTime());
                user.setPayedOrder(payOrder);
                user.setPayedFee(payOrder);
                String openId = resultMap.get("openid");
                String tranId = resultMap.get("transaction_id");
                String outTradeNo = resultMap.get("out_trade_no");
                user.setTradeNo(outTradeNo);
                user.setTranId(tranId);
                user.setHidePhone(PhoneUtil.hidePhone(user.getPhone()));
                servletContext.setAttribute(Constants.USER_MAP, userMap);
                synchronized (Object.class) {
                    userPayedMap.put(openId, PayStatus.SUCCESS);
                    unpayedOrder.remove(payOrder);
                }
                servletContext.setAttribute(Constants.USER_PAYED_MAP, userPayedMap);
                servletContext.setAttribute(Constants.UNPAYED_ORDER, unpayedOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        wechatUtil.responsePayNotify(resp);
    }

    private void payResult(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("pay_result.....");
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute(Constants.LOGINED_USER);
        if (userObj != null) {
            User user = (User) userObj;
            ServletContext servletContext = req.getServletContext();
            Map<Integer, User> userMap = (HashMap<Integer, User>) servletContext.getAttribute(Constants.USER_MAP);
            List<Integer> unpayedOrder = (ArrayList<Integer>) servletContext.getAttribute(Constants.UNPAYED_ORDER);
            if (userMap.size() > 0) {
                String openId = user.getOpenId();
                double payedFee = user.getPayedFee();
                int payedOrder = user.getPayedOrder();
                String payResult = req.getParameter("pay_result");
                if (payResult != null && payResult.equals("cancel")) {
                    // 取消支付
                    synchronized (Object.class) {
                        userMap.remove(payedOrder);
                        unpayedOrder.add(payedOrder);
                    }
                } else if (payResult != null && payResult.equals("fail")) {
                    // 支付失败
                    synchronized (Object.class) {
                        userMap.remove(payedOrder);
                        unpayedOrder.add(payedOrder);
                    }
                }
                servletContext.setAttribute(Constants.USER_MAP, userMap);
                resp.setContentType("text/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                out.println(JSON.toJSONString(ControllerResult.getSuccessResult("跳转到用户首页")));
            }
        }
    }

}
