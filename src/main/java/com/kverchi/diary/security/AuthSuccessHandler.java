package com.kverchi.diary.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Kverchi on 14.07.2017.
 */
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String jsonStr = "{\"login\": \"true\"}";
        out.print(jsonStr);
        out.flush();
    }
}
