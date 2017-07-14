package com.gs.common;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wang Genshen on 2017-06-29.
 */
public class WebUtil {

    public static String getReqMethod(HttpServletRequest req) {
        String uri = req.getRequestURI();
        return req.getRequestURI().substring(uri.lastIndexOf("/") + 1);
    }

    public static String getRootPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }

}
