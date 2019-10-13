package com.laboratory.ms.base.web;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.laboratory.ms.base.service.HeaderKey;
import com.laboratory.ms.base.service.MdcKey;

public class MdcFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MdcFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean shouldLog = (boolean) request.getAttribute(ShouldLogFilter.SHOULD_LOG);

        if (shouldLog) {
            Instant start = Instant.now();
            HttpServletRequest request2 = (HttpServletRequest) request;
            try {
                MDC.put(MdcKey.IP, request2.getHeader(HeaderKey.IP));
                MDC.put(MdcKey.METHOD, request2.getMethod());
                MDC.put(MdcKey.URL, request2.getRequestURL().toString());
                MDC.put(MdcKey.EVENT_LOG, Boolean.TRUE.toString());
                LOGGER.info("Starting call");
                MDC.put(MdcKey.EVENT_LOG, Boolean.FALSE.toString());
                chain.doFilter(request2, response);
            } finally {
                MDC.put(MdcKey.EVENT_LOG, Boolean.TRUE.toString());
                MDC.put(MdcKey.DURATION_MILLIS, String.valueOf(ChronoUnit.MILLIS.between(start, Instant.now())));
                LOGGER.info("Ending call");
                MDC.remove(MdcKey.IP);
                MDC.remove(MdcKey.METHOD);
                MDC.remove(MdcKey.URL);
                MDC.remove(MdcKey.EVENT_LOG);
                MDC.remove(MdcKey.DURATION_MILLIS);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}
