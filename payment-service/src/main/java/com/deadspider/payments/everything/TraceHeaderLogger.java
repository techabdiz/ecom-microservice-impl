package com.deadspider.payments.everything;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TraceHeaderLogger extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("TRACE ID: " + request.getHeader("X-B3-TraceId"));
        System.out.println("PARENT ID: " + request.getHeader("X-B3-ParentSpanId"));
        System.out.println("SPAN ID: " + request.getHeader("X-B3-SpanId"));
        filterChain.doFilter(request, response);
    }
}