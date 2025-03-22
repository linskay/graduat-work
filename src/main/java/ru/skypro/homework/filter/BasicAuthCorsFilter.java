package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, DELETE");
            httpServletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}