package org.godea.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Injector;
import org.godea.services.JwtUtilService;

import java.io.IOException;

@WebFilter("/*")
public class JwtFilter implements Filter {
    JwtUtilService jwtUtilService;


    @Override
    public void init(FilterConfig filterConfig) {
        Injector.initialize("org.godea");
        jwtUtilService = (JwtUtilService) Injector.getBean(JwtUtilService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.startsWith("/styles/") || path.startsWith("/scripts/")) {
            chain.doFilter(request, response);
            return;
        }

        if (path.equals("/api/login") || path.equals("/api/register")) {
            chain.doFilter(request, response);
            return;
        }

        String token = null;
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if(path.equals("/")) {
            if (token != null) {
                try {
                    Claims claims = jwtUtilService.parse(token);
                    req.setAttribute("claims", claims);
                } catch (Exception ignore) {
                }
            }
            chain.doFilter(request, response);
            return;
        }

        if (token == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Jwt Token");
            return;
        }

        try {
            Claims claims = jwtUtilService.parse(token);
            req.setAttribute("claims", claims);
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
