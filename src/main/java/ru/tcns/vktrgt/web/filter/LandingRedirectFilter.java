package ru.tcns.vktrgt.web.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by tignatchenko on 30/12/16.
 */
public class LandingRedirectFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contextPath = ((HttpServletRequest) request).getContextPath();
        String requestURI = httpRequest.getRequestURI();
        requestURI = StringUtils.substringAfter(requestURI, contextPath);
        if (StringUtils.equals("/", requestURI) && httpRequest.getUserPrincipal()==null) {
            requestURI = "/landing.html";
            request.getRequestDispatcher(requestURI).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }


    }

    @Override
    public void destroy() {

    }
}
