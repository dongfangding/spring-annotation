package com.ddf.annotation.servlet.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author DDf on 2018/8/14
 */
public class CustomFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化。。。。。。。。");
        System.out.println("servlet容器： " + filterConfig.getServletContext());

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("目标方法被拦截.............");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("销毁方法。。。。。。。。。。。");
    }
}
