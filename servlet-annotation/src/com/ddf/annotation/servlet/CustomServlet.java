package com.ddf.annotation.servlet;

import com.ddf.annotation.servlet.configuration.CustomsContainerInit;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DDf on 2018/8/14
 * 使用编码方式映射servlet {@link CustomsContainerInit}
 */
public class CustomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("使用编码方式映射Servlet..........");
    }
}
