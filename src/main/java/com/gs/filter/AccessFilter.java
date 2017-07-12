package com.gs.filter;

import com.gs.common.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Wang Genshen on 2017-07-07.
 */
@WebFilter(filterName = "AccessFilter", urlPatterns = {"/user/*"})
public class AccessFilter implements Filter {

    public void destroy() {
        System.out.println("access filter destroy...");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            HttpSession session = request.getSession();
            Object obj = session.getAttribute(Constants.LOGINED_USER);
            if (obj == null) {
                response.sendRedirect("/index");
            } else {
                chain.doFilter(req, resp);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {
        System.out.println("init access filter...");
    }
}
