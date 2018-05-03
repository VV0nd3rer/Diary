package com.kverchi.diary.metric;

import com.kverchi.diary.service.UserActivityLogService;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Liudmyla Melnychuk on 3.5.2018.
 */
public class MetricFilter implements Filter {
    private final static Logger logger = Logger.getLogger(MetricFilter.class);
    private UserActivityLogService userActivityLogService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userActivityLogService = (UserActivityLogService) WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext())
                .getBean("userActivityLogService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        userActivityLogService.saveUserInfoFromHttpRequest();
    }

    @Override
    public void destroy() {

    }
}
