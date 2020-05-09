package com.example.happybaby.config;

import com.example.happybaby.exception.BaseHttpStatus;
import com.example.happybaby.exception.MyException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: oa
 * @Package: com.example.happybaby.config
 * @ClassName: LoginInterceptor
 * @Description: java类作用描述
 * @Author:
 * @CreateDate: 2020\4\1 0001 13:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2020\4\1 0001 13:45
 * @UpdateRemark: 更新说明
 * @Version:
 */
@Component
@Order(Integer.MIN_VALUE)
public class HeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods",
                "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin,Accept,Content-Type, x-requested-with, x-ijt,X-Custom-Header, Authorization,token");
        // 传递业务请求处理
        if (request.getMethod().equals("OPTIONS"))
            //跨域资源共享标准新增了一组 HTTP 首部字段，允许服务器声明哪些源站有权限访问哪些资源。
            // 另外，规范要求，对那些可能对服务器数据产生副作用的 HTTP 请求方法（特别是 GET 以外的 HTTP 请求，或者搭配某些 MIME 类型的 POST 请求），
            // 浏览器必须首先使用 OPTIONS 方法发起一个预检请求（preflight request），
            // 从而获知服务端是否允许该跨域请求。服务器确认允许之后，才发起实际的 HTTP 请求。
            // 在预检请求的返回中，服务器端也可以通知客户端，是否需要携带身份凭证（包括 Cookies 和 HTTP 认证相关数据）。
            // 参考：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS
            response.setStatus(HttpServletResponse.SC_OK);
        else
            chain.doFilter(request, response);
    }
}
